package com.example.practice.models;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "customer")
public class Customer {


    @Id
    @Column(name="user_id")
    private Long id;


    @Column(name = "user_title")
    private String title;


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade =CascadeType.ALL)
    private List<Order> order;

    public Customer(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getId(), customer.getId()) && Objects.equals(getTitle(), customer.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
