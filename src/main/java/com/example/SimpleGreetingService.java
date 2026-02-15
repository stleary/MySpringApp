package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SimpleGreetingService implements GreetingService {

    @Value("${greeting.prefix:Hello}")
    String prefix;

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return prefix + ", World!";
        }
        return prefix + ", " + name + "!";
    }
}


