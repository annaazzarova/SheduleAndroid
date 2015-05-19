package com.example.anna.shedule.server.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServerResponse<T> {

    public static final int SUCCESS_RESPONSE_CODE = 0;
    public static final int NO_CONNECTION_ERROR = -0xfe;
    public static final int PARSE_RESPONSE_ERROR = -0xff;

    private int code;
    private String message;
    private T response;

    public ServerResponse(int code) {
        this.code = code;
        this.message = "";
    }

    @JsonCreator
    public ServerResponse(@JsonProperty("message") String message,
                          @JsonProperty("response") T response,
                          @JsonProperty("code") int code) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public boolean isSuccess() {
        return code == SUCCESS_RESPONSE_CODE;
    }

}