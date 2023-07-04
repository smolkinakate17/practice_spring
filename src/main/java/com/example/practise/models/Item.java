package com.example.practise.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
    private OrderItem orderItem;
}