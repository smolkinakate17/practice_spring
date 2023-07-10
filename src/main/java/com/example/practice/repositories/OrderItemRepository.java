package com.example.practice.repositories;

import com.example.practice.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findAllByOrder_Id(Long id);
    List<OrderItem> findAllByItem_Id(Long id);
    OrderItem findAllByItem_IdAndOrder_Id(Long itemId,Long orderId);
}
