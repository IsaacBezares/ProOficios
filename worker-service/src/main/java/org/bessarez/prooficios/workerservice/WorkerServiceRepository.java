package org.bessarez.prooficios.workerservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface WorkerServiceRepository extends JpaRepository<WorkerService, Long> {
    ArrayList<WorkerService> findAllByWorker_Id(Long workerId);
    ArrayList<WorkerService> findAllByServiceId(Long serviceId);
    void deleteAllByServiceId(Long serviceId);
    void deleteByWorker_IdAndServiceId(Long workerId, Long serviceId);

}
