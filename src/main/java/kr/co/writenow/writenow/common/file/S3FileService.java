package kr.co.writenow.writenow.common.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import kr.co.writenow.writenow.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private static final String PDF_EXTENSION = "pdf";
    private static final String PDF_CONTENT_TYPE = "application/pdf";
    @Value("${naver.cloud.bucketName}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    private ObjectMetadata makeObjectMetadata(File file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.length());
        objectMetadata.setContentEncoding("UTF-8");
        String extension = FilenameUtils.getExtension(file.getName());
        if (extension.equals(PDF_EXTENSION)) {
            objectMetadata.setContentType(PDF_CONTENT_TYPE);
        }
        return objectMetadata;
    }

    private PutObjectRequest createPutObjectRequest(File file, String fileKey) {
        return new PutObjectRequest(bucketName, String.join("/", fileKey, file.getName()), file)
                .withCannedAcl(CannedAccessControlList.Private)
                .withMetadata(makeObjectMetadata(file));
    }

    private TransferManager createTransferManager() {
        return TransferManagerBuilder
                .standard()
                .withS3Client(amazonS3)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
                .build();
    }

    @Override
    public void upload(File file, String fileKey) {
        TransferManager tm = createTransferManager();
        try {
            PutObjectRequest request = createPutObjectRequest(file, fileKey);
            Upload upload = tm.upload(request);
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            log.error("파일 업로드가 종료되지 않습니다. : {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 업로드 하는데 문제가 발생했습니다.");
        }
    }

    @Override
    public void upload(List<File> files, String fileKey) {
        for (File file : files) {
            upload(file, fileKey);
        }
    }
}
