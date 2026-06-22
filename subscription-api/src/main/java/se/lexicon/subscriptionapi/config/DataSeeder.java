package se.lexicon.subscriptionapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.lexicon.subscriptionapi.domain.constant.Role;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final OperatorRepository operatorRepository;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
        seedRegularUser();
        seedRegularUser01();
        seedOperatorsAndPlans();
    }

    private void seedAdminUser() {
        String adminEmail = "admin@example.com";
        if (!customerRepository.existsByEmail(adminEmail)) {
            Customer admin = new Customer();
            admin.setEmail(adminEmail);
            admin.setFirstName("Admin");
            admin.setLastName("Adminson");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
            customerRepository.save(admin);
            System.out.println("[DATA_SEED] Admin user created: " + adminEmail);
        }
    }

    private void seedRegularUser() {
        String userEmail = "user@example.com";
        if (!customerRepository.existsByEmail(userEmail)) {
            Customer user = new Customer();
            user.setEmail(userEmail);
            user.setFirstName("User");
            user.setLastName("Userson");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(Set.of(Role.ROLE_USER));
            customerRepository.save(user);
            System.out.println("[DATA_SEED] Regular user created: " + userEmail);
        }
    }
    private void seedRegularUser01() {
        String userEmail = "user01@example.com";
        if (!customerRepository.existsByEmail(userEmail)) {
            Customer user = new Customer();
            user.setEmail(userEmail);
            user.setFirstName("User01");
            user.setLastName("Userson");
            user.setPassword(passwordEncoder.encode("password01"));
            user.setRoles(Set.of(Role.ROLE_USER));
            customerRepository.save(user);
            System.out.println("[DATA_SEED] Regular user created: " + userEmail);
        }
    }


    private void seedOperatorsAndPlans() {
        if (operatorRepository.count() > 0) {
            return;
        }

        System.out.println("[DATA_SEED] Seeding Operators and Plans...");

        //1 Create operators
        Operator telia = new Operator();
        telia.setName("Telia");
        operatorRepository.save(telia);

        Operator telenor = new Operator();
        telenor.setName("Telenor");
        operatorRepository.save(telenor);

        //Create Plans for Telia
        createAndSavePlan("Fiber 50", new BigDecimal("299.00"), ServiceType.INTERNET, null, true, telia);
        createAndSavePlan("Fiber 100", new BigDecimal("399.00"), ServiceType.INTERNET, null, true, telia);
        createAndSavePlan("Fiber 300", new BigDecimal("599.00"), ServiceType.INTERNET, null, false, telia); // Inactive

        createAndSavePlan("Mobile Basic", new BigDecimal("99.00"), ServiceType.MOBILE, 1000, true, telia);
        createAndSavePlan("Mobile Plus", new BigDecimal("199.00"), ServiceType.MOBILE, 5000, true, telia);
        createAndSavePlan("Mobile Unlimited", new BigDecimal("299.00"), ServiceType.MOBILE, null, true, telia);

        // Create Plans for Telenor
        createAndSavePlan("Fiber 100", new BigDecimal("349.00"), ServiceType.INTERNET, null, true, telenor);
        createAndSavePlan("Fiber 300", new BigDecimal("549.00"), ServiceType.INTERNET, null, true, telenor);

        createAndSavePlan("Mobile Plus", new BigDecimal("189.00"), ServiceType.MOBILE, 5000, true, telenor);
        createAndSavePlan("Mobile Unlimited", new BigDecimal("289.00"), ServiceType.MOBILE, null, false, telenor); // Inactive

        System.out.println("[DATA_SEED] Operators and Plans created successfully.");
    }

    private void createAndSavePlan(String name, BigDecimal price, ServiceType type, Integer dataLimitMb, boolean active, Operator operator) {
        Plan plan = new Plan();
        plan.setName(name);
        plan.setPrice(price);
        plan.setServiceType(type);
        plan.setDataLimitMb(dataLimitMb);
        plan.setActive(active);
        plan.setOperator(operator);
        planRepository.save(plan);
    }



}
