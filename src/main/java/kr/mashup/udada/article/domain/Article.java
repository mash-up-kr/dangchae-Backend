package kr.mashup.udada.article.domain;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.config.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Builder
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

    private String thumbImg;

    private String image;

    @Builder
    public Article(Long writerId, Long diaryId, String title, String body, String thumbImg, String image) {
        this.writerId = writerId;
        this.diaryId = diaryId;
        this.title = title;
        this.body = body;
        this.thumbImg = thumbImg;
        this.image = image;
    }
}
