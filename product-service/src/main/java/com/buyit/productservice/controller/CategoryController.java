package com.buyit.productservice.controller;

import com.buyit.productservice.Service.CategoryService;
import com.buyit.productservice.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category addedCategory = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCategory);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(category);
        if(updatedCategory != null){
            return ResponseEntity.ok(updatedCategory);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category Not found");
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategory(categoryId);
            return ResponseEntity.ok(category);
        }catch (Exception e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

}
