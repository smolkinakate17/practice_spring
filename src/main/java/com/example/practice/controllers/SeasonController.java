package com.example.practice.controllers;

import com.example.practice.dto.AnswerSeasonDTO;
import com.example.practice.dto.AnswerSeasonDataDTO;
import com.example.practice.dto.SeasonDTO;
import com.example.practice.models.Item;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SeasonController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ItemService itemService;
    private final OrderItemService orderItemService;

    @GetMapping("/analysis/seasonal")
    public ResponseEntity<AnswerSeasonDTO> seasonAnalysis(@RequestBody SeasonDTO seasonDTO){
        if(seasonDTO.getItemId()!=null){
            Item item=itemService.getItemById(seasonDTO.getItemId());
            if(item==null){
                return new ResponseEntity<>(new AnswerSeasonDTO(false,"Номенклатура с item_id = "+seasonDTO.getItemId()+" не найдена.",null),HttpStatus.NOT_FOUND);
            }

            AnswerSeasonDataDTO dataDTO=new AnswerSeasonDataDTO();
            dataDTO.setItemId(item.getId());
            dataDTO.setItemTitle(item.getTitle());
            dataDTO.setLevels(orderItemService.salesLevels(item.getId()));
            dataDTO.setCoeff(orderItemService.seasonCoeff(item.getId()));
            return new ResponseEntity<>(new AnswerSeasonDTO(true,null, Arrays.asList(dataDTO)),HttpStatus.OK);

        }
        else{
            List<Item> itemList=itemService.getAllItems();
            List<AnswerSeasonDataDTO> dataDTOList=new ArrayList<>();
            for(Item item: itemList){
                AnswerSeasonDataDTO answer=new AnswerSeasonDataDTO(item.getId(), item.getTitle(), orderItemService.salesLevels(item.getId()),orderItemService.seasonCoeff(item.getId()));
                dataDTOList.add(answer);
            }
            return new ResponseEntity<>(new AnswerSeasonDTO(true,null,dataDTOList),HttpStatus.OK);
        }

    }
}
