package com.example.rafnews.repositories.comment;

import com.example.rafnews.entities.Comment;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);
    List<Comment> allCommentsFromArticle(Integer articleId);
    Comment getCommentById(Integer id);
    void deleteComment(Integer id);
}
