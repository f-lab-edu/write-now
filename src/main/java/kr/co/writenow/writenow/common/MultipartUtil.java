package kr.co.writenow.writenow.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.writenow.writenow.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultipartUtil {

    @Value("${file.rootDir}")
    private String rootDir;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T convertTo(Class<T> clazz, MultipartHttpServletRequest request){
        Iterator<String> names = request.getParameterNames().asIterator();
        Map<String, Object> map = new HashMap<>();
        while(names.hasNext()){
            String paramName = names.next();
            map.put(paramName, request.getParameter(paramName));
        }
        return objectMapper.convertValue(map, clazz);
    }

    public Optional<File> makeFile(MultipartFile multipartFile){
        if(!StringUtils.hasText(multipartFile.getOriginalFilename())){
            return Optional.empty();
        }

        File file = new File(rootDir + "/" + multipartFile.getOriginalFilename());
        try {
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
        }catch (IOException e){
            log.error("MultipartFile을 file 객체로 변형하는데 문제가 발생. {}", e.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 생성하는데 문제가 발생했습니다.");
        }
        return Optional.of(file);
    }
}
