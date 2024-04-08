package com.ecommerce.enoca.service;

import com.ecommerce.enoca.model.Cart;
import com.ecommerce.enoca.model.CartItem;
import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.model.Product;
import com.ecommerce.enoca.repository.CartItemRepository;
import com.ecommerce.enoca.repository.CartRepository;
import com.ecommerce.enoca.repository.ProductRepository;
import com.ecommerce.enoca.request.CartItemRequest;
import com.ecommerce.enoca.response.CartItemResponse;
import com.ecommerce.enoca.response.CartResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }


    // GetCard method
    public CartResponse getCart(Long customerId,Cart cart) {
        double totalPrice = 0;
        int totalAmount = 0;

        for (CartItem cartItem : cart.getCartItemList()) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
            totalAmount += cartItem.getQuantity();
        }
        return getCartResponse(cart, totalPrice, totalAmount);
    }

    // UpdateCart method
    public CartResponse updateCart(CartItemRequest cartItemRequest, Customer customer, Cart cart) {
            Long productId = cartItemRequest.getProductId();
            int quantity = cartItemRequest.getQuantity();

            Product product = productRepository.findById(productId).orElse(null);

            CartItem cartItem = cart.getCartItemByProduct(product);
            // If quantity is less than 1, remove the item from the cart
            if (quantity < 1) {
                cart.getCartItemList().remove(cartItem);
                cartItemRepository.delete(cartItem);
            }
            // Update the quantity if it doesn't exceed the stock quantity
            else if (quantity <= product.getStockQuantity()) {
                cartItem.setQuantity(quantity);
            } // If quantity exceeds stock quantity, return empty response
            else {
                return getCartResponse(cart,0,0);
            }

        double totalPrice = 0;
        int totalQuantity = 0;
        for (CartItem item : cart.getCartItemList()) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
            totalQuantity += item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);

        cartRepository.save(cart);
        return getCartResponse(cart, totalPrice, totalQuantity);
    }

    // EmptyCard method
    public void emptyCart(Cart cart) {
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItemList());
        for (CartItem cartItem : cartItems) {
            cartItem.setCart(null);
            cart.getCartItemList().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }
        cart.setTotalPrice(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);
    }


    // AddProductToCart method
    public CartResponse addToCart(Customer customer, CartItemRequest cartItemRequest,Product product) {
        Cart cart = customer.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
        }
        CartItem existingCartItem = cart.getCartItemByProduct(product);
        if (existingCartItem != null) {
            int totalRequestedQuantity =existingCartItem.getQuantity()+cartItemRequest.getQuantity();
            // If the requested quantity exceed product stock quantity, return empty response
            if(totalRequestedQuantity> product.getStockQuantity()){
                 return getCartResponse(cart,0,0);
            }
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setPrice(product.getPrice());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setCart(cart);
            cart.getCartItemList().add(cartItem);
        }
        double totalPrice = 0;
        int totalAmount = 0;

        for (CartItem item : cart.getCartItemList()) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
            totalAmount += item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalAmount);
        cart.setCustomer(customer);
        cartRepository.save(cart);
        return getCartResponse(cart, totalPrice, totalAmount);
    }

    // getCardResponse method to display order items inside an order
    private CartResponse getCartResponse(Cart cart, double totalPrice, int totalAmount) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setTotalPrice(totalPrice);
        cartResponse.setTotalAmount(totalAmount);
        List<CartItemResponse> cartItemResponses = cart.getCartItemList().stream()
                .map(cartItem -> {
                    CartItemResponse cartItemResponse = new CartItemResponse();
                    cartItemResponse.setProductId(cartItem.getProduct().getId());
                    cartItemResponse.setProductName(cartItem.getProduct().getName());
                    cartItemResponse.setPrice(cartItem.getProduct().getPrice());
                    cartItemResponse.setQuantity(cartItem.getQuantity());
                    return cartItemResponse;
                })
                .collect(Collectors.toList());
        cartResponse.setCartItems(cartItemResponses);
        return cartResponse;
    }

    // RemoveProductFromCart method
    public void removeProductFromCart(Product product, Cart cart) {
        CartItem cartItem = cart.getCartItemByProduct(product);
        if(cartItem!=null){
            cart.getCartItemList().remove(cartItem);
            cartItemRepository.delete(cartItem);
            double totalPrice =0;
            int totalQuantity=0;
            for(CartItem item: cart.getCartItemList()){
                totalPrice +=item.getProduct().getPrice()* item.getQuantity();
                totalQuantity += item.getQuantity();
            }
            cart.setTotalPrice(totalPrice);
            cart.setTotalQuantity(totalQuantity);
            cartRepository.save(cart);
        }
    }
}
