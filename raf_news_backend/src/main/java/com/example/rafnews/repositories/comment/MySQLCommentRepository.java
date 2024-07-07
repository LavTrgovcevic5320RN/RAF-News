package com.example.rafnews.repositories.comment;

import com.example.rafnews.entities.Comment;
import com.example.rafnews.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCommentRepository extends MySqlAbstractRepository implements CommentRepository {

    @Override
    public List<Comment> allCommentsFromArticle(Integer articleId) {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            String query = "SELECT * FROM comments WHERE article_id = ? ORDER BY created_at DESC";
            statement = connection.prepareStatement(query);
            statement.setInt(1, articleId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setArticleId(resultSet.getInt("article_id"));
                comment.setAuthor(resultSet.getString("author"));
                comment.setText(resultSet.getString("text"));
                comment.setCreatedAt(resultSet.getTimestamp("created_at").getTime());
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comments;
    }

    @Override
    public Comment getCommentById(Integer id) {
        Comment comment = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            String query = "SELECT * FROM comments WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                comment = new Comment();
                comment.setId(resultSet.getInt("id"));
                comment.setArticleId(resultSet.getInt("article_id"));
                comment.setText(resultSet.getString("text"));
                comment.setCreatedAt(resultSet.getTimestamp("created_at").getTime());
                comment.setAuthor(resultSet.getString("author"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            String query = "INSERT INTO comments (author, text, created_at, article_id) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setInt(4, comment.getArticleId());
            preparedStatement.setString(2, comment.getText());
            preparedStatement.setTimestamp(3, new Timestamp(comment.getCreatedAt()));
            preparedStatement.setString(1, comment.getAuthor());
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                comment.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public void deleteComment(Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            String query = "DELETE FROM comments WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }
}

