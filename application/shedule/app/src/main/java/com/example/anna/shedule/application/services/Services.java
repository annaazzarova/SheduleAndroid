package com.example.anna.shedule.application.services;

import com.example.anna.shedule.application.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

public class Services {

    private static Map<Class, Object> services = new HashMap<>();

    @SneakyThrows
    public static <T> T getService(Class<T> cs) {
        T instance = cs.cast(services.get(cs));
        if (instance == null) {
            instance = cs.newInstance();
            services.put(cs, instance);
        }
        return instance;
    }

    public static void clear() {
        services.clear();
    }
}
