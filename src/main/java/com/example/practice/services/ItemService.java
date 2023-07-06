package com.example.practice.services;

import com.example.practice.models.Item;
import com.example.practice.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }
    public Item getItemById(Long id){
        return itemRepository.findById(id).orElse(null);
    }
    public void saveItem(Item item){
        itemRepository.save(item);
    }

}
