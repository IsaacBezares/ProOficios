package com.example.prooficios.order;

import com.example.prooficios.worker.Worker;
import com.example.prooficios.worker.WorkerRepository;
import com.example.prooficios.review.Review;
import com.example.prooficios.review.ReviewRepository;
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
@Tag(name = "Orders")
public class OrderController {

    private final OrderRepository oRepository;
    private final ReviewRepository rRepository;
    private final WorkerRepository wRepository;
    private final OrderModelAssembler assembler;


    public OrderController(OrderRepository oRepository, ReviewRepository rRepository, WorkerRepository wRepository, OrderModelAssembler assembler) {
        this.oRepository = oRepository;
        this.rRepository = rRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    @Operation(summary = "Gets all orders")
    CollectionModel<EntityModel<Order>> all(){
        List<EntityModel<Order>> orders = oRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Gets one order by its id")
    EntityModel<Order> one(@PathVariable Long id){
        Order order = oRepository.findById(id)
                .orElseThrow(() -> new OrderNotfoundException(id));

        return assembler.toModel(order);
    }

    @PostMapping("/orders")
    @Operation(summary = "Creates an order")
    Order create(@RequestBody Order newOrder){
        return oRepository.save(newOrder);
    }

    @PutMapping("/orders/{id}")
    @Operation(summary = "Updates an order")
    Order update(@PathVariable Long id, @RequestBody Order newOrder){
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

    @Transactional
    @DeleteMapping("/orders/{id}")
    @Operation(summary = "Removes an order")
    void delete(@PathVariable Long id){
        Order order = oRepository.findById(id)
                .orElseThrow(() -> new OrderNotfoundException(id));

        Review review = order.getReview();

        if (review != null){
            Worker worker = order.getWorker();
            worker.setNoReviews(worker.getNoReviews() - 1);
            if (worker.getNoReviews() == 0)
                worker.setRating(null);
            else
                worker.setRating(((worker.getRating() * (worker.getNoReviews() + 1)) - review.getRating()) / worker.getNoReviews());

            wRepository.save(worker);
            rRepository.deleteById(review.getId());
        }

        oRepository.deleteById(id);
    }
}
