package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.*;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

@Mapper(componentModel = "spring", uses = {PlanMapper.class})
public interface SubscriptionMapper {

    @Mapping(target = "customerId", source = "customer.id")
    SubscriptionResponse toResponse(Subscription entity);
}