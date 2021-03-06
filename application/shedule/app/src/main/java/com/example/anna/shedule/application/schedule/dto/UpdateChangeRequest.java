package com.example.anna.shedule.application.schedule.dto;

import com.example.anna.shedule.application.schedule.model.Change;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateChangeRequest {

    @JsonProperty
    private String title;

    @JsonProperty
    private String hull;

    @JsonProperty
    private String auditory;

    @JsonProperty
    private String type;

    @JsonProperty
    private String status;

    public UpdateChangeRequest(Change change) {
        this.title = change.getTitle();
        this.hull = change.getHull();
        this.auditory = change.getAuditory();
        if (change.getType() != null) this.type = change.getType().getId();
        if (change.getStatus() != null) this.status = change.getStatus().getId();
    }
}
