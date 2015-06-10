package com.example.anna.shedule.application.schedule.dto;

import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateNewLessonRequest extends CreateChangeByLessonRequest {

    @JsonProperty
    private String teacherId;

    @JsonProperty
    private List<String> groups;

    @JsonProperty
    private int dayOfWeek;

    @JsonProperty
    private int time;

    @JsonProperty
    private String week;



    public CreateNewLessonRequest(Change change) {
        super(change);

        this.teacherId = change.getTeacherId();
        this.groups = toGroupArray(change.getGroupIds());
        this.dayOfWeek = change.getDayOfWeek();
        if (change.getTime() != null) this.time = change.getTime().getId();
        if (change.getWeekPeriodicity() != null) this.week = change.getWeekPeriodicity().getAsString();
    }

    private List<String> toGroupArray(String groupIds) {
        if (groupIds != null) {
            return StringUtils.split(groupIds, ',');
        }
        return new ArrayList<String>(0);
    }

}
