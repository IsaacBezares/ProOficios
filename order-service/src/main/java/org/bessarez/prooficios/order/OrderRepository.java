package org.bessarez.prooficios.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByWorker_Id(Long workerId);

    List<Order> findAllByUser_Id(Long userId);
}
