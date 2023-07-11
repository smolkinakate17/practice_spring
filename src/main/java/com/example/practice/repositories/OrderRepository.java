package com.example.practice.repositories;

import com.example.practice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order>findAllByCustomer_Id(Long id);

    @Query("select o from Order o where year (o.createDate)=?1 and month (o.createDate)=?2")
    List<Order>findAllByCreateDate_YearAndCreateDate_MonthValue(int year,int month);

    @Query("select o from Order o where year (o.createDate)=?1 and month (o.createDate)=?2 and o.customer.id=?3")
    List<Order>findAllByCreateDate_YearAndCreateDate_MonthValueAAndAndCustomer_Id(int year,int month,Long id);
}
