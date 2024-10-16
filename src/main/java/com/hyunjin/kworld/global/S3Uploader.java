package com.hyunjin.kworld.global;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
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

    /**
     * 여러 파일을 업로드하고 각각의 파일 URL을 반환
     */
    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String fileUrl = uploadSingleFile(multipartFile);
                fileUrls.add(fileUrl);
            } catch (IOException e) {
                throw new IOException("파일 업로드 실패: " + multipartFile.getOriginalFilename(), e);
            }
        }
        return fileUrls;
    }

    /**
     * 개별 파일을 S3에 업로드
     */
    private String uploadSingleFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");
        String fileName = baseDir + uniqueFileName;

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

        return generateFileUrl(fileName);
    }

    /**
     * S3 버킷 내의 파일 URL을 생성
     */
    private String generateFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    /**
     * S3에서 파일을 삭제
     */
    public boolean deleteFile(String fileUrl) throws IOException {
        try {
            String key = extractKeyFromUrl(fileUrl);
            System.out.println("Deleting file from S3: " + key);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("S3에서 이미지 삭제 완료: " + key);
            return true;
        } catch (S3Exception e) {
            System.err.println("S3 파일 삭제 중 오류가 발생했습니다: " + fileUrl);
            throw new IOException("S3 파일 삭제 실패: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.err.println("잘못된 URL입니다: " + fileUrl);
            return false;
        }
    }

    /**
     * S3 URL에서 파일 경로만 추출
     */
    private String extractKeyFromUrl(String fileUrl) {
        int startIndex = fileUrl.indexOf(baseDir);
        if (startIndex == -1) {
            throw new IllegalArgumentException("유효하지 않은 S3 파일 URL입니다: " + fileUrl);
        }
        return fileUrl.substring(startIndex);
    }
}