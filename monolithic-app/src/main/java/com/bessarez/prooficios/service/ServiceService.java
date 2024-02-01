package com.bessarez.prooficios.service;

import com.bessarez.prooficios.worker.WorkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    private final ServiceRepository sRepository;
    private final WorkerRepository wRepository;
    private final ServiceModelAssembler assembler;

    public ServiceService(ServiceRepository sRepository, WorkerRepository wRepository, ServiceModelAssembler assembler) {
        this.sRepository = sRepository;
        this.wRepository = wRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<Service>> all() {
        List<EntityModel<Service>> serviceCategories = sRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(serviceCategories,
                linkTo(methodOn(ServiceController.class).all()).withSelfRel());

    }

    EntityModel<Service> one(Long id){

        Service service = sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        return assembler.toModel(service);
    }

    Service create(Service newS){
        return sRepository.save(newS);
    }

    Service update(Long id, Service newS){
        return sRepository.findById(id)
                .map(service -> {
                    service.setTitle(newS.getTitle());
                    return sRepository.save(service);
                })
                .orElseGet(() -> {
                    newS.setId(id);
                    return sRepository.save(newS);
                });
    }

    void delete(Long id){
        Service service = sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        service.getWorkers().forEach(w -> w.getWorkerServices().remove(service));
        wRepository.saveAll(service.getWorkers());

        sRepository.deleteById(id);
    }
}
