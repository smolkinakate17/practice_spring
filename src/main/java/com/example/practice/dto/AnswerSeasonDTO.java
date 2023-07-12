package com.example.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnswerSeasonDTO {
    private boolean success;
    private String message;
    private List<AnswerSeasonDataDTO> data;
}
