package com.example.rafnews.repositories.category;

import com.example.rafnews.entities.Category;
import com.example.rafnews.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLCategoryRepository extends MySqlAbstractRepository implements CategoryRepository {

    @Override
    public Category addCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedCols = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO categories (name, description) VALUES(?, ?)", generatedCols);
            preparedStatement.setString(1,  category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                category.setId(resultSet.getInt(1));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return category;
    }

    @Override
    public List<Category> allCategories() {
        List<Category> categories = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from categories");

            while(resultSet.next()){
                categories.add(new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"))
                );
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return categories;
    }

    @Override
    public Category findCategory(Integer id) {
        Category category = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM categories where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int categoryId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String desc = resultSet.getString("description");

                category = new Category(categoryId, name, desc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return category;
    }

    @Override
    public boolean deleteCategory(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean deleted = false;

        try {
            connection = this.newConnection();

            // Check if any articles are associated with the category
            boolean hasArticles = checkCategoryArticles(connection, id);

            if(!hasArticles) {
                preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE id = ?");
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Category deleted successfully!");
                    deleted = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return deleted;
    }

    private boolean checkCategoryArticles(Connection connection, Integer categoryId) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean hasArticles = false;

        try {
            statement = connection.prepareStatement("SELECT COUNT(*) FROM articles WHERE category_id = ?");
            statement.setInt(1, categoryId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                hasArticles = count > 0;
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally {
            this.closeResultSet(resultSet);
            this.closeStatement(statement);
        }
        System.out.println(hasArticles);
        return hasArticles;
    }

    @Override
    public List<Category> getCategoryPage(Integer offset) {
        List<Category> categories = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            int pageSize = 10;
            int pageOffset = (offset - 1) * pageSize;
            resultSet = statement.executeQuery("SELECT * FROM categories LIMIT " + pageOffset + ", " + pageSize);

            while(resultSet.next()){
                categories.add(new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"))
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return categories;
    }

    @Override
    public Category updateCategory(Category category) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = this.newConnection();
            String query = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getId());
            statement.executeUpdate();
            System.out.println("Category updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            this.closeStatement(statement);
            this.closeConnection(connection);
        }

        return category;
    }
}
