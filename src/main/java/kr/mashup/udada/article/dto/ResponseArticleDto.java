package kr.mashup.udada.article.dto;

import kr.mashup.udada.article.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ResponseArticleDto {
    private Long articleId;

    private Long writerId;

    private Long diaryId;

    private String title;

    private String body;

    private String thumbImg;

    private String img;

    private LocalDateTime date;

    @Builder
    public ResponseArticleDto(Article article) {
        this.articleId = article.getId();
        this.writerId = article.getWriterId();
        this.diaryId = article.getDiaryId();
        this.title = article.getTitle();
        this.body = article.getBody();
        this.thumbImg = article.getThumbnail();
        this.img = article.getImage();
        this.date = article.getCreatedAt();
    }

}
