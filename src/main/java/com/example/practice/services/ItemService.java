package com.example.practice.services;

import com.example.practice.models.Customer;
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
        if(itemRepository.existsById(id)){
            return itemRepository.getReferenceById(id);
        }
        else return null;
    }
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public void updateItem(Item newItem){
        Long id = newItem.getId();
        Item oldItem=itemRepository.getReferenceById(id);
        oldItem.setTitle(newItem.getTitle());
        itemRepository.save(oldItem);
    }

}
