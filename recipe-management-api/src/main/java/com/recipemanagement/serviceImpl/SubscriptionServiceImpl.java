package com.recipemanagement.serviceImpl;

import com.recipemanagement.dto.SubscriptionDto;
import com.recipemanagement.entity.PaymentsEntity;
import com.recipemanagement.entity.SubscriptionEntity;
import com.recipemanagement.exception.BaseException;
import com.recipemanagement.exception.SubscriptionNotFoundException;
import com.recipemanagement.repository.PaymentRepository;
import com.recipemanagement.repository.SubscriptionRepository;
import com.recipemanagement.service.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SubscriptionDto> getSubscriptions() {
        List<SubscriptionEntity> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(subscriptionEntity -> modelMapper.map(subscriptionEntity, SubscriptionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubscriptionDto> getSubscription(String id) throws SubscriptionNotFoundException {
        Optional<SubscriptionEntity> subscriptionEntity = subscriptionRepository.findById(id);
        if (subscriptionEntity.isEmpty()) {
            throw new SubscriptionNotFoundException("Subscription not found");
        } else {
            return Optional.of(modelMapper.map(subscriptionEntity.get(), SubscriptionDto.class));
        }
    }

    @Override
    public SubscriptionDto addSubscription(SubscriptionDto subscriptionDto) {
        SubscriptionDto userSubscription = getUserSubscription(subscriptionDto.getUserId());
        if (userSubscription != null) throw new BaseException("User already has active subscription");

        SubscriptionEntity subscriptionEntity = modelMapper.map(subscriptionDto, SubscriptionEntity.class);
        PaymentsEntity paymentsEntity = modelMapper.map(subscriptionDto.getPaymentDetails(), PaymentsEntity.class);

        SubscriptionEntity savedSubscriptionEntity = subscriptionRepository.save(subscriptionEntity);

        paymentsEntity.setSubscriptionId(savedSubscriptionEntity.getSubscriptionId());
        paymentRepository.save(paymentsEntity);

        return modelMapper.map(savedSubscriptionEntity, SubscriptionDto.class);
    }

    @Override
    public Optional<SubscriptionDto> updateSubscription(String id, SubscriptionDto updatedSubscriptionDto) throws SubscriptionNotFoundException {
        Optional<SubscriptionEntity> existingSubscriptionEntity = subscriptionRepository.findById(id);
        if (existingSubscriptionEntity.isEmpty()) throw new SubscriptionNotFoundException("Subscription not found");

        SubscriptionEntity updatedSubscriptionEntity = modelMapper.map(updatedSubscriptionDto, SubscriptionEntity.class);
        updatedSubscriptionEntity.setSubscriptionId(id);
        subscriptionRepository.save(updatedSubscriptionEntity);

        return Optional.of(modelMapper.map(updatedSubscriptionEntity, SubscriptionDto.class));
    }

    @Override
    public void deleteSubscription(String id) throws SubscriptionNotFoundException {
        Optional<SubscriptionEntity> existingSubscriptionEntity = subscriptionRepository.findById(id);
        if (existingSubscriptionEntity.isEmpty()) throw new SubscriptionNotFoundException("Subscription not found");

        subscriptionRepository.deleteById(id);
    }

    @Override
    public SubscriptionDto getUserSubscription(String id) {
        Optional<SubscriptionEntity> subscriptionEntity = subscriptionRepository.findByUserId(id);

        return subscriptionEntity.map(entity -> modelMapper.map(entity, SubscriptionDto.class)).orElse(null);
    }

}
