package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GetOrderDTO {
    private final Long orderId;

    @JsonCreator
    public GetOrderDTO(@JsonProperty("id")Long orderId){
        this.orderId=orderId;
    }
}
