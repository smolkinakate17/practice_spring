package com.example.practice.services;

import com.example.practice.models.OrderItem;
import com.example.practice.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    public List<OrderItem> getAllOrderItems(){
        return orderItemRepository.findAll();
    }
    public OrderItem getOrderItemById(Long id){
        return orderItemRepository.findById(id).orElse(null);
    }
    public void saveOrderItem(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

}
