package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class GreetingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GreetingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<GreetingModel> rowMapper = (rs, rowNum) -> {
        GreetingModel greetingModel = new GreetingModel();
        greetingModel.setId(rs.getLong("id"));
        greetingModel.setName(rs.getString("name"));
        greetingModel.setMessage(rs.getString("message"));
        greetingModel.setCreatedAt(rs.getTimestamp("created_at")
                .toLocalDateTime());
        return greetingModel;
    };

    public List<GreetingModel> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM greetings", rowMapper);
    }

    /**
     * lookup in db by name
     * @param name the name to look up
     * @return Optional object, which may or may not contain a GreetingModel
     * Use isPresent() for presence, and get() if presence is true
     */
    public Optional<GreetingModel> findByName(String name) {
        List<GreetingModel> results = jdbcTemplate.query(
                "SELECT * FROM greetings WHERE name = ?",
                rowMapper, name);
        return results.isEmpty() ?
                Optional.empty() : Optional.of(results.get(0));
    }

    public void save(GreetingModel greetingModel) {
        jdbcTemplate.update(
                "INSERT INTO greetings (name, message) VALUES (?, ?)",
                greetingModel.getName(), greetingModel.getMessage());
    }
}