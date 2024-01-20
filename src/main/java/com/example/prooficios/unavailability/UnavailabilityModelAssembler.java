package com.example.prooficios.unavailability;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UnavailabilityModelAssembler implements RepresentationModelAssembler<Unavailability, EntityModel<Unavailability>>{
    @Override
    public EntityModel<Unavailability> toModel(Unavailability unavailability){
        return EntityModel.of(unavailability,
                linkTo(methodOn(UnavailabilityController.class).one(unavailability.getId())).withSelfRel(),
                linkTo(methodOn(UnavailabilityController.class).all()).withRel("unavailability"));
    }
}
