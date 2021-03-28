package kr.mashup.udada.article.domain;

import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.article.dto.ResponseArticleListDto;
import kr.mashup.udada.config.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@SqlResultSetMapping(
        name = "responseArticleListDto",
        classes = @ConstructorResult(
                targetClass = ResponseArticleListDto.class,
                columns = {
                        @ColumnResult(name="diaryId", type=Long.class),
                        @ColumnResult(name="id", type=Long.class),
                        @ColumnResult(name="title", type=String.class),
                        @ColumnResult(name="thumbnail", type=String.class),
                        @ColumnResult(name="createdAt", type= LocalDateTime.class),
                }))
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long writerId;

    @NotNull
    private Long diaryId;

    @NotNull
    private String title;

    @NotNull
    private String body;

    @Nullable
    private String thumbnail;

    @Nullable
    private String image;

    @Builder
    public Article(Long writerId, Long diaryId, String title, String body, String thumbnail, String image) {
        this.writerId = writerId;
        this.diaryId = diaryId;
        this.title = title;
        this.body = body;
        this.thumbnail = thumbnail;
        this.image = image;
    }

    public void update(RequestWriteArticleDto requestBody, String thumbImg, String image) {
        this.title = requestBody.getTitle();
        this.body = requestBody.getBody();

        this.thumbnail = thumbImg;
        this.image = image;
    }
}
