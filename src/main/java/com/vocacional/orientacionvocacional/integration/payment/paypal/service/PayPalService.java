package com.vocacional.orientacionvocacional.integration.payment.paypal.service;

import com.vocacional.orientacionvocacional.exception.ResourceNotFoundException;
import com.vocacional.orientacionvocacional.integration.payment.paypal.dto.*;
import com.vocacional.orientacionvocacional.model.entity.Purchase;
import com.vocacional.orientacionvocacional.repository.PurchaseRepository;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.Collections;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PayPalService {
    @Value("${paypal.client-id}")
    private String clientId;
    @Value("${paypal.client-secret}")
    private String clientSecret;
    @Value("${paypal.api-base}")
    private String apiBase;

    @NonNull
    private PurchaseRepository purchaseRepository;

    private RestClient paypalClient;

    @PostConstruct
    public void init(){
        paypalClient = RestClient
                .builder()
                .baseUrl(apiBase)
                .build();
    }

    public String getAccessToken(){
        MultiValueMap<String, String> body= new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");


        return Objects.requireNonNull(paypalClient.post()
                .uri("/v1/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, "Basic "+ Base64.getEncoder()
                        .encodeToString((clientId + ":" + clientSecret).getBytes()))
                .body(body)
                .retrieve()
                .toEntity(TokenResponse.class).getBody())
                .getAccessToken();
    }

    public OrderResponse createOrder(Integer purchaseId, String returnUrl, String cancelUrl){
        Purchase purchase =  purchaseRepository.findById(purchaseId)
                .orElseThrow(ResourceNotFoundException::new);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIntent("CAPTURE");
        Amount amount = new Amount();
        amount.setCurrencyCode("USD");
        amount.setValue(purchase.getTotal().toString());

        PurchaseUnit purchaseUnit = new PurchaseUnit();
        purchaseUnit.setReferenceId(purchase.getId().toString());
        purchaseUnit.setAmount(amount);
        orderRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));

        ApplicationContext applicationContext = ApplicationContext.builder()
                .brandName("Orientacion Vocacional")
                .returnURL(returnUrl)
                .cancelURL(cancelUrl)
                .build();

        orderRequest.setApplicationContext(applicationContext);
        return paypalClient.post()
                .uri("/v2/checkout/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ getAccessToken())
                .body(orderRequest)
                .retrieve()
                .toEntity(OrderResponse.class)
                .getBody();
    }

    public OrderCaptureResponse captureOrder(String orderId){
        return paypalClient.post()
                .uri("/v2/checkout/orders/{orderId}/capture", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ getAccessToken())
                .retrieve()
                .toEntity(OrderCaptureResponse.class)
                .getBody();
    }
}
