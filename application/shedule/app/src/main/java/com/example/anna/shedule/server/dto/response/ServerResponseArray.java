package com.example.anna.shedule.server.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServerResponseArray<T> extends ServerResponse<List<T>> {

    @JsonCreator
    public ServerResponseArray(@JsonProperty("message") String message,
                               @JsonProperty("response") List<T> response,
                               @JsonProperty("code") int code) {
        super(message, response, code);
    }

    public ServerResponseArray(int code) {
        super(code);
    }
}
