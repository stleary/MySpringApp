package com.example;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
    public String getTime() {
        return " (currentTime)";
    }
}
