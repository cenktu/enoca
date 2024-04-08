package com.ecommerce.enoca.controller;

import com.ecommerce.enoca.model.Cart;
import com.ecommerce.enoca.model.CartItem;
import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.model.Product;
import com.ecommerce.enoca.request.CartItemRequest;
import com.ecommerce.enoca.response.CartResponse;
import com.ecommerce.enoca.service.CartService;
import com.ecommerce.enoca.service.CustomerService;
import com.ecommerce.enoca.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;
    private CustomerService customerService;
    private ProductService productService;

    public CartController(CartService cartService, CustomerService customerService,ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService=productService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        if (!optionalCustomer.isPresent()) {

            return ResponseEntity.notFound().build();
        }
        Cart cart = optionalCustomer.get().getCart();
        if (cart == null || cart.getCartItemList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cartService.getCart(customerId,cart));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long customerId, @RequestBody CartItemRequest cartItemRequest) {
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.notFound().build();

        }
        Optional<Product> product = productService.getSingleProduct(cartItemRequest.getProductId());
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        if(cartItemRequest.getQuantity()> product.get().getStockQuantity()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cartService.addToCart(optionalCustomer.get(),cartItemRequest,product.get()));

    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long customerId, @RequestBody CartItemRequest cartItemRequest) {
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.notFound().build();

        }
        Cart cart = optionalCustomer.get().getCart();
        if (cart == null || cart.getCartItemList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
            Long productId = cartItemRequest.getProductId();

            Product product = productService.getSingleProduct(productId).orElse(null);
            if (product == null) {
                return ResponseEntity.badRequest().build();
            }
            CartItem cartItem = cart.getCartItemByProduct(product);
            if (cartItem == null) {
                return ResponseEntity.badRequest().build();
            }

        return ResponseEntity.ok(cartService.updateCart(cartItemRequest,optionalCustomer.get(),cart));

    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> emptyCart(@PathVariable Long customerId){
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        Cart cart= optionalCustomer.get().getCart();
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        else if(cart==null|| cart.getCartItemList().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        cartService.emptyCart(cart);
        return ResponseEntity.ok("Cart emptied successfully !");
    }

    @DeleteMapping("/{customerId}/product/{productId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long customerId,@PathVariable Long productId) {
        Optional<Customer> optionalCustomer= customerService.getSingleCustomer(customerId);
        Cart cart = optionalCustomer.get().getCart();
        Product product = productService.getSingleProduct(productId).orElse(null);
        CartItem cartItem = cart.getCartItemByProduct(product);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        else if(cart==null|| cart.getCartItemList().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        else if(product==null|| cartItem==null){
            return ResponseEntity.badRequest().build();
        }
        cartService.removeProductFromCart(product,cart);
        return ResponseEntity.ok("Product removed from cart !");
    }
}
