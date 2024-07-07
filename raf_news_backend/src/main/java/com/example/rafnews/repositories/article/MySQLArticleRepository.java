package com.example.rafnews.repositories.article;

import com.example.rafnews.entities.Article;
import com.example.rafnews.entities.Tag;
import com.example.rafnews.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class MySQLArticleRepository extends MySqlAbstractRepository implements ArticleRepository {

    @Override
    public Article addArticle(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        PreparedStatement tagStatement = null;

        try {
            connection = this.newConnection();

            String[] generatedCols = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO articles (title, text, created_at, view_cnt, author, category_id) VALUES(?, ?, ?, ?, ?, ?)", generatedCols);
            preparedStatement.setString(1,  article.getTitle());
            preparedStatement.setString(2, article.getText());
            preparedStatement.setTimestamp(3, new Timestamp(article.getCreatedAt()));
            preparedStatement.setInt(4, article.getViewCnt());
            preparedStatement.setString(5, article.getAuthor());
            preparedStatement.setInt(6, article.getCategoryId());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            tagStatement = connection.prepareStatement("INSERT INTO article_tags (article_id, tag_id) VALUES (?, ?)");

            if(resultSet.next()){
                int artId = resultSet.getInt(1);
                article.setId(artId);

                for(Integer tagId: article.getTagIds()) {
                    tagStatement.setInt(1, artId);
                    tagStatement.setInt(2, tagId);

                    tagStatement.executeUpdate();
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public List<Article> allArticles() {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT a.id, a.title, a.text, a.created_at, a.view_cnt, a.author, a.category_id, t.id AS tag_id, t.name AS tag_name FROM articles a LEFT JOIN article_tags at ON a.id = at.article_id LEFT JOIN tags t ON at.tag_id = t.id");

            Map<Integer, Article> articleMap = new HashMap<>();

            while (resultSet.next()) {
                int articleId = resultSet.getInt("id");
                Article article = articleMap.get(articleId);

                if (article == null) {
                    article = new Article(
                            articleId,
                            resultSet.getString("title"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created_at").getTime(),
                            resultSet.getInt("view_cnt"),
                            resultSet.getString("author"),
                            resultSet.getInt("category_id"),
                            new ArrayList<>()
                    );

                    articleMap.put(articleId, article);
                    articles.add(article);
                }

                int tagId = resultSet.getInt("tag_id");
                String tagName = resultSet.getString("tag_name");

                if (tagId > 0 && tagName != null) {
                    article.getTagIds().add(tagId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public Article findArticle(Integer articleId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Article article = null;

        try {
            connection = this.newConnection();
            String query = "SELECT a.id, a.title, a.text, a.created_at, a.view_cnt, a.author, a.category_id, t.id AS tag_id, t.name AS tag_name " +
                    "FROM articles a " +
                    "LEFT JOIN article_tags at ON a.id = at.article_id " +
                    "LEFT JOIN tags t ON at.tag_id = t.id " +
                    "WHERE a.id = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, articleId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (article == null) {
                    article = new Article(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("text"),
                            resultSet.getTimestamp("created_at").getTime(),
                            resultSet.getInt("view_cnt"),
                            resultSet.getString("author"),
                            resultSet.getInt("category_id"),
                            new ArrayList<>()
                    );
                }

                int tagId = resultSet.getInt("tag_id");
                String tagName = resultSet.getString("tag_name");

                if (tagId > 0 && tagName != null) {
                    article.getTagIds().add(tagId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public void deleteArticle(Integer id) {
        Connection connection = null;
        PreparedStatement statement = null;

        PreparedStatement articleTagStat = null;
        PreparedStatement articleCommentStat = null;
        try {
            connection = this.newConnection();
            String query = "DELETE FROM articles WHERE id = ?";
            String query2 = "DELETE FROM article_tags WHERE article_id = ?";
            String query3 = "DELETE FROM comments WHERE article_id = ?";

            statement = connection.prepareStatement(query);
            articleTagStat = connection.prepareStatement(query2);
            articleCommentStat = connection.prepareStatement(query3);

            statement.setInt(1, id);
            articleTagStat.setInt(1, id);
            articleCommentStat.setInt(1, id);


            articleTagStat.executeUpdate();
            statement.executeUpdate();
            articleCommentStat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<Article> getArticlePage(Integer offset) {
        List<Article> articles = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            int pageSize = 10;
            int pageOffset = (offset - 1) * pageSize;
            resultSet = statement.executeQuery("SELECT * FROM articles ORDER BY created_at DESC LIMIT " + pageOffset + ", " + pageSize);

            while (resultSet.next()) {
                int articleId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String text = resultSet.getString("text");
                Long createdAt = resultSet.getTimestamp("created_at").getTime();
                int viewCount = resultSet.getInt("view_cnt");
                String author = resultSet.getString("author");
                int categoryId = resultSet.getInt("category_id");

                articles.add(new Article(articleId, title, text, createdAt, viewCount, author, categoryId));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public void incrementArticleViewCount(Integer articleId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("UPDATE articles SET view_cnt = view_cnt + 1 WHERE id = ?");
            statement.setInt(1, articleId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<Article> getMostViewedArticles() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) ORDER BY view_cnt DESC LIMIT 10";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getTimestamp("created_at").getTime());
                article.setViewCnt(resultSet.getInt("view_cnt"));
                article.setAuthor(resultSet.getString("author"));
                article.setCategoryId(resultSet.getInt("category_id"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public List<Article> getArticlesByCategory(Integer categoryId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM articles WHERE category_id = ? ORDER BY created_at DESC";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getTimestamp("created_at").getTime());
                article.setViewCnt(resultSet.getInt("view_cnt"));
                article.setAuthor(resultSet.getString("author"));
                article.setCategoryId(resultSet.getInt("category_id"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return articles;
    }

    @Override
    public List<Article> getArticlesByTag(Integer tagId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = this.newConnection();

            String query = "SELECT a.* FROM articles a JOIN article_tags at ON a.id = at.article_id WHERE at.tag_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, tagId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Article article = new Article();
                article.setId(resultSet.getInt("id"));
                article.setTitle(resultSet.getString("title"));
                article.setText(resultSet.getString("text"));
                article.setCreatedAt(resultSet.getTimestamp("created_at").getTime());
                article.setViewCnt(resultSet.getInt("view_cnt"));
                article.setAuthor(resultSet.getString("author"));
                article.setCategoryId(resultSet.getInt("category_id"));
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeResultSet(resultSet);
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        System.out.println("velicina " + articles.size());
        return articles;
    }



}

