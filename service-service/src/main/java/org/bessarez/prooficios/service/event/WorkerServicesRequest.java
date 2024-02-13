package org.bessarez.prooficios.service.event;

import java.util.List;

public class WorkerServicesRequest {
    private Long workerId;
    private List<Long> servicesIds;

    public WorkerServicesRequest() {
    }

    public WorkerServicesRequest(Long workerId, List<Long> servicesIds) {
        this.workerId = workerId;
        this.servicesIds = servicesIds;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public List<Long> getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(List<Long> servicesIds) {
        this.servicesIds = servicesIds;
    }

    @Override
    public String toString() {
        return "WorkerServicesRequest{" +
                "workerId=" + workerId +
                ", servicesIds=" + servicesIds +
                '}';
    }
}
