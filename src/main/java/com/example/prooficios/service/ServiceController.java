package com.example.prooficios.service;

import com.example.prooficios.worker.WorkerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Services")
public class ServiceController {

    private final ServiceRepository sRepository;
    private final WorkerRepository wRepository;
    private final ServiceModelAssembler assembler;

    public ServiceController(ServiceRepository sRepository, WorkerRepository wRepository, ServiceModelAssembler assembler) {
        this.sRepository = sRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    @GetMapping("/services")
    @Operation(summary = "Returns all services")
    CollectionModel<EntityModel<Service>> all() {
        List<EntityModel<Service>> serviceCategories = sRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(serviceCategories,
                linkTo(methodOn(ServiceController.class).all()).withSelfRel());

    }

    @GetMapping("/services/{id}")
    @Operation(summary = "Returns one service by its id")
    EntityModel<Service> one(@PathVariable Long id){

        Service service = sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        return assembler.toModel(service);
    }

    @PostMapping("/services")
    @Operation(summary = "Creates a service")
    Service create(@RequestBody Service newS){
        return sRepository.save(newS);
    }

    @PutMapping("/services/{id}")
    @Operation(summary = "Updates a service")
    Service update(@PathVariable Long id, @RequestBody Service newS){
        return sRepository.findById(id)
                .map(service -> {
                    service.setTitle(newS.getTitle());
                    return sRepository.save(service);
                })
                .orElseGet(() -> {
                    newS.setId(id);
                    return sRepository.save(newS);
                });
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Removes a service")
    void delete(@PathVariable Long id){
        Service service = sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        service.getWorkers().forEach(w -> w.getWorkerServices().remove(service));
        wRepository.saveAll(service.getWorkers());

        sRepository.deleteById(id);
    }
}
