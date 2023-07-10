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
        Customer customer = new Customer(creatingOrderDTO.getCustomer().getCustomerId(), creatingOrderDTO.getCustomer().getCustomerTitle());
        //order from dto and order->customer
        Order order = new Order(creatingOrderDTO.getOrderId(), creatingOrderDTO.getCreateDate(), customer);
        //list<orderItem> from dto
        if (orderService.getOrderById(order.getId()) != null) {
            MessageDTO messageDTO = new MessageDTO();

            messageDTO.setSuccess(false);
            messageDTO.setMessage("Заказ с id = " + order.getId() + " уже существует.");
            return new ResponseEntity<>(messageDTO, HttpStatus.BAD_REQUEST);
        }
        else {
            List<OrderItemCreatingOrderDTO> orderItemListDTO = creatingOrderDTO.getOrderItem();
            List<OrderItem> orderItemList = new ArrayList<>();
            List<Item> itemList = new ArrayList<>();
            for (OrderItemCreatingOrderDTO orderItemI : orderItemListDTO) {
                Item item = new Item(orderItemI.getItemId(), orderItemI.getItemTitle());
                //orderItem->item and orderItem->order
                OrderItem orderItem = new OrderItem(item, orderItemI.getPrice(), orderItemI.getQuantity(), order);
                orderItemList.add(orderItem);
                itemList.add(item);
            }


            //if customer from dto in db
            if (customerService.getCustomerById(customer.getId()) != null) {
                String title = customer.getTitle();
                customer = customerService.getCustomerById(customer.getId());
                customer.setTitle(title);

            }
            //if item from dto in db
            for (Item I : itemList) {
                if (itemService.getItemById(I.getId()) != null) {
                    String title = I.getTitle();
                    I = itemService.getItemById(I.getId());
                    I.setTitle(title);


                }

            }


                //customerService.saveCustomer(customer);

                for (Item I : itemList) {
                    itemService.saveItem(I);
                }

                customerService.saveCustomer(customer);

                orderService.saveOrder(order);

                for (OrderItem I : orderItemList) {
                    orderItemService.saveOrderItem(I);
                }


                //orderService.saveOrder(order)

            //я не знаю как но заказчик и заказ тоже сохраняются. Возможно из-за связей между ними

            MessageDTO messageDTO = new MessageDTO();

            messageDTO.setSuccess(true);
            messageDTO.setMessage(null);


            return new ResponseEntity<>(messageDTO, HttpStatus.OK);
        }

    }


}
