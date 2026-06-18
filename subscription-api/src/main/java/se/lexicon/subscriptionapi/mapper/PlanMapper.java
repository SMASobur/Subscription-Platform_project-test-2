package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.*;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Plan toEntity(PlanRequest request);

    @Mapping(target = "operatorId", source = "operator.id")
    @Mapping(target = "operatorName", source = "operator.name")
    PlanResponse toResponse(Plan entity);
}