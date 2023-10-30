package ru.sovkombank.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность поставщика товаров
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "suppliers", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Product> products;
}
