package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.dto.PaymentCaptureResponse;
import com.vocacional.orientacionvocacional.dto.PaymentOrderResponse;

public interface CheckoutService {

    PaymentOrderResponse createPaymentOrder(Integer purchaseId, String returnUrl, String cancelUrl);

    PaymentCaptureResponse capturePaymentOrder(String orderId);
}
