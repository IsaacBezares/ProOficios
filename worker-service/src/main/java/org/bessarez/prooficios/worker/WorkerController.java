package org.bessarez.prooficios.worker;

import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/workers")
    CollectionModel<EntityModel<Worker>> all() {
        return workerService.all();
    }

    @GetMapping("/workers/{id}")
    EntityModel<Worker> one(@PathVariable Long id) {
        return workerService.one(id);
    }

    @GetMapping("/workers/{id}/ratinginfo")
    WorkerRatingInfo getWorkerRatingInfo(@PathVariable Long id) {
        return workerService.getWorkerRatingInfo(id);
    }

    @PostMapping("/workers")
    Worker create(@RequestBody Worker newW) {
        return workerService.create(newW);
    }

    @PostMapping("/workers/{id}/addservices")
    Worker addServices(@PathVariable Long id, @RequestBody List<Long> newServices) {
        return workerService.addServices(id, newServices);
    }

    @PutMapping("/workers/{id}")
    Worker update(@PathVariable Long id, @RequestBody Worker newW) {
        return workerService.update(id, newW);
    }

    @PutMapping("/workers/{id}/profilepicture")
    Worker updateProfilePic(@PathVariable Long id, @RequestBody TextNode imageURL) {
        return workerService.updateProfilePic(id, imageURL);
    }

    @PutMapping("/workers/{id}/ratinginfo")
    Worker updateRatingInfo(@PathVariable Long id, @RequestBody Worker worker) {
        return workerService.updateRatingInfo(id,worker);
    }

    @DeleteMapping("/workers/{id}")
    void delete(@PathVariable Long id) {
        workerService.delete(id);
    }

    @DeleteMapping("/workers/{id}/removeservices")
    Worker removeServices(@PathVariable Long id, @RequestBody List<Long> services) {
        return workerService.removeServices(id, services);
    }
}
