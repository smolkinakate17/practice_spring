package com.example.practice.controllers;

import com.example.practice.dto.*;
import com.example.practice.models.Customer;
import com.example.practice.models.Item;
import com.example.practice.models.Order;
import com.example.practice.models.OrderItem;
import com.example.practice.services.CustomerService;
import com.example.practice.services.ItemService;
import com.example.practice.services.OrderItemService;
import com.example.practice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetOrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/order")
    public ResponseEntity<MessageDTO> getOrder(@RequestBody GetOrderDTO getOrderDTO){

        Order order=new Order();
        order.setId(getOrderDTO.getOrderId());

        if(orderService.getOrderById(order.getId())!=null){
            order=orderService.getOrderById(order.getId());
            Customer customer=new Customer();
            customer=customerService.getCustomerById(order.getCustomer().getId());
            List<OrderItem> orderItemList=orderItemService.findByOrderId(order.getId());
            List<Item> itemList=new ArrayList<>();
            List<OrderItemCreatingOrderDTO> orderItemDtoList=new ArrayList<>();
            for(OrderItem O : orderItemList){
                itemList.add(O.getItem());
                OrderItemCreatingOrderDTO dto=new OrderItemCreatingOrderDTO(O.getItem().getId(),O.getItem().getTitle(),O.getPrice(),O.getQuantity());
                orderItemDtoList.add(dto);
            }
            CustomerCreatingOrderDTO customerCreatingOrderDTO=new CustomerCreatingOrderDTO(customer.getId(), customer.getTitle());
            CreatingOrderDTO creatingOrderDTO=new CreatingOrderDTO(order.getId(), order.getCreateDate(),customerCreatingOrderDTO,orderItemDtoList);
            MessageDTO messageDTO=new MessageDTO();
            messageDTO.setSuccess(true);
            messageDTO.setMessage(null);
            messageDTO.setData(creatingOrderDTO);
            return new ResponseEntity<>(messageDTO, HttpStatus.OK);



        }
        else{
            MessageDTO messageDTO=new MessageDTO();
            messageDTO.setSuccess(false);
            messageDTO.setMessage("Заказ с id = "+getOrderDTO.getOrderId()+" не найден.");
            return new ResponseEntity<>(messageDTO,HttpStatus.NOT_FOUND);
        }

    }
}
