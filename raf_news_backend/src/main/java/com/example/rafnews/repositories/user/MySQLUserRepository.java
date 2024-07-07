package com.example.rafnews.repositories.user;

import com.example.rafnews.entities.User;
import com.example.rafnews.repositories.MySqlAbstractRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLUserRepository extends MySqlAbstractRepository implements UserRepository {

    public MySQLUserRepository() {
        User user1 = new User( 0, "lav@gmail.com", "lav", "trgovcevic", "tip2", true, "lav"); // lav
        User user2 = new User( 1, "pera@gmail.com", "pera", "peric", "tip2", true, "pera"); // pera

        addUser(user1);
        addUser(user2);
    }

    @Override
    public User addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String[] generatedCols = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO users (email, first_name, last_name, type, status, pass_hash) VALUES(?, ?, ?, ?, ?, ?)", generatedCols);
            preparedStatement.setString(1,  user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getType());
            preparedStatement.setBoolean(5, user.isStatus());
            preparedStatement.setString(6, DigestUtils.sha256Hex(user.getPassHash()));


            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * from users");

            while(resultSet.next()){
                users.add(new User(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("type"),
                                resultSet.getBoolean("status"),
                                resultSet.getString("pass_hash")
                        )
                );
            }

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return users;
    }

    @Override
    public User findUser(Integer id) {
        User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users where id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int userId = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String type = resultSet.getString("type");
                boolean status = resultSet.getBoolean("status");
                String passHash = resultSet.getString("pass_hash");

                user = new User(userId, email, firstName, lastName, type, status, passHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User findUserByEmail(String userMail) {
        User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users where email = ?");
            preparedStatement.setString(1, userMail);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int userId = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String type = resultSet.getString("type");
                boolean status = resultSet.getBoolean("status");
                String passHash = resultSet.getString("pass_hash");

                user = new User(userId, email, firstName, lastName, type, status, passHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("DELETE FROM users where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public List<User> getUserPage(Integer offset) {
        List<User> users = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();
            int pageSize = 10;
            int pageOffset = (offset - 1) * pageSize;
            resultSet = statement.executeQuery("SELECT * FROM users LIMIT " + pageOffset + ", " + pageSize);

            while (resultSet.next()) {
                users.add(new User(
                                resultSet.getInt("id"),
                                resultSet.getString("email"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                resultSet.getString("type"),
                                resultSet.getBoolean("status"),
                                ""
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return users;
    }

    @Override
    public User updateUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE users SET email = ?, first_name = ?, last_name = ?, type = ?, status = ?, pass_hash = ? WHERE id = ?");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getType());
            preparedStatement.setBoolean(5, user.isStatus());
            preparedStatement.setString(6, DigestUtils.sha256Hex(user.getPassHash()));
            preparedStatement.setInt(7, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public boolean updateUserStatus(Integer id, boolean status) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE users SET status = ? WHERE id = ?");
            preparedStatement.setBoolean(1, status);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return true;
    }

}
