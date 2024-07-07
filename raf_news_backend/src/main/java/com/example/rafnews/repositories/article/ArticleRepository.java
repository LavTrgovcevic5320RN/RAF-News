package com.example.rafnews.repositories.article;

import com.example.rafnews.entities.Article;

import java.util.List;

public interface ArticleRepository {
    Article addArticle(Article article);
    List<Article> allArticles();
    Article findArticle(Integer id);
    void deleteArticle(Integer id);
    List<Article> getArticlePage(Integer offset);
    void incrementArticleViewCount(Integer articleId);
    List<Article> getMostViewedArticles();
    List<Article> getArticlesByCategory(Integer categoryId);
    List<Article> getArticlesByTag(Integer tagId);
}

