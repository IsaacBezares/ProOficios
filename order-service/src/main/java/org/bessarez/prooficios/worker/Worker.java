package org.bessarez.prooficios.worker;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.order.Order;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pro_worker")
public class Worker {
    @Id
    @Column(nullable = false)
    Long id;

    @JsonIncludeProperties("id")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker", orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    public Worker() {
    }

    public Worker(Long id) {
        this.id = id;
    }

    public Worker(Long id, Set<Order> orders) {
        this.id = id;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(id, worker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
