package com.example.yin.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Service
public class MinioUploadController {

    private static MinioClient minioClient;
    private static String bucketName;

    public static void init(){
        Properties properties = new Properties();
        try {
            // 使用类加载器获取资源文件的输入流
            InputStream inputStream = MinioUploadController.class.getClassLoader().getResourceAsStream("application-dev.properties");
            if (inputStream != null) {
                properties.load(inputStream);
                String minioEndpoint = properties.getProperty("minio.endpoint");
                String minioAccessKey = properties.getProperty("minio.access-key");
                String minioSecretKey = properties.getProperty("minio.secret-key");
                String minioBucketName = properties.getProperty("minio.bucket-name");
                bucketName = minioBucketName;
                minioClient = MinioClient.builder()
                        .endpoint(minioEndpoint)
                        .credentials(minioAccessKey, minioSecretKey)
                        .build();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static String uploadFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/singer/img/"+file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadSonglistImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/songlist/"+file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadSongImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/singer/song/"+file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String uploadAtorImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/img/avatorImages/"+file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
