package org.bessarez.prooficios.dto;

public class WorkerRatingInfo {

    private Long id;
    private Double rating;
    private Integer noReviews;

    public WorkerRatingInfo() {
    }

    public WorkerRatingInfo(Long id, Double rating, Integer noReviews) {
        this.id = id;
        this.rating = rating;
        this.noReviews = noReviews;
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

    public Integer getNoReviews() {
        return noReviews;
    }

    public void setNoReviews(Integer noReviews) {
        this.noReviews = noReviews;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", rating=" + rating +
                ", noReviews=" + noReviews +
                '}';
    }
}