package org.bessarez.prooficios.order;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.bessarez.prooficios.order.errorhandling.OrderNotfoundException;
import org.bessarez.prooficios.user.User;
import org.bessarez.prooficios.user.UserRepository;
import org.bessarez.prooficios.worker.Worker;
import org.bessarez.prooficios.worker.WorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class OrderService {
    private final OrderRepository oRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final OrderModelAssembler assembler;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;

    public OrderService(OrderRepository oRepository, UserRepository userRepository, WorkerRepository workerRepository, OrderModelAssembler assembler, WebClient.Builder webClientBuilder, ObservationRegistry observationRegistry) {
        this.oRepository = oRepository;
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
        this.assembler = assembler;
        this.webClientBuilder = webClientBuilder;
        this.observationRegistry = observationRegistry;
    }

    CollectionModel<EntityModel<Order>> all() {
        List<EntityModel<Order>> orders = oRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    CollectionModel<EntityModel<Order>> allByWorkerId(Long workerId) {
        List<EntityModel<Order>> orders = oRepository.findAllByWorker_Id(workerId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).allByWorkerId(workerId)).withSelfRel());
    }

    CollectionModel<EntityModel<Order>> allByUserId(Long userId) {
        List<EntityModel<Order>> orders = oRepository.findAllByUser_Id(userId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).allByUserId(userId)).withSelfRel());
    }

    EntityModel<Order> one(Long id) {
        Order order = oRepository.findById(id)
                .orElseThrow(() -> new OrderNotfoundException(id));

        return assembler.toModel(order);
    }

    Order create(Order newOrder) {
        return oRepository.save(newOrder);
    }

    Order update(Long id, Order newOrder) {
        return oRepository.findById(id)
                .map(order -> {
                    order.setOrderDate(newOrder.getOrderDate());
                    order.setOrderStatus(newOrder.getOrderStatus());
                    return oRepository.save(order);
                })
                .orElseGet(() -> {
                    newOrder.setId(id);
                    return oRepository.save(newOrder);
                });
    }


    void delete(Long id, String bearer) {
        Order order= oRepository.findById(id)
                .orElseThrow(() -> new OrderNotfoundException(id));

        Observation reviewServiceObservation = Observation.createNotStarted("review-service-lookup",
                this.observationRegistry);

        reviewServiceObservation.lowCardinalityKeyValue("call", "review-service");
        reviewServiceObservation.observe(() -> {
            webClientBuilder.build().delete()
                    .uri("http://review-service/reviews/orders/" + order.getId().toString())
                    .header("Authorization", bearer)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            oRepository.deleteById(id);
        });
    }

    @KafkaListener(topics = "workerCreatedTopic")
    void handleWorkerCreated(Long workerId) {
        workerRepository.save(new Worker(workerId));
    }

    @KafkaListener(topics = "userCreatedTopic")
    void handleUserCreated(Long userId) {
        userRepository.save(new User(userId));
    }

    @KafkaListener(topics = "workerDeletedTopic")
    void handleWorkerDeleted(Long workerId) {
        workerRepository.deleteById(workerId);
    }

    @KafkaListener(topics = "userDeletedTopic")
    void handleUserDeleted(Long userId) {
        userRepository.deleteById(userId);
    }

}
