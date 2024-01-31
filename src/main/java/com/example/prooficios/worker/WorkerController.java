package com.example.prooficios.worker;

import com.example.prooficios.service.Service;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/workers")
    @Operation(summary = "Returns all workers")
    CollectionModel<EntityModel<Worker>> all() {
        return workerService.all();
    }

    @GetMapping("/workers/{id}")
    @Operation(summary = "Returns one worker")
    EntityModel<Worker> one(@PathVariable Long id) {
        return workerService.one(id);
    }

    @PostMapping("/workers")
    @Operation(summary = "Creates a worker")
    Worker create(@RequestBody Worker newW) {
        return workerService.create(newW);
    }

    @PostMapping("/workers/{id}/addservices")
    @Operation(summary = "Add multiple services to a worker")
    Worker addServices(@PathVariable Long id, @RequestBody List<Service> newServices) {
        return workerService.addServices(id, newServices);
    }

    @PutMapping("/workers/{id}")
    @Operation(summary = "Updates a worker")
    Worker update(@PathVariable Long id, @RequestBody Worker newW) {
        return workerService.update(id, newW);
    }

    @PutMapping("/workers/{id}/profilepicture")
    @Operation(summary = "Updates a worker's profile picture")
    Worker updateProfilePic(@PathVariable Long id, @RequestBody TextNode imageURL) {
        return workerService.updateProfilePic(id, imageURL);
    }

    @DeleteMapping("/workers/{id}")
    @Operation(summary = "Removes a worker")
    void delete(@PathVariable Long id) {
        workerService.delete(id);
    }

    @DeleteMapping("/workers/{id}/removeservices")
    @Operation(summary = "Removes services from a worker")
    Worker removeServices(@PathVariable Long id, @RequestBody List<Service> services) {
        return workerService.removeServices(id, services);
    }
}
