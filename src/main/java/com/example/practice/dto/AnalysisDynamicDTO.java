package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AnalysisDynamicDTO {
    private int month;
    private int year;
    private Long ItemId;

    @JsonCreator
    public AnalysisDynamicDTO(@JsonProperty("month") int month,
                              @JsonProperty("year") int year,
                              @JsonProperty("item_id") Long ItemId){
        this.month=month;
        this.year=year;
        this.ItemId=ItemId;
    }

}
