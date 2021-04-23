package kr.mashup.udada.diary.api;

import kr.mashup.udada.diary.dto.RequestDiaryDto;
import kr.mashup.udada.diary.dto.ResponseDiaryDto;
import kr.mashup.udada.diary.dto.ResponseInvitationUrlDto;
import kr.mashup.udada.diary.service.DiaryService;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/diaries")
@RestController
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity createDiary(@ModelAttribute RequestDiaryDto requestDto) {
        User user = userService.getFromUsername();
        diaryService.create(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDiaryDto>> getDiary() {
        User user = userService.getFromUsername();
        List<ResponseDiaryDto> diaryList = diaryService.getDiariesOf(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(diaryList);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateDiary(@PathVariable("id") long diaryId,
                                      @ModelAttribute RequestDiaryDto requestDto) {
        User user = userService.getFromUsername();
        diaryService.updateDiary(diaryId, requestDto, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDiary(@PathVariable("id") long diaryId) {
        User user = userService.getFromUsername();
        diaryService.deleteDiary(diaryId, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/invite")
    public ResponseEntity makeInvitationUrl(@PathVariable("id") long diaryId) {
        User user = userService.getFromUsername();
        String url = diaryService.makeInvitationUrl(diaryId, user);
        ResponseInvitationUrlDto invitationDto = new ResponseInvitationUrlDto(url);
        return ResponseEntity.status(HttpStatus.OK).body(invitationDto);
    }

    @PostMapping("/invite")
    public ResponseEntity inviteMember(@RequestParam(value = "token") String token) {
        User invitee = userService.getFromUsername();
        diaryService.inviteMember(token, invitee);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
