package kr.mashup.udada.diary.service;

import kr.mashup.udada.diary.dao.DiaryRepository;
import kr.mashup.udada.diary.domain.Diary;
import kr.mashup.udada.diary.dto.RequestDiaryDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.exception.ResourceNotFoundException;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final S3Util s3Util;

    private static final String DIR_NAME = "diary";

    @Transactional
    public void create(User user, RequestDiaryDto requestDto) {
        String coverImgUrl = s3Util.upload(DIR_NAME, requestDto.getImage());
        Diary diary = requestDto.toEntity(coverImgUrl, user);
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public List<ResponseDiaryDto> findDiariesOf(Long userId) {
        return diaryRepository.findByUserId(userId)
                .stream()
                .map(diary -> ResponseDiaryDto.builder()
                        .id(diary.getId())
                        .title(diary.getTitle())
                        .nicknameList(diary.getUser().stream()
                                .map(User::getNickname)
                                .collect(Collectors.toList()))
                        .coverImgUrl(diary.getCoverImgUrl())
                        .createdDate(diary.getDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(long diaryId, RequestDiaryDto requestdto) {
        String coverImgUrl = "";

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        if(!requestdto.getImage().isEmpty()) {
            String fileName = parseUrl(diary.getCoverImgUrl());
            s3Util.deleteImage(DIR_NAME, fileName);

            coverImgUrl = s3Util.upload(DIR_NAME, requestdto.getImage());
        }

        diary.update(requestdto, coverImgUrl);
    }

    @Transactional
    public void deleteDiary(long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        String fileName = parseUrl(diary.getCoverImgUrl());
        s3Util.deleteImage(DIR_NAME, fileName);
        diaryRepository.deleteById(diaryId);
    }

    private String parseUrl(String url) {
        String fileName = "";
        try {
            URL fullURL = new URL(url);
            String filePath = fullURL.getFile().split("/")[1];
            fileName = fullURL.getFile().split("/")[2];
        } catch(MalformedURLException e) {
            log.info(e.getMessage());
        }
        return fileName;
    }
}
