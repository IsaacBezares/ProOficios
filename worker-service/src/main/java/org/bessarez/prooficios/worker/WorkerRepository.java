package org.bessarez.prooficios.worker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Optional<WorkerRatingInfo> findRatingInfoById(Long id);
}
