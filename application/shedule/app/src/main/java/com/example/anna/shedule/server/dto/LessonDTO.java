package com.example.anna.shedule.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LessonDTO {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("number")
    private int lessonNumber;

    @JsonProperty("week")
    private String weekType;

    @JsonProperty("lesson")
    private String lesson;

    @JsonProperty
    private String teacher;

    @JsonProperty
    private String auditory;

    @JsonProperty
    private String hull;

    @JsonProperty
    private int day;

}
