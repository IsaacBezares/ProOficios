package org.bessarez.prooficios.serviceworker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceWorkerRepository extends JpaRepository<ServiceWorker, Long> {
    void deleteAllByWorkerId(Long workerId);

    List<ServiceWorker> findAllByWorkerId(Long workerId);
}
