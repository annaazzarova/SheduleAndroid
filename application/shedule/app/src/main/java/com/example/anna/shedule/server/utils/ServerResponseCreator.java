package com.example.anna.shedule.server.utils;

import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerResponseCreator {

    @SuppressWarnings("all")
    public static <T> ServerResponse<T> convertToObjectResponse(ResponseWithStatusCode response, Class<T> cs) {
        ServerResponse<T> mappedResponse = JsonParser.fromJson(response.getResponse(), ServerResponse.class, cs);
        if (mappedResponse == null) {
            int code = getErrorCode(response);
            return new ServerResponse<T>(code);
        } else {
            return mappedResponse;
        }
    }

    @SuppressWarnings("all")
    public static <T> ServerResponseArray<T> convertToArrayResponse(ResponseWithStatusCode response, Class<T> cs) {
        ServerResponseArray<T> mappedResponse = JsonParser.fromJson(response.getResponse(), ServerResponseArray.class, cs);
        if (mappedResponse == null) {
            int code = getErrorCode(response);
            return new ServerResponseArray<T>(code);
        } else {
            return mappedResponse;
        }
    }

    private static int getErrorCode(ResponseWithStatusCode response) {
        int responseCode = response.getCode();
        if (responseCode == 200) {
            return ServerResponse.PARSE_RESPONSE_ERROR;
        } else if (responseCode == 0) {
            return ServerResponse.NO_CONNECTION_ERROR;
        } else {
            return responseCode;
        }
    }
}
