package org.bessarez.prooficios.workerservice;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.worker.Worker;

import java.util.Objects;

@Entity
@Table(name = "pro_worker_service")
public class WorkerService {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long serviceId;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Worker worker;

    public WorkerService() {
    }

    public WorkerService(Long serviceId, Worker worker) {
        this.serviceId = serviceId;
        this.worker = worker;
    }

    public WorkerService(Long id, Long serviceId, Worker worker) {
        this.id = id;
        this.serviceId = serviceId;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerService that = (WorkerService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "WorkerService{" +
                "id=" + id +
                ", serviceId=" + serviceId +
                ", worker=" + worker +
                '}';
    }
}
