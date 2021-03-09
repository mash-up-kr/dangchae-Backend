package kr.mashup.udada.diary.service;

import kr.mashup.udada.diary.dao.DiaryRepository;
import kr.mashup.udada.diary.domain.Diary;
import kr.mashup.udada.diary.dto.RequestDiaryCreateDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final S3Util s3Util;

    private static final String DIR_NAME = "diary";

    @Transactional
    public void create(User user, RequestDiaryCreateDto requestDto) {
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
}
