package com.ecommerce.enoca.controller;

import com.ecommerce.enoca.model.Product;
import com.ecommerce.enoca.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Optional<Product>> getSingleProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getSingleProduct(productId));
    }
    @PostMapping
    public ResponseEntity<Product> createProduct (@RequestBody Product newProduct){
        return ResponseEntity.ok(productService.createProduct(newProduct));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,@RequestBody Product updateProduct){
        Optional<Product> optionalProduct= productService.getSingleProduct(productId);
        if(optionalProduct.isPresent()){
            return ResponseEntity.ok(productService.updateProduct(productId,updateProduct));
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        Optional<Product> optionalProduct = productService.getSingleProduct(productId);
        if(optionalProduct.isPresent()){
            productService.deleteProduct(productId);
            return ResponseEntity.ok("Product deleted successfully !");
        }
        return ResponseEntity.notFound().build();
    }
}
