package com.example.prooficios.workerimage;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkerImageModelAssembler implements RepresentationModelAssembler<WorkerImage, EntityModel<WorkerImage>> {

    @Override
    public EntityModel<WorkerImage> toModel(WorkerImage workerImage) {
        return EntityModel.of(workerImage,
                linkTo(methodOn(WorkerImageController.class).one(workerImage.getId())).withSelfRel(),
                linkTo(methodOn(WorkerImageController.class).all()).withRel("worker_images"));
    }
}
