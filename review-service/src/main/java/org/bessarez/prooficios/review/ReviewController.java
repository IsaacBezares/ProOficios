package org.bessarez.prooficios.review;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.bessarez.prooficios.review.errorhandling.WorkerServiceNotRespondingException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    CollectionModel<EntityModel<Review>> all() {
        return reviewService.all();
    }

    @GetMapping("/reviews/worker/{workerId}")
    CollectionModel<EntityModel<Review>> allByWorker(@PathVariable Long workerId) {
        return reviewService.allByWorker(workerId);
    }

    @GetMapping("/reviews/user/{userId}")
    CollectionModel<EntityModel<Review>> allByUser(@PathVariable Long userId) {
        return reviewService.allByUser(userId);
    }

    @GetMapping("/reviews/{id}")
    EntityModel<Review> one(@PathVariable Long id) {
        return reviewService.one(id);
    }

    @CircuitBreaker(name= "worker", fallbackMethod = "workerServiceNotResponding")
    @PostMapping("/reviews")
    Review create(@RequestBody Review newReview, @RequestHeader("Authorization") String bearer) {
        return reviewService.create(newReview, bearer);
    }

    @CircuitBreaker(name= "worker", fallbackMethod = "workerServiceNotResponding")
    @PutMapping("/reviews/{id}")
    Review update(@PathVariable Long id, @RequestBody Review newReview, @RequestHeader("Authorization") String bearer) {
        return reviewService.update(id, newReview, bearer);
    }

    @CircuitBreaker(name= "worker", fallbackMethod = "workerServiceNotResponding")
    @DeleteMapping("/reviews/{id}")
    void delete(@PathVariable Long id, @RequestHeader("Authorization") String bearer) {
        reviewService.delete(id, bearer);
    }

    @CircuitBreaker(name= "worker", fallbackMethod = "workerServiceNotResponding")
    @DeleteMapping("/reviews/orders/{orderId}")
    void deleteByOrderId(@PathVariable Long orderId, @RequestHeader("Authorization") String bearer) {
        reviewService.deleteByOrderId(orderId, bearer);
    }

    public Review workerServiceNotResponding(Review review, String bearer, RuntimeException ex){
        if (ex instanceof WebClientRequestException || ex instanceof WebClientResponseException.ServiceUnavailable){
            throw new WorkerServiceNotRespondingException();
        }
        throw ex;
    }

    public Review workerServiceNotResponding(Long id, Review review, String bearer, RuntimeException ex){
        if (ex instanceof WebClientRequestException || ex instanceof WebClientResponseException.ServiceUnavailable){
            throw new WorkerServiceNotRespondingException();
        }
        throw ex;
    }

    public void workerServiceNotResponding(Long id, String bearer, RuntimeException ex){
        if (ex instanceof WebClientRequestException || ex instanceof WebClientResponseException.ServiceUnavailable){
            throw new WorkerServiceNotRespondingException();
        }
        throw ex;
    }

}