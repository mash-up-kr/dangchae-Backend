package kr.mashup.udada.article.dto;

import kr.mashup.udada.article.domain.Article;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class RequestWriteArticleDto {
    private Long writerId;

    private Long diaryId;

    private String title;

    private String body;

    @Nullable
    private MultipartFile image;

    @Builder
    private RequestWriteArticleDto(Long writerId, Long diaryId, String title, String body, MultipartFile image) {
        this.writerId = writerId;
        this.diaryId = diaryId;
        this.title = title;
        this.body = body;
        this.image = image;
    }

    public Article toEntity(String thumbnailUrl, String imgUrl) {
        return Article.builder()
                .writerId(writerId)
                .diaryId(diaryId)
                .title(title)
                .body(body)
                .thumbnail(thumbnailUrl)
                .image(imgUrl)
                .build();
    }
}

