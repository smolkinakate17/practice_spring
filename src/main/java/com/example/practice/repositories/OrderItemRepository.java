package com.example.practice.repositories;

import com.example.practice.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findAllByOrder_Id(Long id);
    List<OrderItem> findAllByItem_Id(Long id);
    OrderItem findAllByItem_IdAndOrder_Id(Long itemId,Long orderId);
    @Query("select o from OrderItem o join Item i on o.item.id=?1 join Order myorder on o.order.id=myorder.id where year (myorder.createDate)=?2 and month (myorder.createDate)=?3")
    List<OrderItem> findAllByItem_IdAndYearAndMonth(Long id,int year,int month);

    @Query("select i from OrderItem i join Order o on i.order.id=o.id join Customer c on o.customer.id=?1 where year (o.createDate)=?2 and month (o.createDate)=?3")
    List<OrderItem> findAllByCustomerIdAndYearAndMonth(Long id,int year,int month);

    @Query("select i from OrderItem i join Order o on i.order.id=o.id where year (o.createDate)=?1 and month (o.createDate)=?2")
    List<OrderItem> findAllByYearAndMonth(int year,int month);
}
