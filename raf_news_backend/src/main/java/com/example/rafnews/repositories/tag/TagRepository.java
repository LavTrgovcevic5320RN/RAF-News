package com.example.rafnews.repositories.tag;

import com.example.rafnews.entities.Tag;

import java.util.List;

public interface TagRepository {

    Tag addTag(Tag tag);
    List<Tag> allTags();
    Tag findTag(Integer id);
    List<Tag> getTagsByArticleId(Integer articleId);
}