package kr.mashup.udada.article.dto;

import com.sun.istack.Nullable;
import kr.mashup.udada.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RequestWriteArticleDto {
    private Long writerId;

    private Long diaryId;

    private String title;

    private String body;

    @Nullable
    private MultipartFile img;

    public Article toEntity(String thumbnailUrl, String imgUrl) {
        return Article.builder()
                .writerId(writerId)
                .diaryId(diaryId)
                .title(title)
                .body(body)
                .thumbImg(thumbnailUrl)
                .image(imgUrl)
                .build();
    }
}
