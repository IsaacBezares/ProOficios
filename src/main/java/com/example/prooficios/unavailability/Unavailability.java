package com.example.prooficios.unavailability;

import com.example.prooficios.worker.Worker;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "pro_unavailability")
@Entity
public class Unavailability {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDate unavailableDate;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Worker worker;

    public Unavailability() {
    }

    public Unavailability(Long id, LocalDate unavailableDate, Worker worker) {
        this.id = id;
        this.unavailableDate = unavailableDate;
        this.worker = worker;
    }

    public Unavailability(LocalDate unavailableDate, Worker worker) {
        this.unavailableDate = unavailableDate;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
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
        Unavailability that = (Unavailability) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Unavailability{" +
                "id=" + id +
                ", unavailableDate=" + unavailableDate +
                ", worker=" + worker +
                '}';
    }
}
