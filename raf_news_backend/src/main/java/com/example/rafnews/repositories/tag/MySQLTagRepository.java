package com.example.rafnews.repositories.tag;

import com.example.rafnews.entities.Tag;
import com.example.rafnews.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLTagRepository extends MySqlAbstractRepository implements TagRepository {

    @Override
    public Tag addTag(Tag tag) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedCols = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO tags (name) VALUES(?)", generatedCols);
            preparedStatement.setString(1,  tag.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                tag.setId(resultSet.getInt(1));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tag;
    }

    @Override
    public List<Tag> allTags() {
        List<Tag> tags = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from tags");

            while(resultSet.next()){
                tags.add(new Tag(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tags;
    }

    @Override
    public Tag findTag(Integer id) {
        Tag tag = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM tags where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int categoryId = resultSet.getInt("id");
                String name = resultSet.getString("name");

                tag = new Tag(categoryId, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return tag;
    }

    public List<Tag> getTagsByArticleId(Integer articleId) {
        List<Tag> tags = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            String query = "SELECT t.id, t.name FROM tags t " +
                    "INNER JOIN article_tags at ON at.tag_id = t.id " +
                    "WHERE at.article_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, articleId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Integer tagId = resultSet.getInt("id");
                String tagName = resultSet.getString("name");
                Tag tag = new Tag(tagId, tagName);
                tags.add(tag);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return tags;
    }

}
