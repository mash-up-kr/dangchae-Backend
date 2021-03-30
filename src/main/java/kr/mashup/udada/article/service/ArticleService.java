package kr.mashup.udada.article.service;

import kr.mashup.udada.article.dao.ArticleRepository;
import kr.mashup.udada.article.domain.Article;
import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.article.dto.ResponseArticleDto;
import kr.mashup.udada.article.dto.ResponseArticleListDto;
import kr.mashup.udada.exception.ResourceNotFoundException;
import kr.mashup.udada.util.S3Util;
import kr.mashup.udada.util.Thumbnail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final S3Util s3Util;

    private final Thumbnail thumbnail;

    private static final String THUMB_DIR_NAME = "thumbnail";
    private static final String ARTICLE_DIR_NAME = "article";

    @Transactional
    public ResponseArticleDto writeArticle(RequestWriteArticleDto requestBody) {
        String thumbImgUrl = "";
        String imgUrl = "";

        if (requestBody.getImage() != null) {
//            thumbImgUrl = s3Util.upload(THUMB_DIR_NAME, thumbnail.createThumbnail(requestBody.getImage()));
            imgUrl = s3Util.upload(ARTICLE_DIR_NAME, requestBody.getImage());
        }

        Article article = requestBody.toEntity(thumbImgUrl, imgUrl);

        return new ResponseArticleDto(articleRepository.save(article));
    }

    public ResponseArticleDto findArticle(Long diaryId, Long articleId) {
        return new ResponseArticleDto(articleRepository.findByIdAndDiaryId(articleId, diaryId)
                .orElseThrow(ResourceNotFoundException::new));
    }

    public Page<List<ResponseArticleListDto>> findArticleList (Long diaryId, YearMonth date, Pageable pageable) {
        LocalDateTime start = date.atDay(1).atStartOfDay();
        LocalDateTime end = date.atEndOfMonth().atTime(23, 59, 59);

        return articleRepository.findByDiaryIdAndCreatedAtBetween(diaryId, start, end, pageable);
    }

    @Transactional
    public ResponseArticleDto updateArticle(RequestWriteArticleDto requestBody, Long diaryId, Long articleId) {

        Article article = articleRepository.findByIdAndDiaryId(articleId, diaryId)
                .orElseThrow(ResourceNotFoundException::new);

        String thumbnailName = "";
        String imageName = "";

        if(requestBody.getImage() != null) {
            thumbnailName = s3Util.parseUrl(article.getThumbnail());
            imageName = s3Util.parseUrl(article.getImage());

            s3Util.deleteImage(THUMB_DIR_NAME, thumbnailName);
            s3Util.deleteImage(ARTICLE_DIR_NAME, imageName);

            s3Util.upload(THUMB_DIR_NAME, thumbnail.createThumbnail(requestBody.getImage()));
            s3Util.upload(ARTICLE_DIR_NAME, requestBody.getImage());
        }

        article.update(requestBody, thumbnailName, imageName);
        return new ResponseArticleDto(articleRepository.save(article));
    }

    @Transactional
    public void deleteArticle(Long diaryId, Long articleId) {
        articleRepository.deleteByIdAndDiaryId(articleId, diaryId);
    }
}
