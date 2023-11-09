package com.buyit.productservice.Service;

import com.buyit.productservice.model.Category;
import com.buyit.productservice.repository.ProductCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final ProductCategoryRepo productCategoryRepo;

    public CategoryService(ProductCategoryRepo productCategoryRepo) {
        this.productCategoryRepo = productCategoryRepo;
    }

    public Category addCategory(Category category) {
        return productCategoryRepo.save(category);
    }

    public void deleteCategory(Long categoryId) {
        productCategoryRepo.deleteById(categoryId);
    }

    public Category updateCategory(Category category) {
        // Ensure the category exists before updating
        if (productCategoryRepo.existsById(category.getCategoryId())) {
            return productCategoryRepo.save(category);
        }
        return null;
    }

    public Category getCategory(Long categoryId) {
        return productCategoryRepo.findById(categoryId).orElse(null);
    }

    public List<Category> getAllCategories() {
        return productCategoryRepo.findAll();
    }

}
