package com.ecommerce.enoca.controller;

import com.ecommerce.enoca.model.Cart;
import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.model.ModelOrder;
import com.ecommerce.enoca.response.OrderResponse;
import com.ecommerce.enoca.service.CustomerService;
import com.ecommerce.enoca.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {


    private OrderService orderService;
    private CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForCustomer(@PathVariable Long customerId){
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        if (!optionalCustomer.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<OrderResponse> orderResponse = orderService.getAllOrdersForCustomer(customerId);

        if(orderResponse.size()==0){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/orders/{orderCode}")
    public OrderResponse getOrderForCode(@PathVariable String orderCode) {
        return orderService.getOrderForCode(orderCode);
    }

    @PostMapping("/place/{customerId}")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable Long customerId){
        Optional<Customer> optionalCustomer = customerService.getSingleCustomer(customerId);
        if (!optionalCustomer.isPresent()) {

            return ResponseEntity.notFound().build();
        }
        Cart cart = optionalCustomer.get().getCart();
        if (cart == null || cart.getCartItemList().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orderService.placeOrder(cart,optionalCustomer.get()));
    }
}
