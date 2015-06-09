package com.example.anna.shedule.application.schedule.dto;

import com.example.anna.shedule.application.schedule.model.Change;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateChangeByLessonRequest {

    @JsonProperty
    private String title;

    @JsonProperty
    private String hull;

    @JsonProperty
    private String auditory;

    @JsonProperty
    private String type;

    @JsonProperty
    private Date durationFrom;

    @JsonProperty
    private Date durationTo;

    @JsonProperty
    private String status;

    public CreateChangeByLessonRequest(Change change) {
        this.title = change.getTitle();
        this.hull = change.getHull();
        this.auditory = change.getAuditory();
        if (change.getType() != null) this.type = change.getType().getId();
        this.durationFrom = change.getDateFrom();
        this.durationTo = change.getDateTo();
        if (change.getStatus() != null) this.status = change.getStatus().getId();
    }
}
