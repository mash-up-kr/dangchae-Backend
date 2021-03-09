package kr.mashup.udada.diary.api;

import kr.mashup.udada.diary.dto.RequestDiaryCreateDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.diary.service.DiaryService;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/diaries")
@RestController
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity createDiary(@RequestPart(value = "image") MultipartFile image,
                                 @ModelAttribute RequestDiaryCreateDto requestDto) {

        User user = userService.getFromUsername();
        diaryService.create(user, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDiaryDto>>findDiary() {

        User user = userService.getFromUsername();
        List<ResponseDiaryDto> diaryList = diaryService.findDiariesOf(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(diaryList);
    }

}
