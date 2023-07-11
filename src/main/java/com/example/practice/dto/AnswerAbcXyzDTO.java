package com.example.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerAbcXyzDTO {
    private boolean success;
    private String message;
    private List<AnswerAbcXyzDataDTO> data;
}
