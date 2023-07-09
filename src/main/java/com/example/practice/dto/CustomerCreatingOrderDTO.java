package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CustomerCreatingOrderDTO {
    private final Long customerId;

    private final String customerTitle;


    @JsonCreator
    public CustomerCreatingOrderDTO(@JsonProperty("id") Long customerId,
                                    @JsonProperty("title") String customerTitle){
        this.customerId = customerId;
        this.customerTitle = customerTitle;
    }
}
