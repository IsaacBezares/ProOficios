package org.bessarez.prooficios.workerimage;

import org.bessarez.prooficios.worker.Worker;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "pro_worker_image")
@Entity
public class WorkerImage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "image_url")
    private String imageURL;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn
    private Worker worker;

    public WorkerImage() {
    }

    public WorkerImage(Long id, String imageURL, Worker worker) {
        this.id = id;
        this.imageURL = imageURL;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        WorkerImage that = (WorkerImage) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "WorkerImage{" +
                "id=" + id +
                ", imageURL='" + imageURL + '\'' +
                ", worker=" + worker +
                '}';
    }
}
