package kr.mashup.udada.article.api;

import kr.mashup.udada.article.dto.RequestWriteArticleDto;
import kr.mashup.udada.article.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/diaries/{diaryId}/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity getArticles(@RequestParam Date date) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("{articleId}")
    public ResponseEntity getArticleById(@PathVariable Long articleId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("")
    public ResponseEntity writeArticle(@PathVariable Long diaryId, @RequestBody RequestWriteArticleDto requestWriteArticleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.writeArticle(requestWriteArticleDto));
    }

    @PutMapping("/{articleId}")
    public ResponseEntity modifyArticle(@PathVariable Long articleId, @PathVariable Long diaryId, @RequestBody RequestWriteArticleDto requestBody) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{artiicleId}")
    public ResponseEntity deleteArticle(@PathVariable Long diaryId) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
