package com.recipemanagement.service;

import com.recipemanagement.dto.PaymentsDto;
import com.recipemanagement.exception.PaymentsNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    List<PaymentsDto> getPayments();

    Optional<PaymentsDto> getPayment(String id) throws PaymentsNotFoundException;

    PaymentsDto addPayment(PaymentsDto paymentDto);

    Optional<PaymentsDto> updatePayment(String id, PaymentsDto updatedPaymentsDto) throws PaymentsNotFoundException;

    void deletePayment(String id) throws PaymentsNotFoundException;
}
