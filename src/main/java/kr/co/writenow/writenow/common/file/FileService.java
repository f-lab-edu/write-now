package kr.co.writenow.writenow.common.file;

import java.io.File;
import java.util.List;

public interface FileService {

    void upload(File file, String fileKey);

    default void upload(List<File> files, String fileKey){
        for (File file : files) {
            this.upload(file, fileKey);
        }
    }
}
