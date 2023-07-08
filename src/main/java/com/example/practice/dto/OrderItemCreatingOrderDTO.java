package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OrderItemCreatingOrderDTO {
    private final Long itemId;

    private final  String itemTitle;

    private final double price;

    private final double quantity;

    @JsonCreator
    public OrderItemCreatingOrderDTO(@JsonProperty("id") Long itemId,
                                     @JsonProperty("title") String itemTitle,
                                     @JsonProperty("price") double price,
                                     @JsonProperty("quantity") double quantity) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.price = price;
        this.quantity = quantity;
    }
}
