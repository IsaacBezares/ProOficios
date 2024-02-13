package org.bessarez.prooficios.order;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import org.bessarez.prooficios.user.User;
import org.bessarez.prooficios.worker.Worker;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "pro_order")
@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private LocalDate orderDate;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @JsonIncludeProperties("id")
    @ManyToOne
    @JoinColumn(nullable = false)
    private Worker worker;

    /*@JsonIncludeProperties("id")
    @OneToOne(mappedBy = "order")
    private Review review;*/


    public Order() {
    }

    public Order(Long id, String orderStatus, LocalDate orderDate, User user, Worker worker) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.user = user;
        this.worker = worker;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
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
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate=" + orderDate +
                ", user=" + user +
                ", worker=" + worker +
                '}';
    }
}
