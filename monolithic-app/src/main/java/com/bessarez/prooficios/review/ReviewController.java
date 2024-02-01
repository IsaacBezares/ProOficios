package com.bessarez.prooficios.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    @Operation(summary = "Gets all reviews")
    CollectionModel<EntityModel<Review>> all(){
        return reviewService.all();
    }

    @GetMapping("/reviews/worker/{workerId}")
    @Operation(summary = "Gets all worker reviews")
    CollectionModel<EntityModel<Review>> allByWorker(@PathVariable Long workerId) {
        return reviewService.allByWorker(workerId);
    }

    @GetMapping("/reviews/user/{userId}")
    @Operation(summary = "Gets all reviews posted by the user")
    CollectionModel<EntityModel<Review>> allByUser(@PathVariable Long userId) {
        return reviewService.allByUser(userId);
    }

    @GetMapping("/reviews/{id}")
    @Operation(summary = "Gets one review by its id")
    EntityModel<Review> one(@PathVariable Long id){
        return reviewService.one(id);
    }

    @Transactional
    @PostMapping("/reviews")
    @Operation(summary = "Creates a review")
    Review create(@RequestBody Review newReview){
        return reviewService.create(newReview);
    }

    @Transactional
    @PutMapping("/reviews/{id}")
    @Operation(summary = "Updates a review")
    Review update(@PathVariable Long id, @RequestBody Review newReview){
        return reviewService.update(id, newReview);
    }

    @Transactional
    @DeleteMapping("/reviews/{id}")
    @Operation(summary = "Removes a review")
    void delete(@PathVariable Long id){
        reviewService.delete(id);
    }
}