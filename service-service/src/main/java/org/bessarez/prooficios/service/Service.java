package org.bessarez.prooficios.service;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.serviceworker.ServiceWorker;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "pro_service")
@Entity
public class Service {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @JsonIncludeProperties("workerId")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "service", orphanRemoval = true)
    private Set<ServiceWorker> serviceWorkers = new HashSet<>();

    public Service() {
    }

    public Service(Long id) {
        this.id = id;
    }

    public Service(Long id, String title, Set<ServiceWorker> serviceWorkers) {
        this.id = id;
        this.title = title;
        this.serviceWorkers = serviceWorkers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<ServiceWorker> getServiceWorkers() {
        return serviceWorkers;
    }

    public void setServiceWorkers(Set<ServiceWorker> serviceWorkers) {
        this.serviceWorkers = serviceWorkers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service that = (Service) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", serviceWorkers=" + serviceWorkers +
                '}';
    }
}
