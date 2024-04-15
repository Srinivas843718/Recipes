package com.recipemanagement.controller;

import com.recipemanagement.dto.PaymentsDto;
import com.recipemanagement.exception.PaymentsNotFoundException;
import com.recipemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentsDto>> getPayments() {
        List<PaymentsDto> payments = paymentService.getPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentsDto> getPayment(@PathVariable String id) throws PaymentsNotFoundException {
        Optional<PaymentsDto> payment = paymentService.getPayment(id);
        return ResponseEntity.ok(payment.orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentsDto> updatePayment(@PathVariable String id, @RequestBody PaymentsDto updatedPaymentsDto) throws PaymentsNotFoundException {
        Optional<PaymentsDto> updatedPayment = paymentService.updatePayment(id, updatedPaymentsDto);
        return ResponseEntity.ok(updatedPayment.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) throws PaymentsNotFoundException {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<PaymentsDto> addPayment(@RequestBody PaymentsDto paymentDto) {
        PaymentsDto newPayment = paymentService.addPayment(paymentDto);
        return ResponseEntity.ok(newPayment);
    }
}

