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
@Table(name = "item")
public class Item {

    @Id
    @Column(name="item_id")
    private Long id;

    @Column(name = "item_title")
    private String title;

    @OneToMany(mappedBy = "item",fetch = FetchType.LAZY)
    private List<OrderItem> orderItem;

    public Item(Long id,String title){
        this.id=id;
        this.title=title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(getId(), item.getId()) && Objects.equals(getTitle(), item.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle());
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
