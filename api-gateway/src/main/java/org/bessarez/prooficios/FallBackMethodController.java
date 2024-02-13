package org.bessarez.prooficios;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @RequestMapping("/workerServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String workerServiceFallBack() {
        return "Worker Service is taking longer than expected to respond." +
                " Please check back in sometime ";
    }

    @RequestMapping("/userServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String userServiceFallBack() {
        return "User Service is taking longer than expected to respond." +
                " Please check back in sometime ";
    }

    @RequestMapping("/serviceServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String serviceServiceFallBack() {
        return "Service Service is taking longer than expected to respond." +
                " Please check back in sometime ";
    }

    @RequestMapping("/orderServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String orderServiceFallBack() {
        return "Order Service is taking longer than expected to respond." +
                " Please check back in sometime ";
    }

    @RequestMapping("/reviewServiceFallBack")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String reviewServiceFallBack() {
        return "Review Service is taking longer than expected to respond." +
                " Please check back in sometime ";
    }
}
