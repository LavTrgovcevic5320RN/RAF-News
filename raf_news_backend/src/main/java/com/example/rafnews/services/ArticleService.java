package com.example.rafnews.services;

import com.example.rafnews.entities.Article;
import com.example.rafnews.repositories.article.ArticleRepository;

import javax.inject.Inject;
import java.util.List;

public class ArticleService {

    public ArticleService(){
        System.out.println(this);
    }

    @Inject
    private ArticleRepository articleRepository;

    public Article addArticle(Article article) {
        return this.articleRepository.addArticle(article);
    }

    public List<Article> allArticles() {
        return this.articleRepository.allArticles();
    }

    public Article findArticle(Integer id) {
        return this.articleRepository.findArticle(id);
    }

    public void deleteArticle(Integer id) {
        this.articleRepository.deleteArticle(id);
    }

    public List<Article> getArticlePage(Integer offset) {
        return this.articleRepository.getArticlePage(offset);
    }

    public void incrementArticleViewCount(Integer articleId) {
        this.articleRepository.incrementArticleViewCount(articleId);
    }

    public List<Article> getMostViewedArticles() {
        return this.articleRepository.getMostViewedArticles();
    }

    public List<Article> getArticlesByCategory(Integer categoryId){
        return this.articleRepository.getArticlesByCategory(categoryId);
    }

    public List<Article> getArticlesByTag(Integer tagId) {
        return this.articleRepository.getArticlesByTag(tagId);
    }
}
