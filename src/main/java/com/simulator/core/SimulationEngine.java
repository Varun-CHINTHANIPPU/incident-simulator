package com.simulator.core;

import com.simulator.config.ScenarioConfig;
import com.simulator.detection.DetectionEngine;
import com.simulator.logging.EventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Main simulation engine that coordinates the entire simulation
 */
public class SimulationEngine {

    private static final Logger logger = LoggerFactory.getLogger(SimulationEngine.class);

    private String scenarioId;
    private String runId;
    private Network network;
    private SimulationScheduler scheduler;
    private MetricsCollector metrics;
    private DetectionEngine detectionEngine;
    private EventLogger eventLogger;
    private ScenarioConfig scenarioConfig;

    // Constructor with scenario config
    public SimulationEngine(ScenarioConfig scenarioConfig, String runId, long seed) {
        this.scenarioConfig = scenarioConfig;
        this.scenarioId = scenarioConfig.getScenarioId();
        this.runId = runId;
        this.network = new Network();
        this.scheduler = new SimulationScheduler(scenarioConfig.getMaxTicks(), seed);
        this.metrics = new MetricsCollector();
        this.eventLogger = new EventLogger(scenarioId, runId);
        this.detectionEngine = new DetectionEngine(metrics, scheduler.getRandom(), 0.7, eventLogger);
        logger.info("Simulation Engine initialized - Scenario: {}, Run: {}", scenarioId, runId);
    }

    // Initialize the network from scenario config
    public void initializeNetwork() {
        logger.info("Initializing network from scenario config...");

        // Create hosts from config
        for (ScenarioConfig.HostConfig hostConfig : scenarioConfig.getHosts()) {
            Host host = new Host(
                    hostConfig.getHostName(),
                    hostConfig.getIpAddress(),
                    hostConfig.getOperatingSystem()
            );

            // Add services
            if (hostConfig.getServices() != null) {
                for (String service : hostConfig.getServices()) {
                    host.addService(service);
                }
            }

            network.addHost(host);
            logger.debug("Added host: {}", host.getHostName());
        }

        logger.info("Network initialized with {} hosts", network.getAllHosts().size());
    }

    // Schedule attacks from scenario config
    public void scheduleAttacks() {
        logger.info("Scheduling attacks from scenario config...");

        for (ScenarioConfig.AttackConfig attackConfig : scenarioConfig.getAttacks()) {
            int tick = attackConfig.getTick();
            String attackType = attackConfig.getAttackType();
            String targetHost = attackConfig.getTargetHost();

            scheduler.scheduleEvent(tick, () -> {
                executeAttack(attackConfig);
            }, attackType + " on " + targetHost);
        }

        logger.info("Scheduled {} attacks", scenarioConfig.getAttacks().size());
    }

    // Execute an attack based on its type
    private void executeAttack(ScenarioConfig.AttackConfig attackConfig) {
        String attackType = attackConfig.getAttackType();
        String targetHost = attackConfig.getTargetHost();

        switch (attackType) {
            case "phishing":
                simulatePhishingAttack(targetHost);
                break;
            case "lateral_movement":
                String sourceHost = (String) attackConfig.getParameters().get("source_host");
                simulateLateralMovement(sourceHost, targetHost);
                break;
            case "data_exfiltration":
                simulateDataExfiltration(targetHost, attackConfig);
                break;
            default:
                logger.warn("Unknown attack type: {}", attackType);
        }
    }

    // Run the simulation
    public void run() {
        logger.info("Starting simulation...");

        // Schedule all attacks from config
        scheduleAttacks();

        // Main simulation loop
        while (!scheduler.isComplete()) {
            scheduler.tick();
        }

        logger.info("Simulation completed");
        metrics.printSummary();
    }

    // Simulate a phishing attack
    private void simulatePhishingAttack(String targetHostName) {
        Host target = network.getHost(targetHostName);
        if (target != null) {
            target.setCompromised(true);
            logger.warn("Host {} compromised via phishing!", target.getHostName());

            // Log event
            Map<String, Object> details = new HashMap<>();
            details.put("attack_method", "phishing");
            details.put("success", true);
            eventLogger.logAttackEvent("phishing", "medium", target.getHostName(), details);

            metrics.recordEvent("phishing_success");

            // Try to detect the attack
            detectionEngine.detectPhishing(target);
        }
    }

    // Simulate lateral movement
    private void simulateLateralMovement(String sourceHostName, String targetHostName) {
        Host source = network.getHost(sourceHostName);
        Host target = network.getHost(targetHostName);

        if (source != null && source.isCompromised() && target != null) {
            target.setCompromised(true);
            logger.warn("Lateral movement: {} -> {}", source.getHostName(), target.getHostName());

            // Log event
            Map<String, Object> details = new HashMap<>();
            details.put("source_host", source.getHostName());
            details.put("target_host", target.getHostName());
            details.put("method", "credential_theft");
            eventLogger.logAttackEvent("lateral_movement", "high", target.getHostName(), details);

            metrics.recordEvent("lateral_movement");

            // Try to detect the attack
            detectionEngine.detectLateralMovement(source, target);
        } else {
            logger.warn("Lateral movement failed: source not compromised or host not found");
        }
    }

    // Simulate data exfiltration
    private void simulateDataExfiltration(String targetHostName, ScenarioConfig.AttackConfig attackConfig) {
        Host target = network.getHost(targetHostName);
        if (target != null && target.isCompromised()) {
            Object dataSizeObj = attackConfig.getParameters().get("data_size_mb");
            int dataSize = dataSizeObj != null ? ((Number) dataSizeObj).intValue() : 0;
            logger.warn("Data exfiltration from {}: {} MB", target.getHostName(), dataSize);

            // Log event
            Map<String, Object> details = new HashMap<>();
            details.put("data_size_mb", dataSize);
            details.put("destination", "external");
            eventLogger.logAttackEvent("data_exfiltration", "critical", target.getHostName(), details);

            metrics.recordEvent("data_exfiltration");

            // Try to detect the attack
            detectionEngine.detectDataExfiltration(target, dataSize);
        } else {
            logger.warn("Data exfiltration failed: host not compromised or not found");
        }
    }

    // Getters
    public Network getNetwork() {
        return network;
    }

    public MetricsCollector getMetrics() {
        return metrics;
    }
}
