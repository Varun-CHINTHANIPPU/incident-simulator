package com.simulator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads scenario configuration from JSON files
 */
public class ConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Load scenario from a file path
     */
    public static ScenarioConfig loadFromFile(String filePath) throws IOException {
        logger.info("Loading scenario from file: {}", filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Scenario file not found: " + filePath);
        }

        ScenarioConfig config = objectMapper.readValue(file, ScenarioConfig.class);
        logger.info("Successfully loaded scenario: {}", config.getScenarioId());
        return config;
    }

    /**
     * Load scenario from classpath resources
     */
    public static ScenarioConfig loadFromResource(String resourcePath) throws IOException {
        logger.info("Loading scenario from resource: {}", resourcePath);

        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        ScenarioConfig config = objectMapper.readValue(inputStream, ScenarioConfig.class);
        logger.info("Successfully loaded scenario: {}", config.getScenarioId());
        return config;
    }

    /**
     * Load scenario from JSON string
     */
    public static ScenarioConfig loadFromJson(String json) throws IOException {
        logger.info("Loading scenario from JSON string");
        ScenarioConfig config = objectMapper.readValue(json, ScenarioConfig.class);
        logger.info("Successfully loaded scenario: {}", config.getScenarioId());
        return config;
    }
}
