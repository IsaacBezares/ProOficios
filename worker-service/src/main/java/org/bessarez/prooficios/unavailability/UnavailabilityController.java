package org.bessarez.prooficios.unavailability;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UnavailabilityController {

    private final UnavailabilityService uService;

    public UnavailabilityController(UnavailabilityService uService) {
        this.uService = uService;
    }

    @GetMapping("/unavailability")
    CollectionModel<EntityModel<Unavailability>> all() {
        return uService.all();
    }

    @GetMapping("/unavailability/worker/{workerId}")
    List<LocalDate> allByWorker(@PathVariable Long workerId) {
        return uService.allByWorker(workerId);
    }

    @GetMapping("/unavailability/{id}")
    EntityModel<Unavailability> one(@PathVariable Long id) {
        return uService.one(id);
    }

    @PostMapping("/unavailability")
    Unavailability create(@RequestBody Unavailability unavailability) {
        return uService.create(unavailability);
    }

    @PostMapping("/unavailability/worker/{workerId}")
    List<Unavailability> bookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {
        return uService.bookDays(workerId, uRange);
    }

    @PutMapping("/unavailability/{id}")
    Unavailability update(@PathVariable Long id, @RequestBody Unavailability newU) {
        return uService.update(id, newU);
    }

    @DeleteMapping("/unavailability/{id}")
    void delete(@PathVariable Long id) {
        uService.delete(id);
    }

    @DeleteMapping("/unavailability/worker/{workerId}")
    void unbookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {
        uService.unbookDays(workerId, uRange);
    }
}