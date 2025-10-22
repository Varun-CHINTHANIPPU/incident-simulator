package com.simulator.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simulator.core.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Logs structured events in JSON format for SIEM ingestion
 */
public class EventLogger {

    private static final Logger logger = LoggerFactory.getLogger(EventLogger.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String LOG_FILE = "logs/simulation-events.json";

    private String scenarioId;
    private String runId;
    private BufferedWriter fileWriter;

    // Constructor
    public EventLogger(String scenarioId, String runId) {
        this.scenarioId = scenarioId;
        this.runId = runId;
        initializeLogFile();
        logger.info("Event Logger initialized for scenario: {}, run: {}", scenarioId, runId);
    }

    // Initialize log file
    private void initializeLogFile() {
        try {
            // Create logs directory if it doesn't exist
            Files.createDirectories(Paths.get("logs"));

            // Open file writer in append mode
            fileWriter = new BufferedWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            logger.error("Failed to initialize log file: {}", e.getMessage());
        }
    }

    // Log an attack event
    public void logAttackEvent(String eventType, String severity, String hostName, Map<String, Object> details) {
        Event event = new Event();
        event.setScenarioId(scenarioId);
        event.setRunId(runId);
        event.setEventType(eventType);
        event.setSeverity(severity);
        event.setHostName(hostName);
        event.setDetails(details);

        logEvent(event);
    }

    // Log an alert event
    public void logAlertEvent(String alertType, String severity, String hostName, String message) {
        Map<String, Object> details = new HashMap<>();
        details.put("alert_type", alertType);
        details.put("message", message);

        Event event = new Event();
        event.setScenarioId(scenarioId);
        event.setRunId(runId);
        event.setEventType("ALERT");
        event.setSeverity(severity);
        event.setHostName(hostName);
        event.setDetails(details);

        logEvent(event);
    }

    // Log a generic event
    private void logEvent(Event event) {
        try {
            String jsonEvent = objectMapper.writeValueAsString(event);

            // Log to console
            logger.info("EVENT: {}", jsonEvent);

            // Write to file for Logstash
            if (fileWriter != null) {
                fileWriter.write(jsonEvent);
                fileWriter.newLine();
                fileWriter.flush();
            }
        } catch (Exception e) {
            logger.error("Failed to serialize event: {}", e.getMessage());
        }
    }

    // Close file writer
    public void close() {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            logger.error("Failed to close log file: {}", e.getMessage());
        }
    }
}
