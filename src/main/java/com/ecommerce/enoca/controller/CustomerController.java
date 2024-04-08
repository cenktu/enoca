package com.ecommerce.enoca.controller;

import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Optional<Customer>> getSingleCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(customerService.getSingleCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer newCustomer){
        return ResponseEntity.ok(customerService.addCustomer(newCustomer));
    }

}
