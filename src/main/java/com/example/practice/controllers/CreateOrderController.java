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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class CreateOrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;



    @PostMapping("/order")
    public ResponseEntity<MessageDTO> addOrder(@RequestBody CreatingOrderDTO creatingOrderDTO) {
        //customer from dto
        Customer customer=new Customer(creatingOrderDTO.getCustomer().getCustomerId(),creatingOrderDTO.getCustomer().getCustomerTitle());
        //order from dto and order->customer
        Order order=new Order(creatingOrderDTO.getOrderId(),creatingOrderDTO.getCreateDate(),customer);
        // customer->order
        customer.setOrder(Arrays.asList(order));
        //list<orderItem> from dto
        List<OrderItemCreatingOrderDTO> orderItemList = creatingOrderDTO.getOrderItem();
        List<OrderItem> orderItemList1=new ArrayList<>();
        List<Item> itemList=new ArrayList<>();
        for(OrderItemCreatingOrderDTO orderItemI : orderItemList ){
            Item item=new Item(orderItemI.getItemId(),orderItemI.getItemTitle());
            //orderItem->item and orderItem->order
            OrderItem orderItem=new OrderItem(item,orderItemI.getPrice(),orderItemI.getQuantity(),order);
            //item->orderItem
            item.setOrderItem(Arrays.asList(orderItem));
            orderItemList1.add(orderItem);
            itemList.add(item);
        }
        //order->orderItem
        order.setItems(orderItemList1);



        //if customer from dto in db
        if(customerService.getCustomerById(customer.getId())!=null){
            String title=customer.getTitle();
            customer=customerService.getCustomerById(customer.getId());
            customer.setTitle(title);
            //if customer from db dont have order
            if(customer.getOrder().isEmpty()){
                customer.setOrder(Arrays.asList(order));
            }
            else{
                customer.getOrder().add(order);
            }
            //update customer's order and title
        }

        for(Item I : itemList){
            if(itemService.getItemById(I.getId())!=null){
                String title=I.getTitle();
                List<OrderItem> iOrderItem=I.getOrderItem();
                I=itemService.getItemById(I.getId());
                I.setTitle(title);


            }

        }

        if(orderService.getOrderById(order.getId())!=null){
            MessageDTO messageDTO=new MessageDTO();

            messageDTO.setSuccess(false);
            messageDTO.setMessage("Заказ с id = "+ order.getId()+ " уже существует.");
            return new ResponseEntity<>(messageDTO,HttpStatus.OK);
        }
        else{

            //customerService.saveCustomer(customer);

            for(Item I : itemList){
                itemService.saveItem(I);
            }

            //customerService.saveCustomer(customer);

            //orderService.saveOrder(order);

            for(OrderItem I  : orderItemList1){
                orderItemService.saveOrderItem(I);
            }


            //orderService.saveOrder(order);
        }

        //я не знаю как но заказчик и заказ тоже сохраняются. Возможно из-за связей между ними

        MessageDTO messageDTO=new MessageDTO();

        messageDTO.setSuccess(true);
        messageDTO.setMessage(null);



       return new ResponseEntity<>(messageDTO,HttpStatus.OK);
    }




}
