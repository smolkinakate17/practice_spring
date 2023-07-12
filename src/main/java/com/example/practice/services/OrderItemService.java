package com.example.practice.services;

import com.example.practice.dto.AnswerAbcXyzDataDTO;
import com.example.practice.dto.AnswerDynamicDataDTO;
import com.example.practice.models.OrderItem;
import com.example.practice.repositories.OrderItemRepository;
import com.example.practice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
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
    public List<OrderItem> findByYearMonthCustomerId(int year,int month, Long id){
        return orderItemRepository.findAllByCustomerIdAndYearAndMonth(id,year,month);
    }
    public List<OrderItem> findByItemIdAndYearMonth(Long id,int year, int month){
        return orderItemRepository.findAllByItem_IdAndYearAndMonth(id, year, month);
    }
    public List<OrderItem>findByYearMonth(int year,int month){
        return orderItemRepository.findAllByYearAndMonth(year, month);
    }
    private List<AnswerDynamicDataDTO> foreachInOrderItemList(List<OrderItem> orderItemList, boolean current){
        List<AnswerDynamicDataDTO> answerList=new ArrayList<>();
        for(OrderItem i : orderItemList){
            AnswerDynamicDataDTO answer=answerList.stream().filter(answerDynamicDataDTO -> answerDynamicDataDTO.getItemId().equals(i.getItem().getId())).findFirst().orElse(null);
            int index= answerList.indexOf(answer);
            if(answer==null){
                answer=new AnswerDynamicDataDTO();
                answer.setItemId(i.getItem().getId());
                answer.setItemTitle(i.getItem().getTitle());
                if(current){
                    answer.setCurrentTotal(i.getPrice()*i.getQuantity());
                }
                else{
                    answer.setPrevTotal(i.getPrice()*i.getQuantity());
                }
                answerList.add(answer);
            }
            else{
                if(current){
                    answer.setCurrentTotal(i.getPrice()*i.getQuantity()+ answer.getCurrentTotal());
                }
                else{
                    answer.setPrevTotal(i.getPrice()*i.getQuantity() + answer.getPrevTotal());
                }
                answerList.set(index,answer);
            }

        }
        return answerList;
    }
    public List<AnswerDynamicDataDTO> twoListToOneDynamic(List<AnswerDynamicDataDTO> big,List<AnswerDynamicDataDTO> little){
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
    public List<AnswerDynamicDataDTO> resultDynamicData(List<OrderItem> cur,List<OrderItem> prev){
        List<AnswerDynamicDataDTO> answerCur=foreachInOrderItemList(cur,true);
        List<AnswerDynamicDataDTO> answerPrev=foreachInOrderItemList(prev,false);
        List<AnswerDynamicDataDTO> result;
        if(answerPrev.size()> answerCur.size()){
            result=twoListToOneDynamic(answerPrev,answerCur);
        }
        else result=twoListToOneDynamic(answerCur,answerPrev);
        return result;


    }

    public List<AnswerAbcXyzDataDTO> resultData(List<OrderItem> orderItemList,boolean customer){
        List<AnswerAbcXyzDataDTO> answerList=new ArrayList<>();
        for(OrderItem i : orderItemList){
            AnswerAbcXyzDataDTO answer= answerList.stream().filter(answerAbcXyzDataDTO -> answerAbcXyzDataDTO.getItemId().equals(i.getItem().getId())).findFirst().orElse(null);
            int index= answerList.indexOf(answer);
            if(answer==null){
                answer=new AnswerAbcXyzDataDTO(i.getItem().getId(),i.getItem().getTitle(),i.getPrice()*i.getQuantity(),0,null,1,null);
                answerList.add(answer);
            }
            else{
                answer.setOrderCount(answer.getOrderCount()+1);
                answer.setTotal(answer.getTotal()+(i.getPrice()*i.getQuantity()));
                answerList.set(index,answer);
            }

        }
        answerList.sort(Comparator.comparing(AnswerAbcXyzDataDTO::getTotal).reversed());
        double sumAll=0;
        for(AnswerAbcXyzDataDTO answer : answerList){
            sumAll+=answer.getTotal();
        }
        double sumPrev=0;
        for(AnswerAbcXyzDataDTO answer : answerList){

            sumPrev+=answer.getTotal();
            answer.setPercent(sumPrev/sumAll*100);
            if(customer){
                if(answer.getOrderCount()>=10)answer.setXyz("X");
                else if(answer.getOrderCount()>=5) answer.setXyz("Y");
                else answer.setXyz("Z");
            }
            else{
                if(answer.getOrderCount()>=30)answer.setXyz("X");
                else if(answer.getOrderCount()>=10) answer.setXyz("Y");
                else answer.setXyz("Z");
            }
            if(answer.getPercent()<=80) answer.setAbc("A");
            else if(answer.getPercent()>80 && answer.getPercent()<=90) answer.setAbc("B");
            else answer.setAbc("C");

        }
        return answerList;

    }

    public List<Double> salesLevels(Long itemId){

        List<Integer>years=orderRepository.findYearsByItem_id(itemId);
        List<Double> levels=new ArrayList<>();
        for(int m=1;m<13;m++){
            double sum=0;
            for(Integer year:years){
                List<OrderItem> orderItemList=findByItemIdAndYearMonth(itemId, year,m);
                for(OrderItem orderItem:orderItemList){
                    sum+=orderItem.getPrice()*orderItem.getQuantity();
                }
            }
            if(years.isEmpty()){
                levels.add(sum);
            }
            else{
                levels.add(sum/years.size());
            }


        }


        return levels;

    }
   public List<Double> seasonCoeff(Long itemId){
        List<Integer>years=orderRepository.findYearsByItem_id(itemId);
        List<Double>sumYear=new ArrayList<>();
        List<Double> result=new ArrayList<>();
        for (int year : years){
            double sum=0;
            List<OrderItem> orderItemList=orderItemRepository.findByYearAndItem_id(year,itemId);
            for(OrderItem orderItem:orderItemList){
                sum+=orderItem.getPrice()*orderItem.getQuantity();
            }
            sumYear.add(sum);
        }
        for(int m=1;m<13;m++){
            double sum=0;
            for(Integer year:years){
                double sumOneYear=0;
                List<OrderItem> orderItemList=findByItemIdAndYearMonth(itemId, year,m);
                for(OrderItem orderItem:orderItemList){
                    sumOneYear+=orderItem.getPrice()*orderItem.getQuantity();
                }
                int index=years.indexOf(year);
                double yearCoeff=sumYear.get(index);
                sumOneYear=sumOneYear/yearCoeff;
                sum+=sumOneYear;
            }
            if(years.isEmpty()){
                result.add(sum);
            }
            else{
                result.add(sum/ years.size()*100);
            }

        }
        return result;
   }

}
