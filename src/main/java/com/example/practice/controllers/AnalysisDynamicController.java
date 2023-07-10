package com.example.practice.controllers;

import com.example.practice.dto.AnalysisDynamicDTO;
import com.example.practice.dto.AnswerDynamicDTO;
import com.example.practice.dto.AnswerDynamicDataDTO;
import com.example.practice.dto.MessageDTO;
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

    public List<AnswerDynamicDataDTO> resultData(List<AnswerDynamicDataDTO> big,List<AnswerDynamicDataDTO> little){
        List<AnswerDynamicDataDTO> result=new ArrayList<>();
        for(AnswerDynamicDataDTO bigAnswer : big){
            AnswerDynamicDataDTO littleAnswer=little.stream().filter(answerDynamicDataDTO ->answerDynamicDataDTO.equals(bigAnswer) ).findFirst().orElse(null);
            if(littleAnswer!=null){

                AnswerDynamicDataDTO resultAnswer=new AnswerDynamicDataDTO();
                resultAnswer.setItemId(bigAnswer.getItemId());
                resultAnswer.setItemTitle(bigAnswer.getItemTitle());
                if(bigAnswer.getCurrentTotal()>littleAnswer.getCurrentTotal()){
                    resultAnswer.setCurrentTotal(resultAnswer.getCurrentTotal() + bigAnswer.getCurrentTotal());
                    resultAnswer.setPrevTotal(resultAnswer.getPrevTotal() + littleAnswer.getPrevTotal());
                }
                else{
                    resultAnswer.setCurrentTotal(resultAnswer.getCurrentTotal() + littleAnswer.getCurrentTotal());
                    resultAnswer.setPrevTotal(resultAnswer.getPrevTotal() + bigAnswer.getPrevTotal());
                }
                result.add(resultAnswer);
            }
            else{
                result.add(bigAnswer);
            }

        }
        for(AnswerDynamicDataDTO resultAnswer : result){
            double change= resultAnswer.getCurrentTotal() - resultAnswer.getPrevTotal();
            double index=0;
            if(resultAnswer.getCurrentTotal()>0){
                index=change/ resultAnswer.getCurrentTotal()*100;
            }
            resultAnswer.setChange(change);
            resultAnswer.setIndex(index);
        }
        return result;

    }

    @GetMapping("/analysis/dynamics")
    public ResponseEntity<AnswerDynamicDTO> dynamic(@RequestBody AnalysisDynamicDTO dynamicDTO){
        int month=dynamicDTO.getMonth();
        //проверка условия по месяцу
        if(month>0&&month<13){
            int year=dynamicDTO.getYear();
            //проверка условия по году
            if(year>=2000){
                //есть ли айди товара
                if(dynamicDTO.getItemId()!=null){
                    //есть ли в базе
                    if(itemService.getItemById(dynamicDTO.getItemId())!=null){
                        Item item=itemService.getItemById(dynamicDTO.getItemId());
                        //ищем строки товаров с данным айди товара
                        List<OrderItem> orderItemList=orderItemService.findByItemId(item.getId());
                        //ищем заказы с этим товаром
                        List<Order>orderList=new ArrayList<>();
                        for(OrderItem orderItem:orderItemList){
                            orderList.add(orderItem.getOrder());
                        }
                        //сортируем по дате по убыванию
                        orderList.sort(Comparator.comparing(Order::getCreateDate).reversed());

                        Order orderCheckYearAndMonth=orderList.stream().filter(order -> order.getCreateDate().getYear()==year && order.getCreateDate().getMonthValue()==month).findFirst().orElse(null);
                        if(orderCheckYearAndMonth==null){
                            return new ResponseEntity<>(new AnswerDynamicDTO(false,"Такого года и/или месяца нет в базе данных", null),HttpStatus.NOT_FOUND);
                        }

                        double currentTotal=0;
                        double prevTotal=0;

                        for(Order order: orderList){
                            //соответствует ли текущему месяцу и году
                            if((order.getCreateDate().getMonthValue())==month && (order.getCreateDate().getYear())==year){

                                currentTotal+=( orderItemService.findByItemIdAndOrderId(item.getId(),order.getId()).getPrice()
                                                *
                                                orderItemService.findByItemIdAndOrderId(item.getId(),order.getId()).getQuantity());
                            }
                            //соответствует ли предыдущему месяцу и текущему году
                            else if((order.getCreateDate().getMonthValue())==month-1 && (order.getCreateDate().getYear())==year){

                                prevTotal+=( orderItemService.findByItemIdAndOrderId(item.getId(),order.getId()).getPrice()
                                            *
                                            orderItemService.findByItemIdAndOrderId(item.getId(),order.getId()).getQuantity());
                            }
                            //если месяц меньше предыдущего и год не текущий, прерываем цикл(тк остальные заказы тоже не будут подходить из-за сортировки
                            else if((order.getCreateDate().getMonthValue())<month-1 || (order.getCreateDate().getYear())!=year){
                                break;
                            }
                        }
                        //считаем
                        double change=currentTotal-prevTotal;
                        double index=0;
                        if(currentTotal>0) index=change/currentTotal*100;
                        AnswerDynamicDataDTO dataDTO=new AnswerDynamicDataDTO(item.getId(), item.getTitle(), currentTotal,prevTotal,change,index);
                        List<AnswerDynamicDataDTO> list= Arrays.asList(dataDTO);
                        AnswerDynamicDTO dto=new AnswerDynamicDTO(true,null,list);
                        return new ResponseEntity<>(dto,HttpStatus.OK);


                    }
                    //если не наши товар с таким айди
                    else{
                        AnswerDynamicDTO dto=new AnswerDynamicDTO(true,"Номенклатура с item_id = "+dynamicDTO.getItemId()+ "не найдена.",null);
                        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
                    }
                }
                //если айди товара не передали
                else{
                    List<Order> orderList=orderService.getAllOrders();
                    orderList.sort(Comparator.comparing(Order::getCreateDate).reversed());
                    Order orderCheckYearAndMonth=orderList.stream().filter(order -> order.getCreateDate().getYear()==year && order.getCreateDate().getMonthValue()==month).findFirst().orElse(null);
                    if(orderCheckYearAndMonth==null){
                        return new ResponseEntity<>(new AnswerDynamicDTO(false,"Такого года и/или месяца нет в базе данных", null),HttpStatus.NOT_FOUND);
                    }

                    List<OrderItem> orderItemListCur=new ArrayList<>();
                    List<OrderItem>orderItemListPrev=new ArrayList<>();
                    //из всех заказов выбираем нужные
                    for(Order order : orderList){
                        //если заказ из текущего месяца и года
                        if((order.getCreateDate().getMonthValue())==month && (order.getCreateDate().getYear())==year){
                            orderItemListCur.addAll(orderItemService.findByOrderId(order.getId()));
                        }
                        //если заказ из прошлого месяца и текущего года
                        else if((order.getCreateDate().getMonthValue())==month-1 && (order.getCreateDate().getYear())==year){
                            orderItemListPrev.addAll(orderItemService.findByOrderId(order.getId()));
                        }
                        //если заказ из месяца меньше предыдущего или из меньшего года, то выходим(тк из-за сортировки оставшиеся заказы не подойдут)
                        else if((order.getCreateDate().getMonthValue())<month-1 || (order.getCreateDate().getYear())!=year){
                            break;
                        }
                    }
                    //использую данный класс из-за подходящей структуры(три поля остаются нулевыми, но это просто числа)
                    List<AnswerDynamicDataDTO> answerListCur=new ArrayList<>();
                    List<AnswerDynamicDataDTO> answerListPrev=new ArrayList<>();
                    List<AnswerDynamicDataDTO> answerList=new ArrayList<>();

                    //по строкам заказов из текущего месяца
                    for(OrderItem orderItem : orderItemListCur){
                        if(answerListCur.isEmpty()){
                            answerListCur.add(new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),orderItem.getPrice()*orderItem.getQuantity(),0,0,0));
                        }
                        else{
                            AnswerDynamicDataDTO answer=new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),orderItem.getPrice()*orderItem.getQuantity(),0,0,0);
                            //есть ли answer с таким же айди заказа в листе
                            AnswerDynamicDataDTO oldAnswer = answerListCur.stream().filter(answerDynamicDataDTO ->answerDynamicDataDTO.equals(answer) ).findFirst().orElse(null);
                            //если есть, то обновляем его, приплюсовывая другой currentTotal
                            if(oldAnswer!=null){
                                int index=answerListCur.indexOf(oldAnswer);
                                oldAnswer.setCurrentTotal(oldAnswer.getCurrentTotal()+(orderItem.getPrice()*orderItem.getQuantity()));
                                answerListCur.set(index,oldAnswer);
                            }
                            else{
                                answerListCur.add(new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),orderItem.getPrice()*orderItem.getQuantity(),0,0,0));
                            }
                        }
                    }
                    //по строкам заказов из предыдущего месяца аналогично
                    for(OrderItem orderItem : orderItemListPrev){
                        if(answerListPrev.isEmpty()){
                            answerListPrev.add(new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),0,orderItem.getPrice()*orderItem.getQuantity(),0,0));

                        }
                        else{
                            AnswerDynamicDataDTO answer=new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),0,orderItem.getPrice()*orderItem.getQuantity(),0,0);

                            AnswerDynamicDataDTO oldAnswer = answerListPrev.stream().filter(answerDynamicDataDTO ->answerDynamicDataDTO.equals(answer) ).findFirst().orElse(null);
                            if(oldAnswer!=null){
                                int index=answerListPrev.indexOf(oldAnswer);
                                oldAnswer.setPrevTotal(oldAnswer.getCurrentTotal()+(orderItem.getPrice()*orderItem.getQuantity()));
                                answerListPrev.set(index,oldAnswer);
                            }
                            else{
                                answerListPrev.add(new AnswerDynamicDataDTO(orderItem.getItem().getId(), orderItem.getItem().getTitle(),0,orderItem.getPrice()*orderItem.getQuantity(),0,0));

                            }
                        }
                    }
                    if(answerListCur.size()>answerListPrev.size()){
                        answerList=resultData(answerListCur,answerListPrev);
                    }
                    else{
                        answerList=resultData(answerListPrev,answerListCur);
                    }

                    answerList.sort(Comparator.comparing(AnswerDynamicDataDTO ::getIndex).reversed());
                    AnswerDynamicDTO dto=new AnswerDynamicDTO(true,null,answerList);
                    return new ResponseEntity<>(dto,HttpStatus.OK);


                }

            }
            else{
                AnswerDynamicDTO dto=new AnswerDynamicDTO(true,"Год должен быть целым числом, не менее 2000.",null);
                return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);

            }
        }
        else{
            AnswerDynamicDTO dto=new AnswerDynamicDTO(true,"Номер месяца должен быть целым числом от 1 (январь) до 12 (декабрь).",null);
            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        }



    }
}
