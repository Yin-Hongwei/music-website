package com.example.yin.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequestMapping("/download")
public class FileDownloadController {

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // 加载文件作为资源
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song\\"+fileName;
        File file=new File(filePath);

        byte[] musicBytes = getMusicBytesFromFileName(file);
        // 创建一个ByteArrayResource对象，用于包装字节数组
        ByteArrayResource resource = new ByteArrayResource(musicBytes);
        // 构建响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // 返回一个 ResponseEntity 对象
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(musicBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // 这个方法需要根据你的实际情况来获取音乐文件的字节数组
    private byte[] getMusicBytesFromFileName(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            // 如果发生异常，你可以返回一个空数组或者其他适当的错误处理
            return new byte[0];
        }
    }
}
