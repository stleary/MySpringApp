package com.example;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestExample {

    private SimpleGreetingService simpleGreetingService;

    @Before
    public void setUp() throws Exception {
        simpleGreetingService = new SimpleGreetingService();

        Field prefixField = SimpleGreetingService.class.getDeclaredField("prefix");
        prefixField.setAccessible(true);
        prefixField.set(simpleGreetingService, "Hello");
    }

    // --- Existing controller test ---

    @Test
    public void testIndexReturnsGreeting() {
        Example example = new Example(simpleGreetingService, new TimeService());
        String result = example.index();
        assertEquals("Greetings from Spring Boot! /", result);
    }

    // --- GreetingService unit tests ---

    @Test
    public void testGreetWithName() {
        String result = simpleGreetingService.greet("Alice");
        assertEquals("Hello, Alice!", result);
    }

    @Test
    public void testGreetWithEmptyName() {
        String result = simpleGreetingService.greet("");
        assertEquals("Hello, World!", result);
    }

    @Test
    public void testGreetWithNull() {
        String result = simpleGreetingService.greet(null);
        assertEquals("Hello, World!", result);
    }

    // --- Controller greeting endpoint tests ---

    @Test
    public void testGreetingEndpointWithName() {
        Example example = new Example(simpleGreetingService, new TimeService());
        String result = example.index("Bob");
        assertEquals("Hello, Bob! (currentTime)", result);
    }

    @Test
    public void testGreetingEndpointWithEmptyName() {
        Example example = new Example(simpleGreetingService, new TimeService());
        String result = example.index("");
        assertEquals("Hello, World! (currentTime)", result);
    }
}