package com.ecommerce.enoca.repository;

import com.ecommerce.enoca.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepository extends JpaRepository<CartItem,Long> {


}
