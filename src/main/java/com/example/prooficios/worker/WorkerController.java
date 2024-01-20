package com.example.prooficios.worker;

import com.example.prooficios.service.Service;
import com.example.prooficios.service.ServiceNotFoundException;
import com.example.prooficios.service.ServiceRepository;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Workers")
public class WorkerController {

    private final WorkerRepository wRepository;
    private final ServiceRepository sRepository;
    private final WorkerModelAssembler assembler;

    public WorkerController(WorkerRepository wRepository, ServiceRepository sRepository, WorkerModelAssembler assembler) {
        this.wRepository = wRepository;
        this.sRepository = sRepository;
        this.assembler = assembler;
    }

    @GetMapping("/workers")
    @Operation(summary = "Returns all workers")
    CollectionModel<EntityModel<Worker>> all(){
        List<EntityModel<Worker>> workers = wRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workers,
                linkTo(methodOn(WorkerController.class).all()).withSelfRel());
    }

    @GetMapping("/workers/{id}")
    @Operation(summary = "Returns one worker")
    EntityModel<Worker> one(@PathVariable Long id){
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        return assembler.toModel(worker);
    }

    @PostMapping("/workers")
    @Operation(summary = "Creates a worker")
    Worker create(@RequestBody Worker newW){
        newW.setNoReviews(0);
        return wRepository.save(newW);
    }

    @PostMapping("/workers/{id}/addservices")
    @Operation(summary = "Add multiple services to a worker")
    Worker addServices(@PathVariable Long id, @RequestBody List<Service> newServices){
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        newServices.forEach(newService -> {
            sRepository.findById(newService.getId())
                    .orElseThrow(() -> new ServiceNotFoundException(newService.getId()));

            System.out.println(newService);

            worker.getWorkerServices().add(newService);
        });

        return wRepository.save(worker);
    }

    @PutMapping("/workers/{id}")
    @Operation(summary = "Updates a worker")
    Worker update(@PathVariable Long id, @RequestBody Worker newW){
        return wRepository.findById(id)
                .map(worker -> {
                    worker.setName(newW.getName());
                    worker.setEmail(newW.getEmail());
                    worker.setPhone(newW.getPhone());
                    worker.setJoinDate(newW.getJoinDate());
                    worker.setDescription(newW.getDescription());
                    worker.setPassword(newW.getPassword());
                    return wRepository.save(worker);
                })
                .orElseGet(() -> {
                    newW.setId(id);
                    return wRepository.save(newW);
                });
    }



    @PutMapping("/workers/{id}/profilepicture")
    @Operation(summary = "Updates a worker's profile picture")
    Worker updateProfilePic(@PathVariable Long id, @RequestBody TextNode imageURL){
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        worker.setProfilePicture(imageURL.asText());

        return wRepository.save(worker);
    }

    @DeleteMapping("/workers/{id}")
    @Operation(summary = "Removes a worker")
    void delete(@PathVariable Long id){
        wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        wRepository.deleteById(id);
    }

    @DeleteMapping("/workers/{id}/removeservices")
    @Operation(summary = "Removes services from a worker")
    Worker removeServices(@PathVariable Long id, @RequestBody List<Service> services){

        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        services.forEach(service -> {
            worker.getWorkerServices().remove(service);
        });

        return wRepository.save(worker);

    }
}
