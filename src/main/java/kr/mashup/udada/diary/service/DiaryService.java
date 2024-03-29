package kr.mashup.udada.diary.service;

import kr.mashup.udada.config.jwt.InvitationJwt;
import kr.mashup.udada.diary.dao.DiaryRepository;
import kr.mashup.udada.diary.domain.Diary;
import kr.mashup.udada.diary.dto.RequestDiaryDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.diary.vo.InvitationInfo;
import kr.mashup.udada.exception.BadRequestException;
import kr.mashup.udada.exception.ResourceNotFoundException;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.user.domain.UserRepository;
import kr.mashup.udada.util.FileUtil;
import kr.mashup.udada.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;
    private final FileUtil fileUtil;
    private final InvitationJwt invitationJwt;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private static final String DIR_NAME = "diary";

    @Transactional
    public void create(User user, RequestDiaryDto requestDto) {
        String coverImgUrl = "";

        if(!requestDto.getImage().isEmpty()) {
            coverImgUrl = s3Util.upload(DIR_NAME, requestDto.getImage(), user.getUsername());
        }
        Diary diary = requestDto.toEntity(coverImgUrl, user);
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public List<ResponseDiaryDto> getDiariesOf(Long userId) {
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
    public void updateDiary(long diaryId, RequestDiaryDto requestdto, User user) {
        String coverImgUrl = requestdto.getImageUrl();
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        if(requestdto.getImageUrl().isEmpty()) {
            String fileName = fileUtil.getFileNameFromUrl(diary.getCoverImgUrl());
            if(!fileName.isEmpty()) {
                s3Util.deleteImage(fileName);
            }
            if(!requestdto.getImage().isEmpty()) {
                coverImgUrl = s3Util.upload(DIR_NAME, requestdto.getImage(), user.getUsername());
            }
        }

        diary.update(requestdto, coverImgUrl);
    }

    @Transactional
    public void deleteDiary(long diaryId, User user) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        diary.getUser().remove(user);
    }

    public String makeInvitationUrl(long diaryId, User user) {
        InvitationInfo info = new InvitationInfo(diaryId, user.getId());
        String token = invitationJwt.createToken(info);
        return "http://ysjleader.com:8080/diaries/invite?token=" + token;
    }

    @Transactional
    public void inviteMember(String token, User invitee) {
        InvitationInfo info = invitationJwt.getInfoFromToken(token);

        Diary diary = diaryRepository.findById(info.getDiaryId())
                .orElseThrow(ResourceNotFoundException::new);
        User inviter = userRepository.findById(info.getInviterId())
                .orElseThrow(ResourceNotFoundException::new);

        if(!diary.getUser().contains(inviter) && diary.getUser().contains(invitee)) {
            throw new BadRequestException();
        }
        diary.inviteMember(invitee);
    }
}
