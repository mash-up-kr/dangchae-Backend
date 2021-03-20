package kr.mashup.udada.article.dao;

import kr.mashup.udada.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public List<Article> findByDiaryIdAndArticleId(Long diaryId, Long articleId);
}
