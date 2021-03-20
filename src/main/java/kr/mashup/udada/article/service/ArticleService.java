package kr.mashup.udada.article.service;

import kr.mashup.udada.article.dao.ArticleRepository;
import kr.mashup.udada.article.domain.Article;
import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.article.dto.ArticleResponseDto;
import kr.mashup.udada.util.S3Util;
import kr.mashup.udada.util.Thumbnail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

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
    public ArticleResponseDto writeArticle(RequestWriteArticleDto requestBody) {
        String thumbImgUrl = s3Util.upload(THUMB_DIR_NAME, thumbnail.createThumbnail(requestBody.getImg()));
        String imgUrl = s3Util.upload(ARTICLE_DIR_NAME, requestBody.getImg());

        Article article = requestBody.toEntity(thumbImgUrl, imgUrl);

        return new ArticleResponseDto(articleRepository.save(article));
    }
}