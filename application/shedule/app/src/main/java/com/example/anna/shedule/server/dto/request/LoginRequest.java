package com.example.anna.shedule.server.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
}
