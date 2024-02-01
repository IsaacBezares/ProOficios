package com.bessarez.prooficios.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findAllByWorker_Id(Long workerId);
    List<Review> findAllByUser_Id(Long userId);
}
