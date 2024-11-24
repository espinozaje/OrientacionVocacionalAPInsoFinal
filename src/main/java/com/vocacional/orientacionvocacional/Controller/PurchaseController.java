package com.vocacional.orientacionvocacional.Controller;

import com.vocacional.orientacionvocacional.dto.PurchaseCreateDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseDTO;
import com.vocacional.orientacionvocacional.dto.PurchaseReportDTO;

import com.vocacional.orientacionvocacional.service.impl.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseDTO>> listAllPurchases(){
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseCreateDTO purchase) {
        PurchaseDTO newPurchase = purchaseService.createPurchase(purchase);
        return new ResponseEntity<>(newPurchase, HttpStatus.CREATED);
    }

     @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseDTO>> getPurchaseHistoryByUserId(@PathVariable Integer userId) {
        List<PurchaseDTO> purchases = purchaseService.getPurchaseHistoryByUserId(userId);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/report")
    public ResponseEntity<List<PurchaseReportDTO>> getPurchaseReport(){
        List<PurchaseReportDTO> reports = purchaseService.getPurchaseReportByDate();
        return ResponseEntity.ok(reports);
    }
}
