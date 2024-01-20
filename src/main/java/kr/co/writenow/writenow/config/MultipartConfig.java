package kr.co.writenow.writenow.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("c:\\opt\\");
        factory.setMaxRequestSize(DataSize.ofMegabytes(1L));
        factory.setMaxFileSize(DataSize.ofMegabytes(10L));
        return factory.createMultipartConfig();
    }
}
