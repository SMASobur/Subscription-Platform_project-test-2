package se.lexicon.subscriptionapi.dto.response;

import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;

import java.time.LocalDateTime;


public record SubscriptionResponse(
        Long id,
        SubscriptionStatus status,
        LocalDateTime cancelledAt,
        Long customerId,
        PlanResponse plan,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

