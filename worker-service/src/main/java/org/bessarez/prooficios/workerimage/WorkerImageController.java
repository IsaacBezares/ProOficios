package org.bessarez.prooficios.workerimage;

import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkerImageController {

    private final WorkerImageService wiService;

    public WorkerImageController(WorkerImageService wiService) {
        this.wiService = wiService;
    }

    @GetMapping("/workerimages")
    CollectionModel<EntityModel<WorkerImage>> all() {
        return wiService.all();
    }

    @GetMapping("/workerimages/worker/{workerId}")
    CollectionModel<EntityModel<WorkerImage>> allByWorker(@PathVariable Long workerId) {
        return wiService.allByWorker(workerId);
    }

    @GetMapping("/workerimages/{id}")
    EntityModel<WorkerImage> one(@PathVariable Long id) {
        return wiService.one(id);
    }

    @PostMapping("/workerimages/add/{workerId}")
    List<WorkerImage> addImages(@PathVariable Long workerId, @RequestBody List<WorkerImage> newImages) {
        return wiService.addImages(workerId, newImages);
    }

    @PutMapping("/workerimages/{id}")
    WorkerImage update(@PathVariable Long id, @RequestBody WorkerImage newWI) {
        return wiService.update(id, newWI);
    }

    @DeleteMapping("/workerimages/{id}")
    void delete(@PathVariable Long id) {
        wiService.delete(id);
    }

    @Transactional
    @DeleteMapping("/workerimages/deleteAll/{workerId}")
    void deleteAll(@PathVariable Long workerId) {
        wiService.deleteAll(workerId);
    }
}
