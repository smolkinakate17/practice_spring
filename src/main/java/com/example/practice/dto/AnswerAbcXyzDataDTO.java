package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class AnswerAbcXyzDataDTO {
    @JsonProperty("item_id")
    private Long itemId;
    @JsonProperty("item_title")
    private String itemTitle;
    private double total;
    private double percent;
    private String abc;
    @JsonProperty("orders_count")
    private int orderCount;
    private String xyz;
}
