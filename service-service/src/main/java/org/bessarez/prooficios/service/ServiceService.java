package org.bessarez.prooficios.service;

import jakarta.transaction.Transactional;
import org.bessarez.prooficios.service.event.WorkerServicesRequest;
import org.bessarez.prooficios.serviceworker.ServiceWorker;
import org.bessarez.prooficios.serviceworker.ServiceWorkerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    private final ServiceRepository sRepository;
    private final ServiceWorkerRepository swRepository;
    private final ServiceModelAssembler assembler;
    private final KafkaTemplate<String, Long> serviceUpdatedTemplate;

    public ServiceService(ServiceRepository sRepository, ServiceWorkerRepository swRepository, ServiceModelAssembler assembler, KafkaTemplate<String, Long> serviceUpdatedTemplate) {
        this.sRepository = sRepository;
        this.swRepository = swRepository;
        this.assembler = assembler;
        this.serviceUpdatedTemplate = serviceUpdatedTemplate;
    }

    CollectionModel<EntityModel<Service>> all() {
        List<EntityModel<Service>> serviceCategories = sRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(serviceCategories,
                linkTo(methodOn(ServiceController.class).all()).withSelfRel());

    }

    EntityModel<Service> one(Long id) {

        Service service = sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        return assembler.toModel(service);
    }

    Service create(Service newS) {
        return sRepository.save(newS);
    }

    Service update(Long id, Service newS) {
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

    void delete(Long id) {
        sRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));

        sRepository.deleteById(id);

        serviceUpdatedTemplate.send("serviceDeletedTopic", id);
    }

    @KafkaListener(topics = "servicesAddedToWorkerTopic")
    public void handleServicesAddedToWorker(WorkerServicesRequest wsr) {

        List<ServiceWorker> serviceWorkers = wsr.getServicesIds().stream()
                .map(serviceId -> new ServiceWorker(wsr.getWorkerId(), new Service(serviceId)))
                .toList();

        swRepository.saveAll(serviceWorkers);
    }

    @KafkaListener(topics = "servicesRemovedFromWorkerTopic")
    public void handleServicesRemovedFromWorker(WorkerServicesRequest wsr) {
        List<Long> servicesToRemove = swRepository.findAllByWorkerId(wsr.getWorkerId()).stream()
                .filter(serviceWorker -> wsr.getServicesIds().contains(serviceWorker.getService().getId()))
                .map(ServiceWorker::getId)
                .toList();

        swRepository.deleteAllById(servicesToRemove);
    }

    @KafkaListener(topics = "workerDeletedTopic")
    public void handleWorkerDeleted(Long workerId) {
        swRepository.deleteAllByWorkerId(workerId);
    }
}
