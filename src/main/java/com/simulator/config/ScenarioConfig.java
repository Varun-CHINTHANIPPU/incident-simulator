package com.simulator.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Configuration class for scenario definition
 */
public class ScenarioConfig {

    @JsonProperty("scenario_id")
    private String scenarioId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("max_ticks")
    private int maxTicks;

    @JsonProperty("hosts")
    private List<HostConfig> hosts;

    @JsonProperty("attacks")
    private List<AttackConfig> attacks;

    // Getters and Setters
    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    public List<HostConfig> getHosts() {
        return hosts;
    }

    public void setHosts(List<HostConfig> hosts) {
        this.hosts = hosts;
    }

    public List<AttackConfig> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<AttackConfig> attacks) {
        this.attacks = attacks;
    }

    // Inner class for host configuration
    public static class HostConfig {
        @JsonProperty("host_name")
        private String hostName;

        @JsonProperty("ip_address")
        private String ipAddress;

        @JsonProperty("operating_system")
        private String operatingSystem;

        @JsonProperty("services")
        private List<String> services;

        // Getters and Setters
        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getOperatingSystem() {
            return operatingSystem;
        }

        public void setOperatingSystem(String operatingSystem) {
            this.operatingSystem = operatingSystem;
        }

        public List<String> getServices() {
            return services;
        }

        public void setServices(List<String> services) {
            this.services = services;
        }
    }

    // Inner class for attack configuration
    public static class AttackConfig {
        @JsonProperty("tick")
        private int tick;

        @JsonProperty("attack_type")
        private String attackType;

        @JsonProperty("target_host")
        private String targetHost;

        @JsonProperty("parameters")
        private Map<String, Object> parameters;

        // Getters and Setters
        public int getTick() {
            return tick;
        }

        public void setTick(int tick) {
            this.tick = tick;
        }

        public String getAttackType() {
            return attackType;
        }

        public void setAttackType(String attackType) {
            this.attackType = attackType;
        }

        public String getTargetHost() {
            return targetHost;
        }

        public void setTargetHost(String targetHost) {
            this.targetHost = targetHost;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
}
