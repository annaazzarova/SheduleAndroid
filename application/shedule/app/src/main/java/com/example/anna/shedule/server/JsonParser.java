package com.example.anna.shedule.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser {

    private final static ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static byte[] toJson(Object o) {
        try {
            return MAPPER.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String jsonString, Class<T> type) {
        try {
            return MAPPER.readValue(jsonString, type);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJson(InputStream jsonStream, Class<T> parametrized, Class<?> parameter){
        JavaType javaType = MAPPER
                .getTypeFactory()
                .constructParametrizedType(parametrized, parametrized, parameter);
        try {
            return MAPPER.readValue(jsonStream, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
