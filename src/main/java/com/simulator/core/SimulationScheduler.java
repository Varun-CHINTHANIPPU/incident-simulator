package com.simulator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Discrete tick-based scheduler for simulation events
 */
public class SimulationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SimulationScheduler.class);

    private int currentTick;
    private int maxTicks;
    private Random random;
    private List<ScheduledEvent> eventQueue;

    // Constructor
    public SimulationScheduler(int maxTicks, long seed) {
        this.currentTick = 0;
        this.maxTicks = maxTicks;
        this.random = new Random(seed);
        this.eventQueue = new ArrayList<>();
        logger.info("Scheduler initialized with seed: {} and maxTicks: {}", seed, maxTicks);
    }
    // Schedule an event at a specific tick
    public void scheduleEvent(int tick, Runnable action, String description) {
        eventQueue.add(new ScheduledEvent(tick, action, description));
        logger.debug("Scheduled event '{}' at tick {}", description, tick);
    }

    // Advance to next tick
    public void tick() {
        currentTick++;
        logger.debug("Tick: {}/{}", currentTick, maxTicks);

        // Execute all events scheduled for current tick
        eventQueue.stream()
                .filter(e -> e.tick == currentTick)
                .forEach(e -> {
                    logger.info("Executing: {}", e.description);
                    e.action.run();
                });
    }

    // Check if simulation is complete
    public boolean isComplete() {
        return currentTick >= maxTicks;
    }

    // Get current tick
    public int getCurrentTick() {
        return currentTick;
    }

    // Get random instance (for deterministic randomness)
    public Random getRandom() {
        return random;
    }

    // Inner class for scheduled events
    private static class ScheduledEvent {
        int tick;
        Runnable action;
        String description;

        ScheduledEvent(int tick, Runnable action, String description) {
            this.tick = tick;
            this.action = action;
            this.description = description;
        }
    }
}
