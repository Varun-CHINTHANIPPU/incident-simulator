package com.simulator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a host (machine) in the simulated network
 */
public class Host {

    private String hostName;
    private String ipAddress;
    private String operatingSystem;
    private List<String> runningServices;
    private boolean isCompromised;

    // Constructor
    public Host(String hostName, String ipAddress, String operatingSystem) {
        this.hostName = hostName;
        this.ipAddress = ipAddress;
        this.operatingSystem = operatingSystem;
        this.runningServices = new ArrayList<>();
        this.isCompromised = false;
    }

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

    public List<String> getRunningServices() {
        return runningServices;
    }

    public void addService(String service) {
        this.runningServices.add(service);
    }

    public boolean isCompromised() {
        return isCompromised;
    }

    public void setCompromised(boolean compromised) {
        isCompromised = compromised;
    }

    @Override
    public String toString() {
        return "Host{" +
                "hostName='" + hostName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", os='" + operatingSystem + '\'' +
                ", compromised=" + isCompromised +
                '}';
    }
}
