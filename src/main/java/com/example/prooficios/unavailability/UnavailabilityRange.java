package com.example.prooficios.unavailability;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class UnavailabilityRange {

    private LocalDate unavailabilityStartDate;

    private LocalDate unavailabilityEndDate;

    public UnavailabilityRange() {
    }

    public UnavailabilityRange(LocalDate unavailabilityStartDate, LocalDate unavailabilityEndDate) {
        this.unavailabilityStartDate = unavailabilityStartDate;
        this.unavailabilityEndDate = unavailabilityEndDate;
    }

    public LocalDate getUnavailabilityStartDate() {
        return unavailabilityStartDate;
    }

    public void setUnavailabilityStartDate(LocalDate unavailabilityStartDate) {
        this.unavailabilityStartDate = unavailabilityStartDate;
    }

    public LocalDate getUnavailabilityEndDate() {
        return unavailabilityEndDate;
    }

    public void setUnavailabilityEndDate(LocalDate unavailabilityEndDate) {
        this.unavailabilityEndDate = unavailabilityEndDate;
    }
}
