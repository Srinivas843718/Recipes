package com.recipemanagement.controller;

import com.recipemanagement.dto.SubscriptionDto;
import com.recipemanagement.exception.SubscriptionNotFoundException;
import com.recipemanagement.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscription(@PathVariable String id) throws SubscriptionNotFoundException {
        Optional<SubscriptionDto> subscription = subscriptionService.getSubscription(id);
        return ResponseEntity.ok(subscription.orElse(null));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<SubscriptionDto> getUserSubscription(@PathVariable String id) throws SubscriptionNotFoundException {
        SubscriptionDto subscription = subscriptionService.getUserSubscription(id);
        return ResponseEntity.ok(subscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable String id, @RequestBody SubscriptionDto updatedSubscriptionDto) throws SubscriptionNotFoundException {
        Optional<SubscriptionDto> updatedSubscription = subscriptionService.updateSubscription(id, updatedSubscriptionDto);
        return ResponseEntity.ok(updatedSubscription.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id) throws SubscriptionNotFoundException {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        SubscriptionDto newSubscription = subscriptionService.addSubscription(subscriptionDto);
        return ResponseEntity.ok(newSubscription);
    }
}
