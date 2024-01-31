package com.example.prooficios.worker;

import com.example.prooficios.service.Service;
import com.example.prooficios.service.ServiceNotFoundException;
import com.example.prooficios.service.ServiceRepository;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@org.springframework.stereotype.Service
@Transactional
public class WorkerService {
    private final WorkerRepository wRepository;
    private final ServiceRepository sRepository;
    private final WorkerModelAssembler assembler;

    public WorkerService(WorkerRepository wRepository, ServiceRepository sRepository, WorkerModelAssembler assembler) {
        this.wRepository = wRepository;
        this.sRepository = sRepository;
        this.assembler = assembler;
    }

    CollectionModel<EntityModel<Worker>> all() {
        List<EntityModel<Worker>> workers = wRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(workers,
                linkTo(methodOn(WorkerController.class).all()).withSelfRel());
    }

    EntityModel<Worker> one(Long id) {
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        return assembler.toModel(worker);
    }

    Worker create(Worker newW) {
        newW.setNoReviews(0);
        return wRepository.save(newW);
    }

    Worker addServices(Long id, List<Service> newServices) {
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        newServices.forEach(newService -> {
            sRepository.findById(newService.getId())
                    .orElseThrow(() -> new ServiceNotFoundException(newService.getId()));

            System.out.println(newService);

            worker.getWorkerServices().add(newService);
        });

        return wRepository.save(worker);
    }

    Worker update(Long id, Worker newW) {
        return wRepository.findById(id)
                .map(worker -> {
                    worker.setName(newW.getName());
                    worker.setEmail(newW.getEmail());
                    worker.setPhone(newW.getPhone());
                    worker.setJoinDate(newW.getJoinDate());
                    worker.setDescription(newW.getDescription());
                    return wRepository.save(worker);
                })
                .orElseGet(() -> {
                    newW.setId(id);
                    return wRepository.save(newW);
                });
    }

    Worker updateProfilePic(Long id, TextNode imageURL) {
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        worker.setProfilePicture(imageURL.asText());

        return wRepository.save(worker);
    }

    void delete(Long id) {
        wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        wRepository.deleteById(id);
    }

    Worker removeServices(Long id, List<Service> services) {

        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        services.forEach(service -> {
            worker.getWorkerServices().remove(service);
        });

        return wRepository.save(worker);

    }
}
