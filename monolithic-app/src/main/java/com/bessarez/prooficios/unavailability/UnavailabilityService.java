package com.bessarez.prooficios.unavailability;

import com.bessarez.prooficios.worker.Worker;
import com.bessarez.prooficios.worker.WorkerNotFoundException;
import com.bessarez.prooficios.worker.WorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class UnavailabilityService {
    private final UnavailabilityRepository uRepository;
    private final WorkerRepository wRepository;

    private final UnavailabilityModelAssembler assembler;

    public UnavailabilityService(UnavailabilityRepository uRepository, WorkerRepository wRepository, UnavailabilityModelAssembler assembler) {
        this.uRepository = uRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<Unavailability>> all() {

        List<EntityModel<Unavailability>> unavailabilities = uRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(unavailabilities,
                linkTo(methodOn(UnavailabilityController.class).all()).withSelfRel());
    }

    List<LocalDate> allByWorker(Long workerId) {

        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        return uRepository.findAllByWorker_Id(workerId).stream()
                .map(DateOnly::getUnavailableDate)
                .toList();

    }

    EntityModel<Unavailability> one(Long id) {

        Unavailability unavailability = uRepository.findById(id)
                .orElseThrow(() -> new UnavailabilityNotFoundException(id));

        return assembler.toModel(unavailability);
    }

    Unavailability create(Unavailability unavailability) {
        return uRepository.save(unavailability);
    }

    List<Unavailability> bookDays(Long workerId, UnavailabilityRange uRange) {

        Worker worker = wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

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

    Unavailability update(Long id, Unavailability newU) {
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

    void delete(Long id) {
        uRepository.findById(id)
                .orElseThrow(() -> new UnavailabilityNotFoundException(id));

        uRepository.deleteById(id);
    }

    void unbookDays(Long workerId, UnavailabilityRange uRange) {
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
