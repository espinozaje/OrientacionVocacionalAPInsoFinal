package com.vocacional.orientacionvocacional.dto;


import lombok.Data;

@Data
public class PurchaseItemCreateDTO {
    private Integer planId;
    private Integer quantity;
    private Float price;
}
