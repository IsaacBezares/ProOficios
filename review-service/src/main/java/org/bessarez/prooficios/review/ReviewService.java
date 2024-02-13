package org.bessarez.prooficios.review;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.bessarez.prooficios.dto.WorkerRatingInfo;
import org.bessarez.prooficios.review.errorhandling.ReviewForOrderNotFoundException;
import org.bessarez.prooficios.review.errorhandling.ReviewNotFoundException;
import org.bessarez.prooficios.user.User;
import org.bessarez.prooficios.user.UserNotFoundException;
import org.bessarez.prooficios.user.UserRepository;
import org.bessarez.prooficios.worker.Worker;
import org.bessarez.prooficios.worker.WorkerNotFoundException;
import org.bessarez.prooficios.worker.WorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository rRepository;
    private final WorkerRepository wRepository;
    private final UserRepository uRepository;
    private final WebClient.Builder webClientBuilder;
    private final ReviewModelAssembler assembler;
    private final ObservationRegistry observationRegistry;

    public ReviewService(ReviewRepository rRepository, WorkerRepository wRepository, UserRepository uRepository, WebClient.Builder webClientBuilder, ReviewModelAssembler assembler, ObservationRegistry observationRegistry) {
        this.rRepository = rRepository;
        this.wRepository = wRepository;
        this.uRepository = uRepository;
        this.webClientBuilder = webClientBuilder;
        this.assembler = assembler;
        this.observationRegistry = observationRegistry;
    }

    CollectionModel<EntityModel<Review>> all() {
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

    EntityModel<Review> one(Long id) {
        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        return assembler.toModel(review);
    }

    Review create(Review newReview, String bearer) {

        WorkerRatingInfo worker = getWorkerRatingInfo(newReview.getWorker().getId(), bearer);
        worker.setNoReviews(worker.getNoReviews() + 1);
        worker.setRating((((worker.getRating() == null) ? 0 : worker.getRating() * (worker.getNoReviews() - 1)) + newReview.getRating()) / worker.getNoReviews());

        Review review = rRepository.save(newReview);
        updateWorkerRatingInfo(worker, bearer);
        return review;
    }

    Review update(Long id, Review newReview, String bearer) {

        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        WorkerRatingInfo worker = getWorkerRatingInfo(review.getWorker().getId(), bearer);
        Double ratingDifferential = newReview.getRating() - review.getRating();
        worker.setRating(worker.getRating() + ratingDifferential / worker.getNoReviews());

        review.setComment(newReview.getComment());
        review.setRating(newReview.getRating());
        review.setDate(newReview.getDate());

        rRepository.save(review);
        updateWorkerRatingInfo(worker, bearer);

        return review;
    }

    void delete(Long id, String bearer) {
        Review review = rRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        WorkerRatingInfo worker = getWorkerRatingInfo(review.getWorker().getId(), bearer);
        calcRatingWhenRemovingReview(worker, review.getRating());

        rRepository.deleteById(id);
        updateWorkerRatingInfo(worker, bearer);
    }

    void deleteByOrderId(Long orderId, String bearer) {
        Review review = rRepository.findByOrderId(orderId);

        if (review == null){
            return;
        }

        WorkerRatingInfo worker = getWorkerRatingInfo(review.getWorker().getId(), bearer);
        calcRatingWhenRemovingReview(worker, review.getRating());

        rRepository.deleteById(review.getId());
        updateWorkerRatingInfo(worker, bearer);
    }

    @KafkaListener(topics = "workerCreatedTopic")
    void handleWorkerCreated(Long workerId) {
        wRepository.save(new Worker(workerId));
    }

    @KafkaListener(topics = "userCreatedTopic")
    void handleUserCreated(Long userId) {
        uRepository.save(new User(userId));
    }

    @KafkaListener(topics = "workerDeletedTopic")
    void handleWorkerDeleted(Long workerId) {
        wRepository.deleteById(workerId);
    }

    @KafkaListener(topics = "userDeletedTopic")
    void handleUserDeleted(Long userId) {
        uRepository.deleteById(userId);
    }

    private WorkerRatingInfo getWorkerRatingInfo(Long workerId, String bearer) {
        Observation workerServiceObservation = Observation.createNotStarted("worker-service-lookup",
                this.observationRegistry);
        workerServiceObservation.lowCardinalityKeyValue("call", "worker-service");

        return workerServiceObservation.observe(() ->
                webClientBuilder.build().get()
                        .uri("http://worker-service/workers/" + workerId.toString() + "/ratinginfo")
                        .header("Authorization", bearer)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, res -> Mono.error(new WorkerNotFoundException(workerId)))
                        .bodyToMono(WorkerRatingInfo.class)
                        .doOnError(Throwable::printStackTrace)
                        .block());
    }

    private void updateWorkerRatingInfo(WorkerRatingInfo worker, String bearer) {
        Observation workerServiceObservation = Observation.createNotStarted("worker-service-lookup",
                this.observationRegistry);
        workerServiceObservation.lowCardinalityKeyValue("call", "worker-service");

        workerServiceObservation.observe(() ->
                webClientBuilder.build().put()
                        .uri("http://worker-service/workers/" + worker.getId().toString() + "/ratinginfo")
                        .header("Authorization", bearer)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(BodyInserters.fromValue(worker))
                        .retrieve()
                        .toBodilessEntity()
                        .block()
        );
    }

    private void calcRatingWhenRemovingReview(WorkerRatingInfo worker, Double reviewRating) {
        worker.setNoReviews(worker.getNoReviews() - 1);
        if (worker.getNoReviews() == 0)
            worker.setRating(null);
        else
            worker.setRating(((worker.getRating() * (worker.getNoReviews() + 1)) - reviewRating) / worker.getNoReviews());
    }
}
