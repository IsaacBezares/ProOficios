package com.bessarez.prooficios.worker;

import com.bessarez.prooficios.workerimage.WorkerImage;
import com.bessarez.prooficios.order.Order;
import com.bessarez.prooficios.service.Service;
import com.bessarez.prooficios.unavailability.Unavailability;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "pro_worker")
@Entity
public class Worker {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String profilePicture;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    private Double rating;

    private Integer noReviews;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false, length = 3000)
    private String description;

    @JsonIncludeProperties({"id","title"})
    @ManyToMany
    @JoinTable(
            name = "pro_worker_service",
            joinColumns = @JoinColumn(name = "worker_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    Set<Service> workerServices = new HashSet<>();

    @JsonIgnoreProperties("id")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker", orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    @JsonIncludeProperties({"id","unavailableDate"})
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker", orphanRemoval = true)
    private Set<Unavailability> unavailability = new HashSet<>();

    @JsonIncludeProperties({"id","imageURL"})
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "worker", orphanRemoval = true)
    private Set<WorkerImage> workerImages = new HashSet<>();

    public Worker() {
    }

    public Worker(Long id, String name, String profilePicture, String phone, String email, Double rating, Integer noReviews, LocalDate joinDate, String description, Set<Service> workerServices, Set<Order> orders, Set<Unavailability> unavailability, Set<WorkerImage> workerImages) {
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.noReviews = noReviews;
        this.joinDate = joinDate;
        this.description = description;
        this.workerServices = workerServices;
        this.orders = orders;
        this.unavailability = unavailability;
        this.workerImages = workerImages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getNoReviews() {
        return noReviews;
    }

    public void setNoReviews(Integer noReviews) {
        this.noReviews = noReviews;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Service> getWorkerServices() {
        return workerServices;
    }

    public void setWorkerServices(Set<Service> workerServices) {
        this.workerServices = workerServices;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orderProducts) {
        this.orders = orderProducts;
    }

    public Set<Unavailability> getUnavailability() {
        return unavailability;
    }

    public void setUnavailability(Set<Unavailability> unavailability) {
        this.unavailability = unavailability;
    }

    public Set<WorkerImage> getWorkerImages() {
        return workerImages;
    }

    public void setWorkerImages(Set<WorkerImage> workerImages) {
        this.workerImages = workerImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id.equals(worker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", rating='" + rating + '\'' +
                ", noReviews=" + noReviews +
                ", joinDate=" + joinDate +
                ", description='" + description + '\'' +
                ", workerServices=" + workerServices +
                ", orders=" + orders +
                ", unavailability=" + unavailability +
                ", workerImages=" + workerImages +
                '}';
    }
}