package kr.mashup.udada.article.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseArticleListDto {
    private Long diaryId;
    private Long id;    //articleId
    private String title;
    private String thumbnail;
    private LocalDateTime createdAt;
}
