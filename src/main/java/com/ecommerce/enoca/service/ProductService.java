package com.ecommerce.enoca.service;

import com.ecommerce.enoca.model.Product;
import com.ecommerce.enoca.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService (ProductRepository productRepository){
        this.productRepository=productRepository;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GetProduct method
    public Optional<Product> getSingleProduct(Long productId) {
        return productRepository.findById(productId);
    }

    // CreateProduct method
    public Product createProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    // UpdateProduct method
    public Product updateProduct(Long productId, Product updateProduct) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product foundProduct = optionalProduct.get();
            foundProduct.setName(updateProduct.getName());
            foundProduct.setPrice(updateProduct.getPrice());
            foundProduct.setStockQuantity(updateProduct.getStockQuantity());
            productRepository.save(foundProduct);
            return foundProduct;
        }
        return null;
    }

    // DeleteProduct method
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
