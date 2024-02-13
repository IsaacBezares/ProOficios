package org.bessarez.prooficios.order;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.bessarez.prooficios.order.errorhandling.ReviewServiceNotRespondingException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    CollectionModel<EntityModel<Order>> all() {
        return orderService.all();
    }

    @GetMapping("/orders/worker/{workerId}")
    CollectionModel<EntityModel<Order>> allByWorkerId(@PathVariable Long workerId) {
        return orderService.allByWorkerId(workerId);
    }

    @GetMapping("/orders/user/{userId}")
    CollectionModel<EntityModel<Order>> allByUserId(@PathVariable Long userId) {
        return orderService.allByUserId(userId);
    }

    @GetMapping("/orders/{id}")
    EntityModel<Order> one(@PathVariable Long id) {
        return orderService.one(id);
    }

    @PostMapping("/orders")
    Order create(@RequestBody Order newOrder) {
        return orderService.create(newOrder);
    }

    @PutMapping("/orders/{id}")
    Order update(@PathVariable Long id, @RequestBody Order newOrder) {
        return orderService.update(id, newOrder);
    }

    @CircuitBreaker(name = "review", fallbackMethod = "reviewServiceNotResponding")
    @DeleteMapping("/orders/{id}")
    void delete(@PathVariable Long id, @RequestHeader("Authorization") String bearer) {
        orderService.delete(id, bearer);
    }

    void reviewServiceNotResponding(Long id, String bearer, RuntimeException ex){
        if (ex instanceof WebClientRequestException || ex instanceof WebClientResponseException.ServiceUnavailable){
            throw new ReviewServiceNotRespondingException();
        }
        throw ex;
    }
}
