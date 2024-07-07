package com.example.rafnews.services;

import com.example.rafnews.entities.Category;
import com.example.rafnews.repositories.category.CategoryRepository;
import javax.inject.Inject;

import java.util.List;

public class CategoryService {

    public CategoryService() {
        System.out.println(this);
    }

    @Inject
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category) {
        return this.categoryRepository.addCategory(category);
    }

    public List<Category> allCategories() {
        return this.categoryRepository.allCategories();
    }

    public Category findCategory(Integer id) {
        return this.categoryRepository.findCategory(id);
    }

    public boolean deleteCategory(Integer id) {
        return this.categoryRepository.deleteCategory(id);
    }

    public List<Category> getCategoryPage(Integer offset) {
        return this.categoryRepository.getCategoryPage(offset);
    }

    public Category updateCategory(Category category) {
        return this.categoryRepository.updateCategory(category);
    }
}
