package com.example.anna.shedule.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("login")
    private String login;

    @JsonProperty("hash")
    private String teacherId;

    @JsonProperty
    private boolean isStudent;

}
