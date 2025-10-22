package com.simulator.detection;

import com.simulator.core.Host;
import com.simulator.core.MetricsCollector;
import com.simulator.logging.EventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Detection engine that simulates SIEM/SOC alert generation
 */
public class DetectionEngine {

    private static final Logger logger = LoggerFactory.getLogger(DetectionEngine.class);

    private MetricsCollector metrics;
    private EventLogger eventLogger;
    private Random random;
    private double detectionRate;

    // Constructor
    public DetectionEngine(MetricsCollector metrics, Random random, double detectionRate, EventLogger eventLogger) {
        this.metrics = metrics;
        this.random = random;
        this.detectionRate = detectionRate;
        this.eventLogger = eventLogger;
        logger.info("Detection Engine initialized with detection rate: {}%", detectionRate * 100);
    }

    // Detect phishing attack
    public void detectPhishing(Host host) {
        if (shouldDetect()) {
            String message = "Suspicious email activity detected on " + host.getHostName();
            generateAlert("PHISHING_DETECTED", host.getHostName(), "high", message);
        }
    }

    // Detect lateral movement
    public void detectLateralMovement(Host source, Host target) {
        if (shouldDetect()) {
            String message = "Unusual authentication from " + source.getHostName() + " to " + target.getHostName();
            generateAlert("LATERAL_MOVEMENT_DETECTED", target.getHostName(), "critical", message);
        }
    }

    // Detect data exfiltration
    public void detectDataExfiltration(Host host, int dataSizeMB) {
        if (shouldDetect()) {
            String message = "Large data transfer detected: " + dataSizeMB + " MB from " + host.getHostName();
            generateAlert("DATA_EXFILTRATION_DETECTED", host.getHostName(), "critical", message);
        }
    }

    // Determine if attack should be detected (based on detection rate)
    private boolean shouldDetect() {
        return random.nextDouble() < detectionRate;
    }

    // Generate an alert
    private void generateAlert(String alertType, String hostName, String severity, String message) {
        logger.warn("[ALERT] {} | Host: {} | Severity: {} | {}",
                alertType, hostName, severity.toUpperCase(), message);

        // Log structured alert event
        eventLogger.logAlertEvent(alertType, severity, hostName, message);

        metrics.recordAlert();
    }
}
