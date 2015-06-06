package com.example.anna.shedule.application.schedule.dto;

import com.example.anna.shedule.application.schedule.model.Change;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateChangeRequest {

//    title: {checker: notEmpty, required: false},
//    hull: {checker: isHull, required: false},
//    auditory: {checker: isAuditory, required: false},
//    dayOfWeek: {checker: alwaysFalse, required: false},
//    time: {checker: alwaysFalse, required: false},
//    week: {checker: alwaysFalse, required: false},
//    type: {checker: isValidType, required: false},
//    durationFrom: {checker: alwaysFalse, required: false},
//    status: {checker: isValidStatus,  required: false}


    public UpdateChangeRequest(Change change) {
    }
}
