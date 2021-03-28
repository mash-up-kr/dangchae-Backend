package kr.mashup.udada.article.api;

import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.article.dto.ResponseArticleDto;
import kr.mashup.udada.article.dto.ResponseArticleListDto;
import kr.mashup.udada.article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("{articleId}")
    public ResponseEntity<ResponseArticleDto> getArticle(@PathVariable Long articleId, @RequestParam Long diaryId) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.findArticle(diaryId, articleId));
    }

    @GetMapping("")
    public ResponseEntity<Page<List<ResponseArticleListDto>>> getArticles(@RequestParam Long diaryId,
                                                                          @RequestParam YearMonth date,
                                                                          @RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(articleService.findArticleList(diaryId, date, PageRequest.of(page, 2, Sort.by("createdAt"))));
    }


    @PostMapping("")
    public ResponseEntity<ResponseArticleDto> writeArticle(@ModelAttribute RequestWriteArticleDto requestBody) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.writeArticle(requestBody));
    }

    @PutMapping("")
    public ResponseEntity<ResponseArticleDto> updateArticle(@RequestParam Long diaryId, @RequestParam Long articleId,
                                                            @ModelAttribute RequestWriteArticleDto requestBody) {

        return ResponseEntity.status(HttpStatus.OK).body(articleService.updateArticle(requestBody, diaryId, articleId));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteArticle(@RequestParam Long diaryId, @RequestParam Long articleId) {
        articleService.deleteArticle(diaryId, articleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
