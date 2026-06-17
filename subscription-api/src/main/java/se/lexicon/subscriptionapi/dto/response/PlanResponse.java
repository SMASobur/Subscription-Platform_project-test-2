package se.lexicon.subscriptionapi.dto.response;

import se.lexicon.subscriptionapi.domain.constant.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanResponse(
        Long id,
        String name,
        BigDecimal price,
        ServiceType serviceType,
        Integer dataLimitMb,
        boolean active,
        Long operatorId,
        String operatorName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
