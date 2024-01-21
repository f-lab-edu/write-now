package kr.co.writenow.writenow.common.file;

import kr.co.writenow.writenow.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

    @Value("${file.baseDir}")
    private String baseDir;

    public Optional<File> makeFile(MultipartFile multipartFile){
        if(!StringUtils.hasText(multipartFile.getOriginalFilename())){
            return Optional.empty();
        }
        File file = new File(baseDir + "/" + multipartFile.getOriginalFilename());
        try {
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
            return Optional.of(file);
        }catch (IOException e){
            log.error("MultipartFile을 file 객체로 변형하는데 문제가 발생. {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 생성하는데 문제가 발생했습니다.");
        }
    }

    public static void delete(File file){
        boolean isDeleted = file.delete();
        if(isDeleted){
            log.warn("filePath: {} , 파일이 정상적으로 삭제되지 않았습니다.", file.getAbsoluteFile());
        }
    }

    public static void delete(List<File> files){
        for (File file : files) {
            delete(file);
        }
    }
}
