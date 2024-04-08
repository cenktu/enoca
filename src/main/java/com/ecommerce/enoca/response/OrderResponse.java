package com.ecommerce.enoca.response;

import java.util.List;

public class OrderResponse {

    private List<OrderItemResponse> orderItemResponseList;
    private double totalPrice;
    private int totalAmount;

    private String orderCode;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<OrderItemResponse> getOrderItemResponseList() {
        return orderItemResponseList;
    }

    public void setOrderItemResponseList(List<OrderItemResponse> orderItemResponseList) {
        this.orderItemResponseList = orderItemResponseList;
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
