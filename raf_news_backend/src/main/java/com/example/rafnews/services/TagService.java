package com.example.rafnews.services;

import com.example.rafnews.entities.Tag;
import com.example.rafnews.repositories.tag.TagRepository;

import javax.inject.Inject;
import java.util.List;

public class TagService {

    public TagService(){
        System.out.println(this);
    }

    @Inject
    private TagRepository tagRepository;

    public Tag addTag(Tag tag) {
        return this.tagRepository.addTag(tag);
    }

    public List<Tag> allTags() {
        return this.tagRepository.allTags();
    }

    public Tag findTag(Integer id) {
        return this.tagRepository.findTag(id);
    }

    public List<Tag> getTagsByArticleId(Integer articleId) {
        return this.tagRepository.getTagsByArticleId(articleId);
    }
}
