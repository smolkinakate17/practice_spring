package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
public class AnswerDynamicDataDTO {
    private Long itemId;
    private String itemTitle;

    private  double currentTotal;
    private  double prevTotal;
    private double change;
    private  double index;

    @JsonCreator
    public AnswerDynamicDataDTO(@JsonProperty("item_id") Long itemId,
                                @JsonProperty("item_title") String itemTitle,
                                @JsonProperty("current_total") double currentTotal,
                                @JsonProperty("prev_total") double prevTotal,
                                @JsonProperty("change") double change,
                                @JsonProperty("index") double index) {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.currentTotal = currentTotal;
        this.prevTotal = prevTotal;
        this.change = change;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerDynamicDataDTO dataDTO)) return false;
        return Objects.equals(getItemId(), dataDTO.getItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemId());
    }
}
