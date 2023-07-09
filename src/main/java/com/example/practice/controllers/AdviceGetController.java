package com.example.practice.controllers;

import com.example.practice.dto.MessageDTO;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = GetOrderController.class)
public class AdviceGetController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request){
        MessageDTO messageDTO=new MessageDTO();
        messageDTO.setSuccess(false);
        messageDTO.setMessage("Неверный формат данных");
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
        messageDTO.setMessage("Неизвестная ошибка при чтении заказа.");
        return new ResponseEntity<>(messageDTO,statusCode);
    }
}
