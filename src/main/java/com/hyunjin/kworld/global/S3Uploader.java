package com.hyunjin.kworld.global;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    private final String baseDir = "uploads/";

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

            String fileName = baseDir + uniqueFileName;
            fileUrls.add(uploadToS3(multipartFile, fileName));
        }
        return fileUrls;
    }

    private String uploadToS3(MultipartFile multipartFile, String fileName) throws IOException {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );
        } catch (S3Exception e) {
            throw new IOException("S3 업로드 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    public void deleteFile(String filePath) throws IOException {
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucket).key(filePath).build());
        } catch (S3Exception e) {
            throw new IOException("S3 파일 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}