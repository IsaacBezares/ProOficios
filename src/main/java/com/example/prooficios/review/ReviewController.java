package com.example.prooficios.review;

import com.example.prooficios.user.UserNotFoundException;
import com.example.prooficios.user.UserRepository;
import com.example.prooficios.worker.Worker;
import com.example.prooficios.worker.WorkerNotFoundException;
import com.example.prooficios.worker.WorkerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Reviews")
public class ReviewController {

    private final ReviewRepository rRepository;
    private final WorkerRepository wRepository;
    private final UserRepository uRepository;

    private final ReviewModelAssembler assembler;

    public ReviewController(ReviewRepository rRepository, WorkerRepository wRepository, UserRepository uRepository, ReviewModelAssembler assembler) {
        this.rRepository = rRepository;
        this.wRepository = wRepository;
        this.uRepository = uRepository;
        this.assembler = assembler;
    }

    @GetMapping("/reviews")
    @Operation(summary = "Gets all reviews")
    CollectionModel<EntityModel<Review>> all(){
        List<EntityModel<Review>> reviews = rRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                reviews,
                linkTo(methodOn(ReviewController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/reviews/worker/{workerId}")
    @Operation(summary = "Gets all worker reviews")
    CollectionModel<EntityModel<Review>> allByWorker(@PathVariable Long workerId) {

        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        List<EntityModel<Review>> reviews = rRepository.findAllByWorker_Id(workerId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                reviews,
                linkTo(methodOn(ReviewController.class).allByWorker(workerId)).withSelfRel()
        );
    }

    @GetMapping("/reviews/user/{userId}")
    @Operation(summary = "Gets all reviews posted by the user")
    CollectionModel<EntityModel<Review>> allByUser(@PathVariable Long userId) {

        uRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<EntityModel<Review>> reviews = rRepository.findAllByUser_Id(userId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                reviews,
                linkTo(methodOn(ReviewController.class).allByUser(userId)).withSelfRel()
        );
    }

    @GetMapping("/reviews/{id}")
    @Operation(summary = "Gets one review by its id")
    EntityModel<Review> one(@PathVariable Long id){
        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        return assembler.toModel(review);
    }

    @Transactional
    @PostMapping("/reviews")
    @Operation(summary = "Creates a review")
    Review create(@RequestBody Review newReview){

        Worker worker = wRepository.findById(newReview.getWorker().getId())
                .orElseThrow(() -> new WorkerNotFoundException(newReview.getWorker().getId()));

        System.out.println(worker.getId());

        worker.setNoReviews(worker.getNoReviews() + 1);
        worker.setRating((((worker.getRating() == null) ? 0 : worker.getRating() * (worker.getNoReviews() - 1)) + newReview.getRating()) / worker.getNoReviews());
        wRepository.save(worker);

        return rRepository.save(newReview);
    }

    @Transactional
    @PutMapping("/reviews/{id}")
    @Operation(summary = "Updates a review")
    Review update(@PathVariable Long id, @RequestBody Review newReview){

        return rRepository.findById(id)
                .map(review -> {
                    Worker worker = wRepository.findById(review.getWorker().getId())
                            .orElseThrow(() -> new WorkerNotFoundException(review.getWorker().getId()));

                    Double ratingDifferential = newReview.getRating() - review.getRating();
                    worker.setRating(worker.getRating() + ratingDifferential / worker.getNoReviews());
                    wRepository.save(worker);

                    review.setComment(newReview.getComment());
                    review.setRating(newReview.getRating());
                    review.setDate(newReview.getDate());

                    return rRepository.save(review);
                })
                .orElseGet(() -> {
                    newReview.setId(id);
                    return rRepository.save(newReview);
                });
    }

    @Transactional
    @DeleteMapping("/reviews/{id}")
    @Operation(summary = "Removes a review")
    void delete(@PathVariable Long id){
        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        Worker worker = review.getWorker();
        worker.setNoReviews(worker.getNoReviews() - 1);
        if (worker.getNoReviews() == 0)
            worker.setRating(null);
        else
            worker.setRating(((worker.getRating() * (worker.getNoReviews() + 1)) - review.getRating()) / worker.getNoReviews());

        wRepository.save(worker);
        rRepository.deleteById(id);
    }
}