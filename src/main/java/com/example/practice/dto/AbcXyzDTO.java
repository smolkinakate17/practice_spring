package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AbcXyzDTO {
    private int month;
    private int year;
    @JsonProperty("customer_id")
    private Long customerId;


}
