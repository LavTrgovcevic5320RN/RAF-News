package com.example.rafnews.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.rafnews.entities.User;
import com.example.rafnews.repositories.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {

    public UserService() {
        System.out.println(this);
    }

    @Inject
    private UserRepository userRepository;

    public User addUser(User user) {
        return this.userRepository.addUser(user);
    }

    public List<User> allUsers() {
        return this.userRepository.allUsers();
    }

    public User findUser(Integer id) {
        return this.userRepository.findUser(id);
    }

    public void deleteUser(Integer id) {
        this.userRepository.deleteUser(id);
    }

    public String login(String email, String password) {
        String hashed = DigestUtils.sha256Hex(password);
        System.out.println("USAO");
        User user = this.userRepository.findUserByEmail(email);
        System.out.println(user.isStatus());
        if(user == null || !user.getPassHash().equals(hashed)){
            return null;
        }else if(!user.isStatus()){
            return "Inactive";
        }

        Date issuedAt = new Date();

        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withSubject(email)
                .withClaim("role", user.getType())
                .withClaim("name", user.getFirstName() + " " + user.getLastName())
                .sign(algorithm);
    }

    public boolean isAuthorized(String token, String path) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();
        String type = jwt.getClaim("role").asString();
        System.out.println(type);
        System.out.println(path);
        if(path.contains("user") && type.equals("Creator")){
            return false;
        }
        System.out.println("hey");
        if(email == null) {
            return false;
        }

        return true;
    }

    public List<User> getUserPage(Integer offset) {
        return this.userRepository.getUserPage(offset);
    }
    public User updateUser(User user) {
        return this.userRepository.updateUser(user);
    }

    public boolean updateUserStatus(Integer id, boolean status) {
        return this.userRepository.updateUserStatus(id, status);
    }
}
