package com.example.practice.controllers;

import com.example.practice.dto.AbcXyzDTO;
import com.example.practice.dto.AnswerAbcXyzDTO;
import com.example.practice.dto.AnswerAbcXyzDataDTO;
import com.example.practice.dto.MessageDTO;
import com.example.practice.models.Customer;
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
public class AbcXyzController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/analysis/abcxyz")
    public ResponseEntity<AnswerAbcXyzDTO> abcXyz(@RequestBody AbcXyzDTO abcXyzDTO){
        int month= abcXyzDTO.getMonth();
        if(month<1 || month>12){
            return new ResponseEntity<>(new AnswerAbcXyzDTO(false,"Номер месяца должен быть целым числом от 1 (январь) до 12 (декабрь).",null),HttpStatus.BAD_REQUEST);

        }
        int year=abcXyzDTO.getYear();
        if(year<2000){
            return new ResponseEntity<>(new AnswerAbcXyzDTO(false,"Год должен быть целым числом, не менее 2000.",null),HttpStatus.BAD_REQUEST);

        }
        List<Order>orderList=orderService.findByYearAndMonth(year,month);
        if(orderList.isEmpty()){
            return new ResponseEntity<>(new AnswerAbcXyzDTO(false,"Такого месяца и/или года нет в базе данных",null),HttpStatus.BAD_REQUEST);

        }
        if(abcXyzDTO.getCustomerId()!=null){
            Customer customer=customerService.getCustomerById(abcXyzDTO.getCustomerId());
            if(customer==null){
                return new ResponseEntity<>(new AnswerAbcXyzDTO(false,"Заказчик с customer_id = "+abcXyzDTO.getCustomerId()+ " не найден.",null),HttpStatus.BAD_REQUEST);

            }


            List<OrderItem>orderItemList=orderItemService.findByYearMonthCustomerId(year,month, customer.getId());
            List<AnswerAbcXyzDataDTO> result=orderItemService.resultData(orderItemList,true);
            return new ResponseEntity<>(new AnswerAbcXyzDTO(true,null,result),HttpStatus.OK);

        }
        else {
            List<OrderItem> orderItemList=orderItemService.findByYearMonth(year,month);
            List<AnswerAbcXyzDataDTO> result=orderItemService.resultData(orderItemList,false);
            return new ResponseEntity<>(new AnswerAbcXyzDTO(true,null,result),HttpStatus.OK);
        }


    }

}
