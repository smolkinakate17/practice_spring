package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public class SeasonDTO {
    @JsonProperty("item_id")
    private Long itemId;

    @JsonCreator
    public SeasonDTO( @JsonProperty("item_id")Long itemId){
        this.itemId=itemId;
    }
}
