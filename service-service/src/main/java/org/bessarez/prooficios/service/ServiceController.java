package org.bessarez.prooficios.service;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services")
    CollectionModel<EntityModel<Service>> all() {
        return serviceService.all();
    }

    @GetMapping("/services/{id}")
    EntityModel<Service> one(@PathVariable Long id){
        return serviceService.one(id);
    }

    @PostMapping("/services")
    Service create(@RequestBody Service newS){
        return serviceService.create(newS);
    }

    @PutMapping("/services/{id}")
    Service update(@PathVariable Long id, @RequestBody Service newS){
       return serviceService.update(id,newS);
    }

    @DeleteMapping("/services/{id}")
    void delete(@PathVariable Long id){
        serviceService.delete(id);
    }
}
