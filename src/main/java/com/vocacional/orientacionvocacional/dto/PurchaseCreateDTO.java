package com.vocacional.orientacionvocacional.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseCreateDTO {
    private Float total;
    private Integer userId;
    private List<PurchaseItemCreateDTO> items;
}
