package com.example.prooficios.workerimage;

import com.example.prooficios.worker.Worker;
import com.example.prooficios.worker.WorkerNotFoundException;
import com.example.prooficios.worker.WorkerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Worker Images")
public class WorkerImageController {

    private final WorkerImageRepository wiRepository;
    private final WorkerRepository wRepository;
    private final WorkerImageModelAssembler assembler;

    public WorkerImageController(WorkerImageRepository wiRepository, WorkerRepository wRepository, WorkerImageModelAssembler assembler) {
        this.wiRepository = wiRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    @GetMapping("/workerimages")
    @Operation(summary = "Returns all worker images")
    CollectionModel<EntityModel<WorkerImage>> all(){
        List<EntityModel<WorkerImage>> workerImages = wiRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workerImages,
                linkTo(methodOn(WorkerImageController.class).all()).withSelfRel());
    }

    @GetMapping("/workerimages/worker/{workerId}")
    @Operation(summary = "Returns all worker images by worker id")
    CollectionModel<EntityModel<WorkerImage>> allByWorker(@PathVariable Long workerId){
        List<EntityModel<WorkerImage>> workerImages = wiRepository.findAllByWorker_Id(workerId).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workerImages,
                linkTo(methodOn(WorkerImageController.class).allByWorker(workerId)).withSelfRel());
    }

    @GetMapping("/workerimages/{id}")
    @Operation(summary = "Returns one worker by id")
    EntityModel<WorkerImage> one(@PathVariable Long id){
        WorkerImage workerImage = wiRepository.findById(id)
                .orElseThrow(() -> new WorkerImageNotFoundException(id));

        return assembler.toModel(workerImage);
    }

    @PostMapping("/workerimages/add/{workerId}")
    @Operation(summary = "Adds multiple images to a worker")
    List<WorkerImage> addImages(@PathVariable Long workerId, @RequestBody List<WorkerImage> newImages){
        Worker worker = wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        newImages.forEach(workerImage -> workerImage.setWorker(worker));

        return wiRepository.saveAll(newImages);
    }

    @PutMapping("/workerimages/{id}")
    @Operation(summary = "Updates a worker image")
    WorkerImage update(@PathVariable Long id, @RequestBody WorkerImage newWI){
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

    @DeleteMapping("/workerimages/{id}")
    @Operation(summary = "Remove a worker image by its id")
    void delete(@PathVariable Long id){
        wiRepository.findById(id)
                .orElseThrow(() -> new WorkerImageNotFoundException(id));

        wiRepository.deleteById(id);
    }

    @Transactional
    @DeleteMapping("/workerimages/deleteAll/{workerId}")
    @Operation(summary = "Remove all worker's images by worker id")
    void deleteAll(@PathVariable Long workerId){
        wRepository.findById(workerId)
                .orElseThrow(() -> new WorkerNotFoundException(workerId));

        wiRepository.deleteAllByWorker_Id(workerId);
    }
}
