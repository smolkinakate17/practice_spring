package com.example.practice.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;


    @Column(name="price")
    private double price;


    @Column(name="quantity")
    private double quantity;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName = "order_id")
    private Order order;

    public OrderItem(Item item, double price, double quantity, Order order) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return Double.compare(orderItem.getPrice(), getPrice()) == 0 && Double.compare(orderItem.getQuantity(), getQuantity()) == 0 && Objects.equals(getId(), orderItem.getId()) && Objects.equals(getItem(), orderItem.getItem()) && Objects.equals(getOrder(), orderItem.getOrder());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getItem(), getPrice(), getQuantity(), getOrder());
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", item=" + item +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
