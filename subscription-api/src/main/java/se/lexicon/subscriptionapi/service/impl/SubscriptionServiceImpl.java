package se.lexicon.subscriptionapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.request.ChangePlanRequest;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.DuplicateSubscriptionException;
import se.lexicon.subscriptionapi.exception.InactivePlanException;
import se.lexicon.subscriptionapi.exception.PlanChangeNotAllowedException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.SubscriptionMapper;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(Long customerId, SubscriptionRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.planId()));

        //Business rule (B R): Only active plans are subscribable
        if (!plan.isActive()) {
            throw new InactivePlanException("Cannot subscribe to an inactive plan: " + plan.getName());
        }

        // B R: one active subscription per service type
        boolean hasActiveSub = subscriptionRepository
                .findByCustomerIdAndPlan_ServiceTypeAndStatus(customerId, plan.getServiceType(), SubscriptionStatus.ACTIVE)
                .isPresent();

        if (hasActiveSub) {
            throw new DuplicateSubscriptionException("Customer already has an active subscription for service type: " + plan.getServiceType());
        }

        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> findByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        return subscriptionRepository.findByCustomerId(customerId).stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SubscriptionResponse changePlan(Long customerId, Long subscriptionId, ChangePlanRequest request) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + subscriptionId));

        // Security check to ensure the subscription belongs to the customer
        if (!subscription.getCustomer().getId().equals(customerId)) {
            throw new BusinessRuleException("Subscription does not belong to customer: " + customerId);
        }

        //B R: Cannot change a cancelled subscription
        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot change plan for a cancelled subscription.");
        }

        Plan newPlan = planRepository.findById(request.newPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + request.newPlanId()));

        // B R: Plan changes are allowed only within the same operator and same service type
        Plan currentPlan = subscription.getPlan();
        if (!currentPlan.getOperator().getId().equals(newPlan.getOperator().getId()) ||
                currentPlan.getServiceType() != newPlan.getServiceType()) {
            throw new PlanChangeNotAllowedException("Plan change allowed only within the same operator and service type.");
        }

        if (!newPlan.isActive()) {
            throw new InactivePlanException("Cannot change to an inactive plan: " + newPlan.getName());
        }

        subscription.setPlan(newPlan);
        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional
    public SubscriptionResponse cancelSubscription(Long customerId, Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + subscriptionId));

        if (!subscription.getCustomer().getId().equals(customerId)) {
            throw new BusinessRuleException("Subscription does not belong to customer: " + customerId);
        }

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException("Subscription is already cancelled.");
        }

        // B R: Cancelled subscriptions must store a cancellation date
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(LocalDateTime.now());

        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }
}