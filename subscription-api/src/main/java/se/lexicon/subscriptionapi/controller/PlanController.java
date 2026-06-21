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
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@Tag(name = "Plans", description = "Plan management endpoints.")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(summary = "Create plan", description = "Requires JWT.\n\nRoles: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> create(@Valid @RequestBody PlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update plan", description = "Requires JWT.\n\nRoles: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanResponse> update(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(planService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete plan", description = "Requires JWT.\n\nRoles: ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active plans", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PlanResponse>> findAllActive() {
        return ResponseEntity.ok(planService.findAllActive());
    }

    @GetMapping("/active/type")
    @Operation(summary = "Get active plans by service type", description = "Requires JWT.\n\nRoles: USER, ADMIN")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PlanResponse>> findByServiceType(@RequestParam ServiceType type) {
        return ResponseEntity.ok(planService.findByServiceType(type));
    }
}