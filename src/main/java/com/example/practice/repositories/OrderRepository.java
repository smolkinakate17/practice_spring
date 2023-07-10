package com.example.practice.repositories;

import com.example.practice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    //List<Order>findAllByCreateDate_YearAndCreateDate_MonthValue(int year,int month);
}
