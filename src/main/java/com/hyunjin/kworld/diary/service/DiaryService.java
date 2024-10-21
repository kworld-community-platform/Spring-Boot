package com.hyunjin.kworld.diary.service;

import com.hyunjin.kworld.comment.dto.CommentResponseDto;
import com.hyunjin.kworld.comment.repository.CommentRepository;
import com.hyunjin.kworld.diary.dto.DiaryRequestDto;
import com.hyunjin.kworld.diary.dto.DiaryResponseDto;
import com.hyunjin.kworld.diary.dto.DiaryUpdateRequestDto;
import com.hyunjin.kworld.diary.dto.ImageDto;
import com.hyunjin.kworld.diary.entity.Diary;
import com.hyunjin.kworld.diary.entity.DiaryImage;
import com.hyunjin.kworld.diary.repository.DiaryRepository;
import com.hyunjin.kworld.global.S3Uploader;
import com.hyunjin.kworld.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;
    private final CommentRepository commentRepository;

    @Transactional
    public DiaryResponseDto createDiary(List<MultipartFile> images, DiaryRequestDto diaryRequestDto, Member member) {
        Diary diary = new Diary(diaryRequestDto.getTitle(), diaryRequestDto.getContent(), member);
        diaryRepository.save(diary);

        List<ImageDto> imageDtos = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            try {
                List<String> imageUrls = s3Uploader.uploadFiles(images);

                for (String imageUrl : imageUrls) {
                    DiaryImage diaryImage = new DiaryImage(imageUrl, diary);
                    diary.addImage(diaryImage);
                }

                diaryRepository.saveAndFlush(diary);

                for(DiaryImage diaryImage : diary.getImages()) {
                    imageDtos.add(new ImageDto(diaryImage.getId(), diaryImage.getImageUrl()));
                }

            } catch (IOException e) {
                throw new RuntimeException("S3 파일 업로드 중 오류가 발생했습니다.", e);
            }
        }
        return new DiaryResponseDto(diary.getId(), diary.getTitle(), diary.getContent(), diary.getLikeCount(), imageDtos);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponseDto> getAllDiary(Long memberId) {
        List<Diary> diaries = diaryRepository.findAllByMemberIdWithImagesOrdered(memberId);
        return diaries.stream()
                .map(diary -> {
                    List<ImageDto> sortedImages = diary.getImages().stream()
                            .sorted(Comparator.comparingInt(DiaryImage::getOrder))
                            .map(img -> new ImageDto(img.getId(), img.getImageUrl()))
                            .collect(Collectors.toList());

                    List<CommentResponseDto> comments = commentRepository.findByDiaryIdOrderByCreatedAtDesc(diary.getId()).stream()
                            .map(CommentResponseDto::new)
                            .collect(Collectors.toList());

                    return new DiaryResponseDto(
                            diary.getId(),
                            diary.getTitle(),
                            diary.getContent(),
                            diary.getLikeCount(),
                            sortedImages,
                            comments
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryResponseDto getOneDiary(Long diaryId, Member member) {
        Diary diary = diaryRepository.findByIdWithImagesOrdered(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("다이어리가 존재하지 않습니다."));

        if (!diary.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<ImageDto> sortedImages = diary.getImages().stream()
                .sorted(Comparator.comparingInt(DiaryImage::getOrder))
                .map(img -> new ImageDto(img.getId(), img.getImageUrl()))
                .collect(Collectors.toList());

        List<CommentResponseDto> comments = commentRepository.findByDiaryIdOrderByCreatedAtDesc(diaryId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        return new DiaryResponseDto(diary.getId(), diary.getTitle(), diary.getContent(), diary.getLikeCount(), sortedImages, comments);
    }

    @Transactional
    public DiaryResponseDto updateDiary(Long diaryId, DiaryUpdateRequestDto diaryUpdateRequestDto, Member member) throws IOException {
        Diary diary = diaryRepository.findByIdWithImagesOrdered(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        if (!diary.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        if (diaryUpdateRequestDto.getDeleteImgIds() != null) {
            List<DiaryImage> imagesToRemove = diary.getImages().stream()
                    .filter(img -> diaryUpdateRequestDto.getDeleteImgIds().contains(img.getId()))
                    .collect(Collectors.toList());

            for (DiaryImage img : imagesToRemove) {
                System.out.println("Deleting image from diary: " + img.getImageUrl());

                diary.removeImage(img);
                s3Uploader.deleteFile(img.getImageUrl());
            }
        }

        if (diaryUpdateRequestDto.getReplaceImgIds() != null && diaryUpdateRequestDto.getReplaceImages() != null) {
            List<DiaryImage> updatedImages = new ArrayList<>(diary.getImages());

            for (int i = 0; i < diaryUpdateRequestDto.getReplaceImgIds().size(); i++) {
                Long existingId = diaryUpdateRequestDto.getReplaceImgIds().get(i);
                MultipartFile newImage = diaryUpdateRequestDto.getReplaceImages().get(i);

                DiaryImage existingImage = updatedImages.stream()
                        .filter(img -> img.getId().equals(existingId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("이미지가 존재하지 않습니다: " + existingId));

                int currentOrder = existingImage.getOrder();

                updatedImages.remove(existingImage);
                s3Uploader.deleteFile(existingImage.getImageUrl());

                String newImageUrl = s3Uploader.uploadFiles(List.of(newImage)).get(0);
                DiaryImage newDiaryImage = new DiaryImage(newImageUrl, diary, currentOrder);

                updatedImages.add(currentOrder, newDiaryImage);
            }

            diary.getImages().clear();
            diary.getImages().addAll(updatedImages);
        }


        if (diaryUpdateRequestDto.getNewImages() != null) {
            List<String> newImageUrls = s3Uploader.uploadFiles(diaryUpdateRequestDto.getNewImages());
            int currentOrder = diary.getImages().size();

            for (String url : newImageUrls) {
                System.out.println("Adding new image: " + url);
                diary.addImage(new DiaryImage(url, diary, currentOrder++));
            }
        }

        diary.update(diaryUpdateRequestDto.getTitle(), diaryUpdateRequestDto.getContent());

        diaryRepository.save(diary);

        List<ImageDto> updatedImageDtos = diary.getImages().stream()
                .sorted(Comparator.comparingInt(DiaryImage::getOrder))
                .map(img -> new ImageDto(img.getId(), img.getImageUrl()))
                .collect(Collectors.toList());

        List<CommentResponseDto> comments = diary.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        return new DiaryResponseDto(diary.getId(), diary.getTitle(), diary.getContent(), diary.getLikeCount(), updatedImageDtos, comments);
    }

    @Transactional
    public void deleteDiary(Long diaryId, Member member) {
        Diary diary = diaryRepository.findByIdWithImagesOrdered(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리입니다."));

        if (!diary.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        deleteAllImagesFromS3(diary);
        diaryRepository.delete(diary);
    }

    private void deleteAllImagesFromS3(Diary diary) {
        for (DiaryImage image : diary.getImages()) {
            System.out.println("Deleting file from S3: " + image.getImageUrl());
            try {
                boolean isDeleted = s3Uploader.deleteFile(image.getImageUrl());
                System.out.println("S3 삭제 결과: " + isDeleted + " / URL: " + image.getImageUrl());
            } catch (IOException e) {
                System.err.println("이미지 삭제 중 오류: " + e.getMessage());
            }
        }
    }
}