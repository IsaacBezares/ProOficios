package com.example.prooficios.workerimage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Worker Images")
public class WorkerImageController {

    private final WorkerImageService wiService;

    public WorkerImageController(WorkerImageService wiService) {
        this.wiService = wiService;
    }

    @GetMapping("/workerimages")
    @Operation(summary = "Returns all worker images")
    CollectionModel<EntityModel<WorkerImage>> all() {
        return wiService.all();
    }

    @GetMapping("/workerimages/worker/{workerId}")
    @Operation(summary = "Returns all worker images by worker id")
    CollectionModel<EntityModel<WorkerImage>> allByWorker(@PathVariable Long workerId) {
        return wiService.allByWorker(workerId);
    }

    @GetMapping("/workerimages/{id}")
    @Operation(summary = "Returns one worker by id")
    EntityModel<WorkerImage> one(@PathVariable Long id) {
        return wiService.one(id);
    }

    @PostMapping("/workerimages/add/{workerId}")
    @Operation(summary = "Adds multiple images to a worker")
    List<WorkerImage> addImages(@PathVariable Long workerId, @RequestBody List<WorkerImage> newImages) {
        return wiService.addImages(workerId, newImages);
    }

    @PutMapping("/workerimages/{id}")
    @Operation(summary = "Updates a worker image")
    WorkerImage update(@PathVariable Long id, @RequestBody WorkerImage newWI) {
        return wiService.update(id, newWI);
    }

    @DeleteMapping("/workerimages/{id}")
    @Operation(summary = "Remove a worker image by its id")
    void delete(@PathVariable Long id) {
        wiService.delete(id);
    }

    @Transactional
    @DeleteMapping("/workerimages/deleteAll/{workerId}")
    @Operation(summary = "Remove all worker's images by worker id")
    void deleteAll(@PathVariable Long workerId) {
        wiService.deleteAll(workerId);
    }
}
