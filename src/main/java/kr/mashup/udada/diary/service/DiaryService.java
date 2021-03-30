package kr.mashup.udada.diary.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.mashup.udada.diary.dao.DiaryRepository;
import kr.mashup.udada.diary.domain.Diary;
import kr.mashup.udada.diary.dto.RequestDiaryDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.exception.BadRequestException;
import kr.mashup.udada.exception.ResourceNotFoundException;
import kr.mashup.udada.user.dao.UserRepository;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.util.FileNameUtil;
import kr.mashup.udada.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final S3Util s3Util;
    private final FileNameUtil fileNameUtil;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private static final String DIR_NAME = "diary";

    @Transactional
    public void create(User user, RequestDiaryDto requestDto) {
        String coverImgUrl = "";

        if(!requestDto.getImage().isEmpty()) {
            coverImgUrl = s3Util.upload(DIR_NAME, requestDto.getImage());
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
    public void updateDiary(long diaryId, RequestDiaryDto requestdto) {
        String coverImgUrl = requestdto.getImageUrl();

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        if(requestdto.getImageUrl().isEmpty()) {
            String fileName = fileNameUtil.getFileNameFromUrl(diary.getCoverImgUrl());
            if(!fileName.isEmpty()) {
                s3Util.deleteImage(DIR_NAME, fileName);
            }
            if(!requestdto.getImage().isEmpty()) {
                coverImgUrl = s3Util.upload(DIR_NAME, requestdto.getImage());
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

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        Map<String, Object> claims = new HashMap<>();
        claims.put("inviterId", user.getId());
        claims.put("diaryId", diaryId);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String url = "http://ysjleader.com:8080/diaries/invite?token=" + jwt;

        return url;
    }

    @Transactional
    public void inviteMember(String token, User invitee) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Long inviterId = claims.get("inviterId", Long.class);
        Long diaryId = claims.get("diaryId", Long.class);

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(ResourceNotFoundException::new);
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(ResourceNotFoundException::new);

        if(!diary.getUser().contains(inviter) && diary.getUser().contains(invitee)) {
            throw new BadRequestException();
        }

        diary.inviteMember(invitee);
    }
}
