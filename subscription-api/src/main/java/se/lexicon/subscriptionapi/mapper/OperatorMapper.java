package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.dto.request.OperatorRequest;
import se.lexicon.subscriptionapi.dto.response.OperatorResponse;

@Mapper(componentModel = "spring")
public interface OperatorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plans", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Operator toEntity(OperatorRequest request);

    OperatorResponse toResponse(Operator operator);
}
