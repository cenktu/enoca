package com.ecommerce.enoca.service;

import com.ecommerce.enoca.model.Cart;
import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.repository.CartRepository;
import com.ecommerce.enoca.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private CartRepository cartRepository;

    public CustomerService(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getSingleCustomer(Long customerId) {
        return customerRepository.findById(customerId);
    }

    // AddCustomer method
    public Customer addCustomer(Customer newCustomer) {
        Cart newCart= new Cart();
        newCart.setCustomer(newCustomer);
        cartRepository.save(newCart);
        return customerRepository.save(newCustomer);
    }
}
