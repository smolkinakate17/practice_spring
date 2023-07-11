package com.example.practice.controllers;

import com.example.practice.dto.*;
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

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AnalysisDynamicController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/analysis/dynamics")
    public ResponseEntity<AnswerDynamicDTO> dynamic(@RequestBody AnalysisDynamicDTO dynamicDTO){
        int month= dynamicDTO.getMonth();
        if(month<1 || month>12){
            return new ResponseEntity<>(new AnswerDynamicDTO(false,"Номер месяца должен быть целым числом от 1 (январь) до 12 (декабрь).",null),HttpStatus.BAD_REQUEST);

        }
        int year=dynamicDTO.getYear();
        if(year<2000){
            return new ResponseEntity<>(new AnswerDynamicDTO(false,"Год должен быть целым числом, не менее 2000.",null),HttpStatus.BAD_REQUEST);

        }
        List<Order>orderList=orderService.findByYearAndMonth(year,month);
        if(orderList.isEmpty()){
            return new ResponseEntity<>(new AnswerDynamicDTO(false,"Такого месяца и/или года нет в базе данных",null),HttpStatus.BAD_REQUEST);

        }
        if(dynamicDTO.getItemId()!=null){
            Item item=itemService.getItemById(dynamicDTO.getItemId());
            if(item==null){
                AnswerDynamicDTO dto=new AnswerDynamicDTO(true,"Номенклатура с item_id = "+dynamicDTO.getItemId()+ "не найдена.",null);
                return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
            }
            List<OrderItem> orderItemListCur=orderItemService.findByItemIdAndYearMonth(item.getId(),year,month);
            List<OrderItem> orderItemListPrev=orderItemService.findByItemIdAndYearMonth(item.getId(),year,month-1);
            List<AnswerDynamicDataDTO> data=orderItemService.resultDynamicData(orderItemListCur,orderItemListPrev);
            return new ResponseEntity<>(new AnswerDynamicDTO(true,null,data),HttpStatus.OK);


        }
        else{
            List<OrderItem> orderItemListCur=orderItemService.findByYearMonth(year,month);
            List<OrderItem> orderItemListPrev=orderItemService.findByYearMonth(year,month-1);
            List<AnswerDynamicDataDTO> data=orderItemService.resultDynamicData(orderItemListCur,orderItemListPrev);
            data.sort(Comparator.comparing(AnswerDynamicDataDTO::getIndex).reversed());
            return new ResponseEntity<>(new AnswerDynamicDTO(true,null,data),HttpStatus.OK);
        }

    }
}
