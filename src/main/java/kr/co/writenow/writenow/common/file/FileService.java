package kr.co.writenow.writenow.common.file;

import java.io.File;
import java.util.List;

public interface FileService {

    void upload(File file, String fileKey);

    void upload(List<File> files, String fileKey);
}
