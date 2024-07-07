package com.example.rafnews;

import com.example.rafnews.repositories.article.ArticleRepository;
import com.example.rafnews.repositories.article.MySQLArticleRepository;
import com.example.rafnews.repositories.category.CategoryRepository;
import com.example.rafnews.repositories.category.MySQLCategoryRepository;
import com.example.rafnews.repositories.comment.CommentRepository;
import com.example.rafnews.repositories.comment.MySQLCommentRepository;
import com.example.rafnews.repositories.tag.MySQLTagRepository;
import com.example.rafnews.repositories.tag.TagRepository;
import com.example.rafnews.repositories.user.MySQLUserRepository;
import com.example.rafnews.repositories.user.UserRepository;
import com.example.rafnews.services.*;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication()  {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(MySQLCategoryRepository.class).to(CategoryRepository.class).in(Singleton.class);
                this.bind(MySQLUserRepository.class).to(UserRepository.class).in(Singleton.class);
                this.bind(MySQLTagRepository.class).to(TagRepository.class).in(Singleton.class);
                this.bind(MySQLArticleRepository.class).to(ArticleRepository.class).in(Singleton.class);
                this.bind(MySQLCommentRepository.class).to(CommentRepository.class).in(Singleton.class);

                this.bindAsContract(CategoryService.class);
                this.bindAsContract(UserService.class);
                this.bindAsContract(TagService.class);
                this.bindAsContract(ArticleService.class);
                this.bindAsContract(CommentService.class);
            }
        };

        register(binder);

        packages("com.example.rafnews");
    }
}