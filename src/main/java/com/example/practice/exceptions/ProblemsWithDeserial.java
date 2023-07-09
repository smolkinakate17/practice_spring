package com.example.practice.exceptions;


import com.example.practice.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ProblemsWithDeserial extends HttpMessageNotReadableException{

    public ProblemsWithDeserial (String message){
        super(message);

    }
}
