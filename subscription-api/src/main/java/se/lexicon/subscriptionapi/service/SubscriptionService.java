package se.lexicon.subscriptionapi.service;

import se.lexicon.subscriptionapi.dto.request.ChangePlanRequest;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse subscribe(Long customerId, SubscriptionRequest request);
    List<SubscriptionResponse> findByCustomer(Long customerId);
    SubscriptionResponse changePlan(Long customerId, Long subscriptionId, ChangePlanRequest request);
    SubscriptionResponse cancelSubscription(Long customerId, Long subscriptionId);
}