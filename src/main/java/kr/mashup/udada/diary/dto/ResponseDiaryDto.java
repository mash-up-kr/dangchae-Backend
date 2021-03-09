package kr.mashup.udada.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseDiaryDto {

    private Long diaryId;

    private String title;

    private List<String> memberNicknameList;

    private String coverImgUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdDate;

    @Builder
    private ResponseDiaryDto(Long id, String title, List<String> nicknameList, String coverImgUrl, LocalDate createdDate) {
        this.diaryId = id;
        this.title = title;
        this.memberNicknameList = nicknameList;
        this.coverImgUrl = coverImgUrl;
        this.createdDate = createdDate;
    }
}
