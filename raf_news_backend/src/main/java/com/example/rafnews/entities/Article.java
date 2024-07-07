package com.example.rafnews.entities;

import java.util.ArrayList;
import java.util.List;

public class Article {

    private Integer id;

    private String title;
    private String text;
    private Long createdAt;
    private int viewCnt;

    private String author;
    private Integer categoryId;
    private List<Integer> tagIds;

    public Article() {
        this.tagIds = new ArrayList<>();
    }

    public Article(Integer id, String title, String text, Long createdAt, int viewCnt, String author, Integer categoryId, List<Integer> tagIds) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.viewCnt = viewCnt;
        this.author = author;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }

    public Article(Integer id, String title, String text, Long createdAt, int viewCnt, String author, Integer categoryId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.viewCnt = viewCnt;
        this.author = author;
        this.categoryId = categoryId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(int viewCnt) {
        this.viewCnt = viewCnt;
    }
}
