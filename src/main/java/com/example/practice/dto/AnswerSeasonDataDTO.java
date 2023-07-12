package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSeasonDataDTO {
    @JsonProperty("item_id")
    private Long itemId;
    @JsonProperty("item_title")
    private String itemTitle;
    @JsonProperty("seasonal_levels")
    private List<Double> levels;
    @JsonProperty("seasonal_coeff")
    private List<Double> coeff;
}
