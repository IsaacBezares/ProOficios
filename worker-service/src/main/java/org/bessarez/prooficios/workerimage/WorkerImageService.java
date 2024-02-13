package org.bessarez.prooficios.workerimage;

import org.bessarez.prooficios.worker.Worker;
import org.bessarez.prooficios.worker.WorkerNotFoundException;
import org.bessarez.prooficios.worker.WorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class WorkerImageService {
    private final WorkerImageRepository wiRepository;
    private final WorkerRepository wRepository;
    private final WorkerImageModelAssembler assembler;

    public WorkerImageService(WorkerImageRepository wiRepository, WorkerRepository wRepository, WorkerImageModelAssembler assembler) {
        this.wiRepository = wiRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<WorkerImage>> all() {
        List<EntityModel<WorkerImage>> workerImages = wiRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workerImages,
                linkTo(methodOn(WorkerImageController.class).all()).withSelfRel());
    }

    CollectionModel<EntityModel<WorkerImage>> allByWorker(Long workerId) {
        List<EntityModel<WorkerImage>> workerImages = wiRepository.findAllByWorker_Id(workerId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workerImages,
                linkTo(methodOn(WorkerImageController.class).allByWorker(workerId)).withSelfRel());
    }

    EntityModel<WorkerImage> one(Long id) {
        WorkerImage workerImage = wiRepository.findById(id)
                .orElseThrow(() -> new WorkerImageNotFoundException(id));

        return assembler.toModel(workerImage);
    }

    List<WorkerImage> addImages(Long workerId, List<WorkerImage> newImages) {
        Worker worker = wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        newImages.forEach(workerImage -> workerImage.setWorker(worker));

        return wiRepository.saveAll(newImages);
    }

    WorkerImage update(Long id, WorkerImage newWI) {
        return wiRepository.findById(id)
                .map(workerImage -> {
                    workerImage.setImageURL(newWI.getImageURL());
                    return wiRepository.save(workerImage);
                })
                .orElseGet(() -> {
                    newWI.setId(id);
                    return wiRepository.save(newWI);
                });
    }

    void delete(Long id) {
        wiRepository.findById(id)
                .orElseThrow(() -> new WorkerImageNotFoundException(id));

        wiRepository.deleteById(id);
    }

    void deleteAll(Long workerId) {
        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        wiRepository.deleteAllByWorker_Id(workerId);
    }
}
