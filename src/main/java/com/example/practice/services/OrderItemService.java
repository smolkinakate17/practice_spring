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

    public List<OrderItem> findByOrderId(Long id){
        return orderItemRepository.findAllByOrder_Id(id);
    }

    public List<OrderItem> findByItemId(Long id){
        return orderItemRepository.findAllByItem_Id(id);
    }
    public OrderItem findByItemIdAndOrderId(Long itemId,Long orderId){
        return orderItemRepository.findAllByItem_IdAndOrder_Id(itemId, orderId);
    }

}
