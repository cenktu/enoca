package com.ecommerce.enoca.repository;

import com.ecommerce.enoca.model.Customer;
import com.ecommerce.enoca.model.ModelOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ModelOrder,Long> {
    ModelOrder findByOrderCode (String orderCode);
}
