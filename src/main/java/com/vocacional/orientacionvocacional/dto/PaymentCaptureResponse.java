package com.vocacional.orientacionvocacional.dto;


import lombok.Data;

@Data
public class PaymentCaptureResponse {
    private boolean completed;
    private Integer purchaseId;
    private String newToken;
}
