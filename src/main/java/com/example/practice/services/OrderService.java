package com.example.practice.services;

import com.example.practice.models.Order;
import com.example.practice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
      if(orderRepository.existsById(id)){
          return orderRepository.getReferenceById(id);
      }
      else return null;
    }

    public void saveOrder(Order order){
        orderRepository.save(order);
    }
}
