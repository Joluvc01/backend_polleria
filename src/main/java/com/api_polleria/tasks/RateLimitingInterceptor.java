package com.api_polleria.tasks;

import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Status;
import com.api_polleria.service.CustomerService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    public static final int SC_TOO_MANY_REQUESTS = 429;

    private final Map<String, LocalDateTime> customerRequestTimes = new HashMap<>();
    private final CustomerService customerService;

    public RateLimitingInterceptor(CustomerService customerService) {
        this.customerService = customerService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String customerId = request.getHeader("customerId");
        if (customerId != null) {
            LocalDateTime lastRequestTime = customerRequestTimes.get(customerId);
            LocalDateTime currentTime = LocalDateTime.now();

            if (lastRequestTime != null && ChronoUnit.HOURS.between(lastRequestTime, currentTime) < 1) {
                Optional<Customer> customer = customerService.findById(Long.parseLong(customerId));
                if (customer.isPresent()) {
                    Customer customerEntity = customer.get();
                    customerEntity.setStatus(Status.BLOCKED);
                    customerService.save(customerEntity);
                }
                response.setStatus(SC_TOO_MANY_REQUESTS);
                return false;
            } else {
                customerRequestTimes.put(customerId, currentTime);
            }
        }

        return true;
    }
}