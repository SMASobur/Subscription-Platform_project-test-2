package se.lexicon.subscriptionapi.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangePlanRequest(
        @NotNull Long newPlanId
) {}