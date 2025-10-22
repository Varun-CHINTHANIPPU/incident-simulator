package com.simulator;

import com.simulator.config.ConfigLoader;
import com.simulator.config.ScenarioConfig;
import com.simulator.core.SimulationEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Main entry point for the Cybersecurity Incident Response Simulator
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Cybersecurity Incident Response Simulator ===");

        try {
            // Parse command line arguments
            long seed = 12345; // Default seed
            String scenarioFile = "scenarios/sample-scenario.json"; // Default scenario

            if (args.length > 0) {
                scenarioFile = args[0];
                logger.info("Using scenario file from command line: {}", scenarioFile);
            }

            if (args.length > 1) {
                try {
                    seed = Long.parseLong(args[1]);
                    logger.info("Using seed from command line: {}", seed);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid seed provided, using default: {}", seed);
                }
            }

            // Load scenario configuration
            logger.info("Loading scenario configuration...");
            ScenarioConfig scenarioConfig = ConfigLoader.loadFromResource(scenarioFile);
            logger.info("Loaded scenario: {} - {}",
                    scenarioConfig.getScenarioId(),
                    scenarioConfig.getDescription());

            // Generate unique run ID
            String runId = UUID.randomUUID().toString();

            // Create and configure simulation engine
            SimulationEngine engine = new SimulationEngine(scenarioConfig, runId, seed);

            // Initialize network
            engine.initializeNetwork();

            // Run simulation
            engine.run();

            logger.info("=== Simulation Complete ===");

        } catch (Exception e) {
            logger.error("Simulation failed with error: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}
