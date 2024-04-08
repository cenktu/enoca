package com.ecommerce.enoca.response;

import com.ecommerce.enoca.request.CartItemRequest;

import java.util.List;

public class CartResponse {

    private List<CartItemResponse> cartItems;
    private double totalPrice;
    private int totalAmount;

    public List<CartItemResponse> getCartItems() {

        return cartItems;
    }

    public void setCartItems(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
