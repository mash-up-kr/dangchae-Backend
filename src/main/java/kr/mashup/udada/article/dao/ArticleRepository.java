package kr.mashup.udada.article.dao;

import kr.mashup.udada.article.domain.Article;
import kr.mashup.udada.article.dto.ResponseArticleListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByIdAndDiaryId(Long id, Long diaryId);

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM article WHERE id=? AND diary_id=?")
    void deleteByIdAndDiaryId(Long id, Long diaryId);

    Page<List<ResponseArticleListDto>> findByDiaryIdAndCreatedAtBetween(Long diaryId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Article findByWriterIdOrderByCreatedAtDesc(Long id);
}
