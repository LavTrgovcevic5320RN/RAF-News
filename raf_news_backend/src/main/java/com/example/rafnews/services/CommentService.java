package com.example.rafnews.services;

import com.example.rafnews.entities.Comment;
import com.example.rafnews.repositories.comment.CommentRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentService {

    public CommentService() {
        System.out.println(this);
    }

    @Inject
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return this.commentRepository.addComment(comment);
    }

    public List<Comment> allCommentsFromArticle(Integer articleId){
        return this.commentRepository.allCommentsFromArticle(articleId);
    }

    public Comment getCommentById(Integer id) {
        return this.commentRepository.getCommentById(id);
    }

    public void deleteComment(Integer id) {
        this.commentRepository.deleteComment(id);
    }
}
