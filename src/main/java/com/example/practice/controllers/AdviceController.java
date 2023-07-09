package com.example.practice.controllers;

import com.example.practice.dto.MessageDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = CreateOrderController.class)
class AdviceController extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){
        MessageDTO messageDTO=new MessageDTO();
        messageDTO.setSuccess(false);
        messageDTO.setMessage("Неверный формат данных.");
        return new ResponseEntity<>(messageDTO,status);

    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request)
    {
        MessageDTO messageDTO=new MessageDTO();
        messageDTO.setSuccess(false);
        messageDTO.setMessage("Неизвестная ошибка при записи заказа.");
        return new ResponseEntity<>(messageDTO,statusCode);
    }


}
