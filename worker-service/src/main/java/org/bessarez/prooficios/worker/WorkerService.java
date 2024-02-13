package org.bessarez.prooficios.worker;

import com.fasterxml.jackson.databind.node.TextNode;
import org.bessarez.prooficios.worker.event.WorkerServicesRequest;
import org.bessarez.prooficios.workerservice.WorkerServiceRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class WorkerService {
    private final WorkerRepository wRepository;
    private final WorkerServiceRepository wsRepository;
    private final WorkerModelAssembler assembler;
    private final KafkaTemplate<String, WorkerServicesRequest> wsUpdatedTemplate;
    private final KafkaTemplate<String, Long> wUpdatedTemplate;

    public WorkerService(WorkerRepository wRepository, WorkerServiceRepository wsRepository, WorkerModelAssembler assembler, KafkaTemplate<String, WorkerServicesRequest> wsUpdatedTemplate, KafkaTemplate<String, Long> wUpdatedTemplate) {
        this.wRepository = wRepository;
        this.wsRepository = wsRepository;
        this.assembler = assembler;
        this.wsUpdatedTemplate = wsUpdatedTemplate;
        this.wUpdatedTemplate = wUpdatedTemplate;
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

    WorkerRatingInfo getWorkerRatingInfo(Long id) {
        WorkerRatingInfo worker = wRepository.findRatingInfoById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        return worker;
    }

    Worker create(Worker newW) {
        newW.setNoReviews(0);
        Worker worker = wRepository.save(newW);
        wUpdatedTemplate.send("workerCreatedTopic", worker.getId());
        return worker;
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

    Worker updateRatingInfo(Long id, Worker newW){
        return wRepository.findById(id)
                .map(worker -> {
                    worker.setRating(newW.getRating());
                    worker.setNoReviews(newW.getNoReviews());
                    return wRepository.save(worker);
                })
                .orElseThrow(() -> new WorkerNotFoundException(id));
    }

    void delete(Long id) {
        wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        wRepository.deleteById(id);
        wUpdatedTemplate.send("workerDeletedTopic", id);
    }

    Worker addServices(Long id, List<Long> newServices) {
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        List<org.bessarez.prooficios.workerservice.WorkerService> newWorkerServices = newServices.stream()
                .map(serviceId -> new org.bessarez.prooficios.workerservice.WorkerService(serviceId, worker))
                .toList();

        wsRepository.saveAll(newWorkerServices);

        wsUpdatedTemplate.send("servicesAddedToWorkerTopic", new WorkerServicesRequest(id, newServices));
        return worker;
    }

    Worker removeServices(Long id, List<Long> services) {
        Worker worker = wRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        services.forEach(serviceId -> {
            wsRepository.deleteByWorker_IdAndServiceId(id, serviceId);
        });

        wsUpdatedTemplate.send("servicesRemovedFromWorkerTopic", new WorkerServicesRequest(id, services));

        return worker;
    }

    @KafkaListener(topics = "serviceDeletedTopic")
    public void handleServiceDeleted(Long serviceId) {
        wsRepository.deleteAllByServiceId(serviceId);
    }
}