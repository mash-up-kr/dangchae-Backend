package kr.mashup.udada.article.dto;

import kr.mashup.udada.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ResponseRecentArticleDto {
    private String title;
    private LocalDateTime date;

    public static ResponseRecentArticleDto fromDomain(Article article) {
        return new ResponseRecentArticleDto(article.getTitle(), article.getCreatedAt());
    }
}
