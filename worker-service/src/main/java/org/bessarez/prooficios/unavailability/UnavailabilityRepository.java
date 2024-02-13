package org.bessarez.prooficios.unavailability;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UnavailabilityRepository extends JpaRepository<Unavailability,Long> {
    List<DateOnly> findAllByWorker_Id(Long workerId);
    Optional<Unavailability> findByWorker_IdAndUnavailableDate(Long workerId, LocalDate date);
    List<Unavailability> findByUnavailableDate(LocalDate date);
}
