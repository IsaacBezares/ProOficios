package com.bessarez.prooficios.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services")
    @Operation(summary = "Returns all services")
    CollectionModel<EntityModel<Service>> all() {
        return serviceService.all();
    }

    @GetMapping("/services/{id}")
    @Operation(summary = "Returns one service by its id")
    EntityModel<Service> one(@PathVariable Long id){
        return serviceService.one(id);
    }

    @PostMapping("/services")
    @Operation(summary = "Creates a service")
    Service create(@RequestBody Service newS){
        return serviceService.create(newS);
    }

    @PutMapping("/services/{id}")
    @Operation(summary = "Updates a service")
    Service update(@PathVariable Long id, @RequestBody Service newS){
       return serviceService.update(id,newS);
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Removes a service")
    void delete(@PathVariable Long id){
        serviceService.delete(id);
    }
}
