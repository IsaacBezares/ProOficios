package com.bessarez.prooficios.workerimage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerImageRepository extends JpaRepository<WorkerImage,Long> {

    void deleteAllByWorker_Id(Long workerId);
    List<WorkerImage> findAllByWorker_Id(Long workerId);
}
