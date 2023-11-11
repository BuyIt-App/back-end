package com.buyit.productservice.Service;

import com.buyit.productservice.Exception.InsufficientQuantityException;
import com.buyit.productservice.Exception.ProductNotFoundException;
import com.buyit.productservice.dto.ProductRes;
import com.buyit.productservice.model.Category;
import com.buyit.productservice.model.Product;
import com.buyit.productservice.repository.ProductCategoryRepo;
import com.buyit.productservice.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private ProductRepo productRepository;

    private ProductCategoryRepo productCategoryRepo;

    public List<ProductRes> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        // Map the Product entities to ProductRes DTOs
        List<ProductRes> productResList = productList.stream()
                .map(this::mapToProductRes)
                .collect(Collectors.toList());

        return productResList;
    }

    private ProductRes mapToProductRes(Product product) {
        ProductRes productRes = new ProductRes();
        productRes.setProductId(product.getProductId());
        productRes.setProductName(product.getProductName());
        productRes.setDescription(product.getDescription());
        productRes.setPrice(product.getPrice());

        Category p =  productCategoryRepo.findById(product.getCategoryId()).orElse(null);
        productRes.setCategory(p.getCategoryName());

        if (product.getQuantity() == 0) {
            productRes.setQuantity("Out of stock");
        } else {
            productRes.setQuantity(String.valueOf(product.getQuantity()));
        }

        return productRes;
    }


    public Product createProduct(Product Product) {
        return productRepository.save(Product);
    }

    public Product updateProduct(Product Product) {
        return productRepository.save(Product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductRes getById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));;
            return mapToProductRes(product);

    }

    public void updateProductQuantity(long productId, long quantity) {
        Product p = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        long currentQuantity = p.getQuantity();

        if (currentQuantity - quantity >= 0) {
            p.setQuantity(currentQuantity - quantity);
            productRepository.save(p);
        } else {
            throw new InsufficientQuantityException("Insufficient quantity in stock.");
        }
    }

}
