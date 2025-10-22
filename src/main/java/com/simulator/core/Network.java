package com.simulator.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the simulated network containing all hosts
 */
public class Network {

    private Map<String, Host> hosts;
    private List<String> connections;

    // Constructor
    public Network() {
        this.hosts = new HashMap<>();
        this.connections = new ArrayList<>();
    }

    // Add a host to the network
    public void addHost(Host host) {
        hosts.put(host.getHostName(), host);
    }

    // Get a host by name
    public Host getHost(String hostName) {
        return hosts.get(hostName);
    }

    // Get all hosts
    public Map<String, Host> getAllHosts() {
        return hosts;
    }

    // Add a connection between two hosts
    public void addConnection(String host1, String host2) {
        String connection = host1 + " <-> " + host2;
        connections.add(connection);
    }

    // Get all connections
    public List<String> getConnections() {
        return connections;
    }

    // Get count of compromised hosts
    public int getCompromisedHostCount() {
        return (int) hosts.values().stream()
                .filter(Host::isCompromised)
                .count();
    }

    @Override
    public String toString() {
        return "Network{" +
                "totalHosts=" + hosts.size() +
                ", compromisedHosts=" + getCompromisedHostCount() +
                '}';
    }
}
