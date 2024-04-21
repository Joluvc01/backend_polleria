package com.api_polleria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String longitude;

    @NotEmpty
    private String latitude;

    private String imageUrl;

    @NotNull
    private Boolean status;

    @NotEmpty
    private String phone;
}
