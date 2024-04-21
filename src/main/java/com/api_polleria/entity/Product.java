package com.api_polleria.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Double price;

    private String cardImage;

    private String detailImage;

    @ElementCollection
    @CollectionTable(name = "product_gallery", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private Set<String> galleryImages;

    @NotNull
    private Boolean status;

    private Double valoration;

    @ManyToMany
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Valoration> valorationList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductStoreStock> storeStocks;

}
