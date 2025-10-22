package com.simulator.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Collects and tracks simulation metrics
 */
public class MetricsCollector {

    private int totalEvents;
    private int totalAlertsGenerated;
    private long simulationStartTime;
    private long timeToFirstDetection;
    private Map<String, Integer> eventTypeCounts;

    // Constructor
    public MetricsCollector() {
        this.totalEvents = 0;
        this.totalAlertsGenerated = 0;
        this.timeToFirstDetection = -1;
        this.eventTypeCounts = new HashMap<>();
        this.simulationStartTime = System.currentTimeMillis();
    }

    // Record an event
    public void recordEvent(String eventType) {
        totalEvents++;
        eventTypeCounts.put(eventType, eventTypeCounts.getOrDefault(eventType, 0) + 1);
    }

    // Record an alert
    public void recordAlert() {
        totalAlertsGenerated++;
        if (timeToFirstDetection == -1) {
            timeToFirstDetection = System.currentTimeMillis() - simulationStartTime;
        }
    }

    // Get total events
    public int getTotalEvents() {
        return totalEvents;
    }

    // Get total alerts
    public int getTotalAlerts() {
        return totalAlertsGenerated;
    }

    // Get time to first detection (in milliseconds)
    public long getTimeToFirstDetection() {
        return timeToFirstDetection;
    }

    // Get event counts by type
    public Map<String, Integer> getEventTypeCounts() {
        return eventTypeCounts;
    }

    // Print summary
    public void printSummary() {
        System.out.println("\n=== Simulation Metrics ===");
        System.out.println("Total Events: " + totalEvents);
        System.out.println("Total Alerts: " + totalAlertsGenerated);
        System.out.println("Time to First Detection: " +
                (timeToFirstDetection == -1 ? "No detection" : timeToFirstDetection + " ms"));
        System.out.println("\nEvent Type Breakdown:");
        eventTypeCounts.forEach((type, count) ->
                System.out.println("  " + type + ": " + count));
        System.out.println("========================\n");
    }
}
