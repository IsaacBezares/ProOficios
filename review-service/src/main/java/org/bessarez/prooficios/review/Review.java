package org.bessarez.prooficios.review;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.user.User;
import org.bessarez.prooficios.worker.Worker;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "pro_review")
@Entity
public class Review {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Double rating;

    @Column(length = 2000)
    private String comment;
    @Column(nullable = false)
    private LocalDate date;

    @Column(unique = true, nullable = false)
    private Long orderId;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Worker worker;

    public Review() {
    }

    public Review(Long id, Double rating, String comment, LocalDate date, Long orderId, User user, Worker worker) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.orderId = orderId;
        this.user = user;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Review review = (Review) o;
        return id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", orderId=" + orderId +
                ", user=" + user +
                ", worker=" + worker +
                '}';
    }
}
