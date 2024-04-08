package com.ecommerce.enoca.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "model_order")
public class ModelOrder extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ModelOrderItem> orderItemList = new ArrayList<>();

    @Column(name = "total_price",nullable = false)
    private double totalPrice;

    @Column(name = "total_quantity")
    private Integer totalQuantity;


    @Column(name = "order_code")
    private String orderCode;

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ModelOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<ModelOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @PrePersist
    public void generateOrderCode() {
        this.orderCode = generateUniqueOrderCode();
    }

    private String generateUniqueOrderCode() {

        long timestamp = System.currentTimeMillis();

        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;

        String uniqueOrderCode = String.valueOf(timestamp) + String.valueOf(randomNumber);

        return uniqueOrderCode;
    }
}
