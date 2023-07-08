package com.example.practice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
public class CreatingOrderDTO {

    private final Long orderId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createDate;

    private final CustomerCreatingOrderDTO customer;

    private final List<OrderItemCreatingOrderDTO> orderItem;

    @JsonCreator
    public CreatingOrderDTO(@JsonProperty("id") Long orderId,
                            @JsonProperty("created") LocalDateTime createDate,
                            @JsonProperty("customer") CustomerCreatingOrderDTO customer,
                            @JsonProperty("items") List<OrderItemCreatingOrderDTO> orderItem) {
        this.orderId = orderId;
        this.createDate = createDate;
        this.customer = customer;
        this.orderItem = orderItem;

    }
}
