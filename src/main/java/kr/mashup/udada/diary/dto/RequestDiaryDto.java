package kr.mashup.udada.diary.dto;

import kr.mashup.udada.diary.domain.Diary;
import kr.mashup.udada.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class RequestDiaryDto {

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    private MultipartFile image;

    @Builder
    private RequestDiaryDto(String title, LocalDate createdDate, MultipartFile image) {
        this.title = title;
        this.createdDate = createdDate;
        this.image = image;
    }

    public Diary toEntity(String coverImgUrl, User user) {
        return Diary.builder()
                .title(title)
                .date(createdDate)
                .coverImgUrl(coverImgUrl)
                .user(user)
                .build();
    }
}
