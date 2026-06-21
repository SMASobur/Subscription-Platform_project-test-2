package se.lexicon.subscriptionapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.request.ChangePlanRequest;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.util.List;

@Tag(name = "Subscriptions", description = "Subscription management endpoints.")
@RestController
@RequestMapping("/api/v1/customers/{customerId}/subscriptions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Subscribe to a plan", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> subscribe(
            @PathVariable Long customerId,
            @Valid @RequestBody SubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.subscribe(customerId, request));
    }

    @GetMapping
    @Operation(summary = "Get customer subscriptions", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<SubscriptionResponse>> findByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(subscriptionService.findByCustomer(customerId));
    }

    @PutMapping("/{subscriptionId}")
    @Operation(summary = "Change subscription plan", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> changePlan(
            @PathVariable Long customerId,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody ChangePlanRequest request) {
        return ResponseEntity.ok(subscriptionService.changePlan(customerId, subscriptionId, request));
    }

    @DeleteMapping("/{subscriptionId}")
    @Operation(summary = "Cancel subscription", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(
            @PathVariable Long customerId,
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(customerId, subscriptionId));
    }
}