package com.ecommerce.enoca.service;

import com.ecommerce.enoca.model.*;
import com.ecommerce.enoca.repository.OrderItemRepository;
import com.ecommerce.enoca.repository.OrderRepository;
import com.ecommerce.enoca.repository.ProductRepository;
import com.ecommerce.enoca.response.OrderItemResponse;
import com.ecommerce.enoca.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private CartService cartService;
    private CustomerService customerService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.customerService = customerService;
    }


    // GetAllOrdersForCustomer method that returns list of orders for customer
    public List<OrderResponse> getAllOrdersForCustomer(Long customerId){
        List<OrderResponse> orderResponses = new ArrayList<>();

        Customer customer = customerService.getSingleCustomer(customerId).orElse(null);
        if (customer == null) {
            return orderResponses;
        }

        List<ModelOrder> orderList = customer.getOrders();
        if (orderList == null || orderList.isEmpty()) {
            return orderResponses;
        }

        for (ModelOrder order : orderList) {
            OrderResponse orderResponse = new OrderResponse();
            List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
            double totalPrice = 0;
            int totalQuantity = 0;

            List<ModelOrderItem> orderItemList = order.getOrderItemList();
            for (ModelOrderItem orderItem : orderItemList) {
                OrderItemResponse orderItemResponse = new OrderItemResponse();
                Product product = orderItem.getProduct();
                double isUpdatedPrice = product.getPrice();
                orderItemResponse.setCurrentPrice(isUpdatedPrice);
                orderItemResponse.setProductName(orderItem.getProduct().getName());
                orderItemResponse.setBuyPrice(orderItem.getPrice());
                orderItemResponse.setQuantity(orderItem.getQuantity());
                orderItemResponseList.add(orderItemResponse);

                totalPrice += orderItem.getPrice() * orderItem.getQuantity();
                totalQuantity += orderItem.getQuantity();
            }

            orderResponse.setTotalPrice(totalPrice);
            orderResponse.setTotalAmount(totalQuantity);
            orderResponse.setOrderCode(order.getOrderCode());
            orderResponse.setOrderItemResponseList(orderItemResponseList);

            orderResponses.add(orderResponse);
        }

        return orderResponses;

    }

    // PlaceOrder method to order items inside customer's cart
    public OrderResponse placeOrder(Cart cart, Customer customer) {
        OrderResponse orderResponse= new OrderResponse();

        ModelOrder order = new ModelOrder();
        order.setCustomer(customer);
        order.setTotalPrice(cart.getTotalPrice());
        order.setTotalQuantity(cart.getTotalQuantity());
        order.generateOrderCode();
        orderResponse.setTotalAmount(cart.getTotalQuantity());
        orderResponse.setTotalPrice(cart.getTotalPrice());

        double totalPrice=0;
        int totalQuantity=0;

        List<ModelOrderItem> orderItemList = new ArrayList<>();
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        for(CartItem cartItem: cart.getCartItemList()){
            ModelOrderItem orderItem = new ModelOrderItem();
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItemList.add(orderItem);

            totalPrice += cartItem.getPrice();
            totalQuantity += cartItem.getQuantity();

            Product product = cartItem.getProduct();
            int orderedAmount= cartItem.getQuantity();
            int currentStock= product.getStockQuantity();
            int updatedStock= (currentStock- orderedAmount);
            product.setStockQuantity(updatedStock);
            productRepository.save( product);
            orderItemResponse.setProductName(cartItem.getProduct().getName());
            orderItemResponse.setQuantity(cartItem.getQuantity());
            orderItemResponse.setCurrentPrice(cartItem.getPrice());
            orderItemResponseList.add(orderItemResponse);
        }
        order.setOrderItemList(orderItemList);
        orderResponse.setOrderItemResponseList(orderItemResponseList);
        orderResponse.setOrderCode(order.getOrderCode());
        orderRepository.save(order);
        cartService.emptyCart(cart);
        return orderResponse;
    }

    // GetOrderForCode method that returns the specific order with the given order code
    public OrderResponse getOrderForCode(String orderCode) {
        ModelOrder order = orderRepository.findByOrderCode(orderCode);
        if (order == null) {
            return new OrderResponse();
        }


        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setTotalAmount(order.getTotalQuantity());
        orderResponse.setOrderCode(order.getOrderCode());

        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        for (ModelOrderItem orderItem : order.getOrderItemList()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setCurrentPrice(orderItem.getProduct().getPrice());
            orderItemResponse.setProductName(orderItem.getProduct().getName());
            orderItemResponse.setBuyPrice(orderItem.getPrice());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponseList.add(orderItemResponse);
        }
        orderResponse.setOrderItemResponseList(orderItemResponseList);

        return orderResponse;
    }
}
