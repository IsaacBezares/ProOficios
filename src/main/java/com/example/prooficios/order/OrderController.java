package com.example.prooficios.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    @Operation(summary = "Gets all orders")
    CollectionModel<EntityModel<Order>> all(){
        return orderService.all();
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Gets one order by its id")
    EntityModel<Order> one(@PathVariable Long id){
        return orderService.one(id);
    }

    @PostMapping("/orders")
    @Operation(summary = "Creates an order")
    Order create(@RequestBody Order newOrder){
        return orderService.create(newOrder);
    }

    @PutMapping("/orders/{id}")
    @Operation(summary = "Updates an order")
    Order update(@PathVariable Long id, @RequestBody Order newOrder) {
        return orderService.update(id, newOrder);
    }

    @Transactional
    @DeleteMapping("/orders/{id}")
    @Operation(summary = "Removes an order")
    void delete(@PathVariable Long id){
        orderService.delete(id);
    }
}
