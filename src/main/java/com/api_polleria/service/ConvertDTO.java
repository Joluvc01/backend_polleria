package com.api_polleria.service;

import com.api_polleria.dto.*;
import com.api_polleria.entity.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConvertDTO {

    public ProductDTO convertToProductDTO(Product product){
        Set<String> galleryImages = new HashSet<>(new ArrayList<>(product.getGalleryImages()));
        List<String> categoryList = product.getCategoryList()
                .stream()
                .map(Category::getName)
                .toList();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCardImage(),
                product.getDetailImage(),
                galleryImages,
                product.getStatus(),
                product.getValoration(),
                categoryList
        );
    }

    public ProductStoreStockDTO convertToProductStoreStockDTO(ProductStoreStock storeStock){
        Long product = (storeStock.getProduct() != null) ? storeStock.getProduct().getId() : null;
        Long store = (storeStock.getStore() != null) ? storeStock.getStore().getId() : null;

        return new ProductStoreStockDTO(
                storeStock.getId(),
                product,
                store,
                storeStock.getQuantity()
        );
    }

    public CustomerDTO convertToCustomerDTO(Customer customer){
        List<Long> addressList = customer.getAddressList()
                .stream()
                .map(Address::getId)
                .toList();
        List<Long> favoriteProducts = customer.getFavoriteProducts()
                .stream()
                .map(Product::getId)
                .toList();

        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getBirthdate(),
                customer.getStatus(),
                addressList,
                favoriteProducts
        );
    }

    public ValorationDTO convertToValorationDTO(Valoration valoration){
        Long customer = (valoration.getCustomer() != null) ? valoration.getCustomer().getId() : null;
        Long product = (valoration.getProduct() != null) ? valoration.getProduct().getId() : null;
        return new ValorationDTO(
                valoration.getId(),
                valoration.getValoration(),
                valoration.getReview(),
                customer,
                product
        );
    }

    public Purchase_detailDTO convertToPurchase_detailDTO(Purchase_detail purchase_detail){
        Long product = (purchase_detail.getProduct() != null) ? purchase_detail.getProduct().getId() : null;
        return new Purchase_detailDTO(
                product,
                purchase_detail.getQuantity(),
                purchase_detail.getTotal()
        );
    }

    public PurchaseDTO convertToPurchaseDTO(Purchase purchase){
        Long customer = (purchase.getCustomer() != null) ? purchase.getCustomer().getId() : null;
        List<Purchase_detailDTO> details = purchase.getDetails()
                .stream()
                .map(this::convertToPurchase_detailDTO)
                .toList();

        return new PurchaseDTO(
                purchase.getId(),
                purchase.getDate(),
                details,
                customer,
                purchase.getIgv(),
                purchase.getSubtotal(),
                purchase.getTotal()
        );
    }

}
