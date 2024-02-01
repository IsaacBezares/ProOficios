package com.bessarez.prooficios.review;

import com.bessarez.prooficios.worker.Worker;
import com.bessarez.prooficios.order.Order;
import com.bessarez.prooficios.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

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

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Worker worker;

    //throws No _valueDeserializer exception only on first execution if I use @JsonIncludeProperties
    @JsonIgnoreProperties(value = {"orderStatus","orderDate","user","worker","review"}, allowSetters = true)
    @OneToOne
    @JoinColumn(nullable = false)
    private Order order;

    public Review() {
    }

    public Review(Long id, Double rating, String comment, LocalDate date, User user, Worker worker, Order order) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.worker = worker;
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
                ", rating='" + rating + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", worker=" + worker +
                ", order=" + order +
                '}';
    }
}
