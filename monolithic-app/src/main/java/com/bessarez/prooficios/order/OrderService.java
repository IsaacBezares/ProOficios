package com.bessarez.prooficios.order;

import com.bessarez.prooficios.review.Review;
import com.bessarez.prooficios.review.ReviewRepository;
import com.bessarez.prooficios.worker.Worker;
import com.bessarez.prooficios.worker.WorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class OrderService {
    private final OrderRepository oRepository;
    private final ReviewRepository rRepository;
    private final WorkerRepository wRepository;
    private final OrderModelAssembler assembler;


    public OrderService(OrderRepository oRepository, ReviewRepository rRepository, WorkerRepository wRepository, OrderModelAssembler assembler) {
        this.oRepository = oRepository;
        this.rRepository = rRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<Order>> all(){
        List<EntityModel<Order>> orders = oRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel());
    }

    EntityModel<Order> one(Long id){
        Order order = oRepository.findById(id)
                .orElseThrow(() -> new OrderNotfoundException(id));

        return assembler.toModel(order);
    }

    Order create(Order newOrder){
        return oRepository.save(newOrder);
    }

    Order update(Long id, Order newOrder){
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

    void delete(Long id){
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
