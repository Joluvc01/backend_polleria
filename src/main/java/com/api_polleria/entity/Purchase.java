package com.api_polleria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate date;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase_detail> details;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    private Double subtotal;

    private Double igv;

    private Double total;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

}
