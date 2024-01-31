package com.example.prooficios.unavailability;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Unavailability")
public class UnavailabilityController {

    private final UnavailabilityService uService;

    public UnavailabilityController(UnavailabilityService uService) {
        this.uService = uService;
    }

    @GetMapping("/unavailability")
    @Operation(summary = "Gets all unavailable dates")
    CollectionModel<EntityModel<Unavailability>> all() {
        return uService.all();
    }

    @GetMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Gets all worker's unavailable dates")
    List<LocalDate> allByWorker(@PathVariable Long workerId) {
        return uService.allByWorker(workerId);
    }

    @GetMapping("/unavailability/{id}")
    @Operation(summary = "Gets an specific unavailable date")
    EntityModel<Unavailability> one(@PathVariable Long id) {
        return uService.one(id);
    }

    @PostMapping("/unavailability")
    @Operation(summary = "Creates an unavailable date")
    Unavailability create(@RequestBody Unavailability unavailability) {
        return uService.create(unavailability);
    }

    @PostMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Books days for a worker")
    List<Unavailability> bookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {
        return uService.bookDays(workerId, uRange);
    }

    @PutMapping("/unavailability/{id}")
    @Operation(summary = "Updates an unavailable date")
    Unavailability update(@PathVariable Long id, @RequestBody Unavailability newU) {
        return uService.update(id, newU);
    }

    @DeleteMapping("/unavailability/{id}")
    @Operation(summary = "Removes an unavailable date")
    void delete(@PathVariable Long id) {
        uService.delete(id);
    }

    @DeleteMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Unbooks days for a worker")
    void unbookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {
        uService.unbookDays(workerId, uRange);
    }
}