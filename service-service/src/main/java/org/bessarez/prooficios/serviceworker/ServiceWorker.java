package org.bessarez.prooficios.serviceworker;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.service.Service;

import java.util.Objects;

@Entity
@Table(name = "pro_service_worker")
public class ServiceWorker {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long workerId;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Service service;

    public ServiceWorker() {
    }

    public ServiceWorker(Long workerId, Service service) {
        this.workerId = workerId;
        this.service = service;
    }

    public ServiceWorker(Long id, Long workerId, Service service) {
        this.id = id;
        this.workerId = workerId;
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceWorker that = (ServiceWorker) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ServiceWorker{" +
                "id=" + id +
                ", workerId=" + workerId +
                ", service=" + service +
                '}';
    }
}
