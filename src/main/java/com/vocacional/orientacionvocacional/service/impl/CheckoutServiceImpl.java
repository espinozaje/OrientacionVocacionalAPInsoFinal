package com.vocacional.orientacionvocacional.service.impl;


import com.vocacional.orientacionvocacional.dto.PaymentCaptureResponse;
import com.vocacional.orientacionvocacional.dto.PaymentOrderResponse;
import com.vocacional.orientacionvocacional.dto.PurchaseDTO;
import com.vocacional.orientacionvocacional.integration.payment.paypal.dto.OrderCaptureResponse;
import com.vocacional.orientacionvocacional.integration.payment.paypal.dto.OrderResponse;
import com.vocacional.orientacionvocacional.integration.payment.paypal.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService{
    private final PayPalService payPalService;
    private final PurchaseService purchaseService;
    private final UserService userService;

    @Override
    public PaymentOrderResponse createPaymentOrder(Integer purchaseId, String returnUrl, String cancelUrl) {
        OrderResponse orderResponse = payPalService.createOrder(purchaseId, returnUrl, cancelUrl);
        String paypalUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();
        return new PaymentOrderResponse(paypalUrl);
    }

    @Override
    public PaymentCaptureResponse capturePaymentOrder(String orderId) {
        OrderCaptureResponse orderCaptureResponse = payPalService.captureOrder(orderId);
        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");

        PaymentCaptureResponse paypalCaptureResponse = new PaymentCaptureResponse();
        paypalCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId();

            // Confirmar la compra
            PurchaseDTO purchaseDTO = purchaseService.confirmPurchase(Integer.parseInt(purchaseIdStr));
            paypalCaptureResponse.setPurchaseId(purchaseDTO.getId());

            // Actualizar el plan del estudiante basado en el userId del DTO y obtener el nuevo token si es necesario
            String newToken = userService.updateStudentPlanToPremium(purchaseDTO.getUserId());

            // Si el plan se actualizó, devolvemos el nuevo token en la respuesta
            if (newToken != null) {
                paypalCaptureResponse.setNewToken(newToken);  // Asegúrate de tener un campo para el token en la respuesta
            }
        }

        return paypalCaptureResponse;
    }

}
