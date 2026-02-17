package com.example;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestExample {

    private SimpleGreetingService simpleGreetingService;
    private GreetingService greetingService;
    private FakeGreetingRepository fakeRepository;


    @Before
    public void setUp() throws Exception {
        simpleGreetingService = new SimpleGreetingService();

        Field prefixField = SimpleGreetingService.class.getDeclaredField("prefix");
        prefixField.setAccessible(true);
        prefixField.set(simpleGreetingService, "Hello");

        fakeRepository = new FakeGreetingRepository();
        greetingService = new FormalGreetingService(fakeRepository);
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

    @Test
    public void testGreetDBWithStoredMessage() {
        fakeRepository.setStoredGreeting("Alice",
                "Welcome back, Alice!");
        String result = greetingService.greet("Alice");
        assertEquals("Welcome back, Alice!", result);
    }

    @Test
    public void testGreetDBWithNoStoredMessage() {
        String result = greetingService.greet("Charlie");
        assertEquals("Hello, Charlie!", result);
    }

    @Test
    public void testGreetDBWithEmptyName() {
        fakeRepository.setStoredGreeting("World",
                "Hello, World!");
        String result = greetingService.greet("");
        assertEquals("Hello, World!", result);
    }

    // Simple fake for testing
    static class FakeGreetingRepository extends GreetingRepository {
        private String storedName;
        private String storedMessage;
        public FakeGreetingRepository() {
            super(null); // No real JdbcTemplate needed
        }

        public void setStoredGreeting(String name, String message) {
            this.storedName = name;
            this.storedMessage = message;
        }

        @Override
        public Optional<GreetingModel> findByName(String name) {
            if (name.equals(storedName)) {
                GreetingModel greetingModel = new GreetingModel();
                greetingModel.setName(storedName);
                greetingModel.setMessage(storedMessage);
                return Optional.of(greetingModel);
            }
            return Optional.empty();
        }
    }


}