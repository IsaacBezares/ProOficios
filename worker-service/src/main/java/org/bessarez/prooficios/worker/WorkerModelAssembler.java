package org.bessarez.prooficios.worker;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkerModelAssembler implements RepresentationModelAssembler<Worker, EntityModel<Worker>> {

    @Override
    public EntityModel<Worker> toModel(Worker worker) {
        return EntityModel.of(worker,
                linkTo(methodOn(WorkerController.class).one(worker.getId())).withSelfRel(),
                linkTo(methodOn(WorkerController.class).all()).withRel("workers"));
    }
}
