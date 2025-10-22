package com.simulator.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;

/**
 * Represents a single event in the simulation
 */
public class Event {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("scenario_id")
    private String scenarioId;

    @JsonProperty("run_id")
    private String runId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("severity")
    private String severity;

    @JsonProperty("host_name")
    private String hostName;

    @JsonProperty("details")
    private Map<String, Object> details;

    // Constructor
    public Event() {
        this.timestamp = Instant.now().toString();
    }

    // Getters and Setters
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
