package com.example.yin.controller;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;


@Controller
public class MinioController {
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Autowired
    private MinioClient minioClient;
    @GetMapping("/user01/{fileName:.+}")
    public ResponseEntity<byte[]> getMusic(@PathVariable String fileName) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();
            InputStream inputStream = minioClient.getObject(args);

            // 将文件内容读取为字节数组
            byte[] bytes = IOUtils.toByteArray(inputStream);

            // 设置响应头，指示浏览器如何处理响应内容
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName); // 如果需要下载文件，可以使用此头部

            // 返回字节数组作为响应
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
