package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.dto.PurchaseCreateDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseReportDTO;
import com.vocacional.orientacionvocacional.model.entity.Purchase;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO createPurchase(PurchaseCreateDTO purchase);
    List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId);
    List<PurchaseReportDTO> getPurchaseReportByDate();


    List<PurchaseDTO> getAllPurchases();
    PurchaseDTO confirmPurchase(Integer purchaseId);
    PurchaseDTO getPurchaseById(Integer purchaseId);
}
