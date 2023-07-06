package com.example.practice.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item", referencedColumnName = "id")
    private Item item;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;




}
