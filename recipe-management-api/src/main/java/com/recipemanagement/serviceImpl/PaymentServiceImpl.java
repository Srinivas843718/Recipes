package com.recipemanagement.serviceImpl;

import com.recipemanagement.dto.PaymentsDto;
import com.recipemanagement.entity.PaymentsEntity;
import com.recipemanagement.exception.PaymentsNotFoundException;
import com.recipemanagement.repository.PaymentRepository;
import com.recipemanagement.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PaymentsDto> getPayments() {
        List<PaymentsEntity> payments = paymentRepository.findAll();
        return payments.stream().map(paymentEntity -> modelMapper.map(paymentEntity, PaymentsDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentsDto> getPayment(String id) throws PaymentsNotFoundException {
        Optional<PaymentsEntity> paymentEntity = paymentRepository.findById(id);
        if (paymentEntity.isEmpty()) {
            throw new PaymentsNotFoundException("Payment not found");
        } else {
            PaymentsDto paymentDto = modelMapper.map(paymentEntity.get(), PaymentsDto.class);
            return Optional.of(paymentDto);
        }
    }

    @Override
    public PaymentsDto addPayment(PaymentsDto paymentDto) {
        PaymentsEntity paymentEntity = modelMapper.map(paymentDto, PaymentsEntity.class);
        PaymentsEntity savedPaymentsEntity = paymentRepository.save(paymentEntity);
        return modelMapper.map(savedPaymentsEntity, PaymentsDto.class);
    }

    @Override
    public Optional<PaymentsDto> updatePayment(String id, PaymentsDto updatedPaymentsDto) throws PaymentsNotFoundException {
        Optional<PaymentsEntity> existingPaymentsEntity = paymentRepository.findById(id);
        if (existingPaymentsEntity.isEmpty()) throw new PaymentsNotFoundException("Payment not found");

        PaymentsEntity updatedPaymentsEntity = modelMapper.map(updatedPaymentsDto, PaymentsEntity.class);
        updatedPaymentsEntity.setPaymentId(id);
        paymentRepository.save(updatedPaymentsEntity);

        return Optional.of(modelMapper.map(updatedPaymentsEntity, PaymentsDto.class));
    }

    @Override
    public void deletePayment(String id) throws PaymentsNotFoundException {
        Optional<PaymentsEntity> existingPaymentsEntity = paymentRepository.findById(id);
        if (existingPaymentsEntity.isEmpty()) throw new PaymentsNotFoundException("Payment not found");

        paymentRepository.deleteById(id);
    }
}
