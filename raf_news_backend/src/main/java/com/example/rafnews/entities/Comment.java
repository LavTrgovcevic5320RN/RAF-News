package com.example.rafnews.entities;

public class Comment {
    private Integer id;
    private String author;
    private String text;
    private Long createdAt;
    private Integer articleId;

    public Comment(Integer id, String author, String text, Long createdAt, Integer articleId) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.createdAt = createdAt;
        this.articleId = articleId;
    }

    public Comment(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
