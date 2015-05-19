package com.example.anna.shedule.server.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseWithStatusCode {

    public static final int NO_INTERNET_ERROR = -1;

    private int code;
    private String response;

    public ResponseWithStatusCode(int code, InputStream content) throws IOException {
        this.code = code;
        this.response = IOUtils.toString(content);
    }

    public ResponseWithStatusCode(int code) {
        this.code = code;
    }
}
