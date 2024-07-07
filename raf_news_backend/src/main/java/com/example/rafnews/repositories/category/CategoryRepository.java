package com.example.rafnews.repositories.category;

import com.example.rafnews.entities.Category;

import java.util.List;

public interface CategoryRepository {

    Category addCategory(Category category);
    List<Category> allCategories();
    List<Category> getCategoryPage(Integer offset);
    Category findCategory(Integer id);
    boolean deleteCategory(Integer id);
    Category updateCategory(Category category);
}

