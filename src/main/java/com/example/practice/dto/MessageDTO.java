package com.example.practice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private  boolean success;
    private   String message;
    private CreatingOrderDTO data;

}
