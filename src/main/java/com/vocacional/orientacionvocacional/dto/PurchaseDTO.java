package com.vocacional.orientacionvocacional.dto;

import com.vocacional.orientacionvocacional.model.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseDTO {
    private Integer id;
    private Float total;
    private LocalDateTime createAt;
    private PaymentStatus paymentStatus;
    private Integer userId;
    private List<PurchaseItemDTO> items;
}
