package com.example.prooficios.unavailability;

import com.example.prooficios.worker.Worker;
import com.example.prooficios.worker.WorkerNotFoundException;
import com.example.prooficios.worker.WorkerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Unavailability")
public class UnavailabilityController {

    private final UnavailabilityRepository uRepository;
    private final WorkerRepository wRepository;

    private final UnavailabilityModelAssembler assembler;

    public UnavailabilityController(UnavailabilityRepository uRepository, WorkerRepository wRepository, UnavailabilityModelAssembler assembler) {
        this.uRepository = uRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    @GetMapping("/unavailability")
    @Operation(summary = "Gets all unavailable dates")
    CollectionModel<EntityModel<Unavailability>> all() {

        List<EntityModel<Unavailability>> unavailabilities = uRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(unavailabilities,
                linkTo(methodOn(UnavailabilityController.class).all()).withSelfRel());
    }

    @GetMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Gets all worker's unavailable dates")
    List<LocalDate> allByWorker(@PathVariable Long workerId) {

        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        return uRepository.findAllByWorker_Id(workerId).stream()
                .map(DateOnly::getUnavailableDate)
                .toList();

    }

    @GetMapping("/unavailability/{id}")
    @Operation(summary = "Gets an specific unavailable date")
    EntityModel<Unavailability> one(@PathVariable Long id) {

        Unavailability unavailability = uRepository.findById(id)
                .orElseThrow(() -> new UnavailabilityNotFoundException(id));

        return assembler.toModel(unavailability);
    }

    @PostMapping("/unavailability")
    @Operation(summary = "Creates an unavailable date")
    Unavailability create(@RequestBody Unavailability unavailability) {
        return uRepository.save(unavailability);
    }

    @PostMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Books days for a worker")
    List<Unavailability> bookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {

        Worker worker = wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        System.out.println("------------------------");

        LocalDate start = uRange.getUnavailabilityStartDate();
        LocalDate end = uRange.getUnavailabilityEndDate();

        if (start.isAfter(end)) {
            throw new InvaildDateRangeException();
        }

        List<Unavailability> unavailableDates = start.datesUntil(end.plusDays(1))
                .map(unavailableDate -> new Unavailability(unavailableDate, worker))
                .toList();

        return uRepository.saveAll(unavailableDates);
    }

    @PutMapping("/unavailability/{id}")
    @Operation(summary = "Updates an unavailable date")
    Unavailability update(@PathVariable Long id, @RequestBody Unavailability newU) {
        return uRepository.findById(id)
                .map(unavailability -> {
                    unavailability.setUnavailableDate(newU.getUnavailableDate());
                    return uRepository.save(unavailability);
                })
                .orElseGet(() -> {
                    newU.setId(id);
                    return uRepository.save(newU);
                });
    }

    @DeleteMapping("/unavailability/{id}")
    @Operation(summary = "Removes an unavailable date")
    void delete(@PathVariable Long id) {
        uRepository.findById(id)
                .orElseThrow(() -> new UnavailabilityNotFoundException(id));

        uRepository.deleteById(id);
    }

    @DeleteMapping("/unavailability/worker/{workerId}")
    @Operation(summary = "Unbooks days for a worker")
    void unbookDays(@PathVariable Long workerId, @RequestBody UnavailabilityRange uRange) {
        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        LocalDate start = uRange.getUnavailabilityStartDate();
        LocalDate end = uRange.getUnavailabilityEndDate();

        if (start.isAfter(end)) {
            throw new InvaildDateRangeException();
        }

        start.datesUntil(end.plusDays(1))
            .forEach(uDate -> {
                Unavailability unavailability = uRepository.findByWorker_IdAndUnavailableDate(workerId,uDate)
                        .orElseThrow(AvailableDateException::new);
                uRepository.deleteById(unavailability.getId());
            });
    }
}