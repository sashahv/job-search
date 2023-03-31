package com.olekhv.job.search.controller;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.service.PaypalService;
import com.olekhv.job.search.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/purchases")
public class PaypalController {
    private final PaypalService service;
    private final UserService userService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/premium/pay")
    public ResponseEntity<String> purchasePremium() {
        try {
            Payment payment = service.createPayment(
                    100.0,
                    "PLN",
                    "PAYPAL",
                    "ORDER",
                    "Upgrading account to Premium"
                    , "http://localhost:8080/api/v1/purchases" + CANCEL_URL,
                    "http://localhost:8080/api/v1/purchases" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error occurred");
    }

    @GetMapping(value = CANCEL_URL)
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("Payment was canceled");
    }

    @GetMapping(value = SUCCESS_URL)
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId,
                                             @AuthenticationPrincipal UserCredential userCredential) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                userService.upgradeToPremium(userCredential);
                return ResponseEntity.ok("success");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Error occurred");
    }
}
