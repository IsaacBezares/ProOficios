package org.bessarez.prooficios.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findAllByWorker_Id(Long workerId);
    List<Review> findAllByUser_Id(Long userId);
    Review findByOrderId(Long orderId);
}
