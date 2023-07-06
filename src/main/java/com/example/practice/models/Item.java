package com.example.practice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item",fetch = FetchType.EAGER)
    private List<OrderItem> orderItem;
}
