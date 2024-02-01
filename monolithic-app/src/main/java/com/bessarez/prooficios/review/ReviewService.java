package com.bessarez.prooficios.review;

import com.bessarez.prooficios.worker.Worker;
import com.bessarez.prooficios.worker.WorkerNotFoundException;
import com.bessarez.prooficios.worker.WorkerRepository;
import com.bessarez.prooficios.user.UserNotFoundException;
import com.bessarez.prooficios.user.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository rRepository;
    private final WorkerRepository wRepository;
    private final UserRepository uRepository;

    private final ReviewModelAssembler assembler;

    public ReviewService(ReviewRepository rRepository, WorkerRepository wRepository, UserRepository uRepository, ReviewModelAssembler assembler) {
        this.rRepository = rRepository;
        this.wRepository = wRepository;
        this.uRepository = uRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<Review>> all(){
        List<EntityModel<Review>> reviews = rRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(
                reviews,
                linkTo(methodOn(ReviewController.class).all()).withSelfRel()
        );
    }

    CollectionModel<EntityModel<Review>> allByWorker(Long workerId) {

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

    CollectionModel<EntityModel<Review>> allByUser(Long userId) {

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

    EntityModel<Review> one(Long id){
        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        return assembler.toModel(review);
    }

    Review create(Review newReview){

        Worker worker = wRepository.findById(newReview.getWorker().getId())
                .orElseThrow(() -> new WorkerNotFoundException(newReview.getWorker().getId()));

        System.out.println(worker.getId());

        worker.setNoReviews(worker.getNoReviews() + 1);
        worker.setRating((((worker.getRating() == null) ? 0 : worker.getRating() * (worker.getNoReviews() - 1)) + newReview.getRating()) / worker.getNoReviews());
        wRepository.save(worker);

        return rRepository.save(newReview);
    }

    Review update(Long id, Review newReview){

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

    void delete(Long id){
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
