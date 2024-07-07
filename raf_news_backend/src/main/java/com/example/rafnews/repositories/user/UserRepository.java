package com.example.rafnews.repositories.user;

import com.example.rafnews.entities.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user);
    List<User> allUsers();
    User findUser(Integer id);
    User findUserByEmail(String userMail);
    void deleteUser(Integer id);
    List<User> getUserPage(Integer offset);
    User updateUser(User user);

    boolean updateUserStatus(Integer id, boolean status);
}
