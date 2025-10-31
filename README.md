# Cybersecurity Incident Response Simulator

A configurable, tick-based discrete event simulator that generates realistic cybersecurity attack scenarios and SIEM-ready logs for incident response training and analysis.

Project Overview
The Cybersecurity Incident Response Simulator is designed to generate realistic cyberattack scenarios within a controlled environment. It produces structured, SIEM-compatible logs that can be used for training, testing, and analysis.

It is suitable for:
    • Security Operations Center (SOC) analyst training
    • Incident response playbook development and testing
    • SIEM configuration and validation
    • Security awareness exercises
    • Academic and cybersecurity research
    
Key feature: deterministic randomness using a seed-based random number generator for reproducible simulation results.
Features

 ### Attack Simulation
    • Phishing attacks with configurable success rates
    • Lateral movement between network hosts
    • Data exfiltration with adjustable payload sizes
    • Probabilistic detection model simulating real SIEM accuracy (default: 70% detection rate)
    
 ### Logging and Observability
    • Structured JSON logs ready for ELK stack ingestion
    • Real-time event streaming to Elasticsearch via Logstash
    • Preconfigured Kibana dashboards for visualization
    • Metrics: total events, alerts, and time-to-detection
    
 ### Configuration
    • JSON-based scenario configuration files
    • Network topology definition (hosts, IPs, services)
    • Discrete tick-based event system
    • Seed-based reproducibility for consistent results
```    
Architecture
┌─────────────────┐     ┌──────────────┐     ┌─────────────────┐
│ Simulator       |───▶│ JSON Logs	   │───▶│ Logstash        │
│ (Java 17)       │     │ (Events)     │     |(Pipeline)       │
└─────────────────┘     └──────────────┘     └─────────────────┘
                           │
                           ▼
                    ┌─────────────────┐
                    │ Elasticsearch   │
                    │ (Indexing)      │
                    └─────────────────┘
                           │
                           ▼
                    ┌─────────────────┐
                    │ Kibana          │
                    │ (Visualization) │
                    └─────────────────┘
```
```
Project Structure
incident-simulator/
├── src/main/java/com/simulator/
│ ├── core/ # Simulation engine and scheduler
│ ├── config/ # Scenario configuration and loader
│ ├── logging/ # Structured event logging
│ └── detection/ # Attack detection engine
├── src/main/resources/
│ ├── scenarios/ # Scenario JSON definitions
│ └── log4j2.xml # Logging configuration
├── src/docker/ # Docker and ELK setup
│ ├── docker-compose.yml
│ └── logstash.conf
├── logs/ # Generated simulation logs
├── pom.xml # Maven dependencies
└── README.md
```

Development

### Building from Source:
mvn clean compile

### Running Tests:
mvn test

### Creating a JAR:
mvn package
java -jar target/incident-simulator-1.0-SNAPSHOT.jar


### Dependencies
    • Jackson (JSON parsing and serialization)
    • SLF4J + Log4j2 (logging)
    • JUnit 5 (testing)
    • Docker (ELK stack deployment)
Refer to pom.xml for the full dependency list.


### Quick Start
#### Prerequisites
    • Java 17 or later
    • Maven 3.6 or later
    • Docker and Docker Compose (for ELK stack)
1. Clone the Repository
2. Build the Project
mvn clean package
3. Start ELK Stack
cd src/docker
docker-compose up -d
Run Your First Simulation
cd ../..

mvn exec:java -Dexec.mainClass="com.simulator.Main"

5. View Results in Kibana
    1. Open http://localhost:5601 in a browser
    2. Navigate to Management → Stack Management → Data Views
    3. Create a data view named simulation-events-* with timestamp field @timestamp
    4. Explore events under Discover
    5. Create custom visualizations and dashboards
  
### Usage
Running Custom Scenarios
```mvn exec:java -Dexec.mainClass="com.simulator.Main" -Dexec.args="scenarios/my-scenario.json 54321"```

### Arguments:
    • scenarios/my-scenario.json: Path to scenario file
    • 54321: Random seed for reproducibility (optional)
Creating Custom Scenarios
Example JSON (saved in src/main/resources/scenarios/):
```
{
  "scenario_id": "custom-attack",
  "description": "Custom phishing to ransomware scenario",
  "max_ticks": 30,
  "hosts": [
    {
      "host_name": "web-server",
      "ip_address": "192.168.1.10",
      "operating_system": "Ubuntu 22.04",
      "services": ["HTTP", "SSH", "MySQL"]
    }
  ],
  "attacks": [
    {
      "tick": 5,
      "attack_type": "phishing",
      "target_host": "web-server",
      "parameters": {
        "success_rate": 0.8
      }
    }
  ]
}
```
### Supported Attack Types
```
Attack Type	Description	Parameters
phishing	Email-based social engineering	success_rate (0.0–1.0)
lateral_movement	Host-to-host compromise	source_host, method
data_exfiltration	Data theft	data_size_mb, destination
```

### Example Output
```
Console Output
21:49:07.981 [main] WARN SimulationEngine - Host workstation-01 compromised via phishing
21:49:07.988 [main] WARN DetectionEngine - [ALERT] PHISHING_DETECTED | Host: workstation-01
21:49:07.992 [main] WARN DetectionEngine - [ALERT] DATA_EXFILTRATION_DETECTED | Host: db-server

=== Simulation Metrics ===
Total Events: 4
Total Alerts: 2
Time to First Detection: 9 ms
```
```
{
  "timestamp": "2025-10-22T16:19:07.981Z",
  "scenario_id": "phishing-lateral-movement",
  "run_id": "ccf3f656-f21b-46f9-b579-eb684e0fc5cc",
  "event_type": "phishing",
  "severity": "medium",
  "host_name": "workstation-01",
  "details": {
    "success": true,
    "attack_method": "phishing"
  }
}
```

### Docker Setup
#### ELK Stack Services
    • Elasticsearch: http://localhost:9200
    • Kibana: http://localhost:5601
    • Logstash: Port 5000
Managing Docker Services
```
cd src/docker
docker-compose up -d
```
Stop:
```
docker-compose down
```

View logs:
```
docker-compose logs -f logstash
```

Restart a service:
```
docker-compose restart logstash
```

### Configuration
Detection Rate:<br>
Edit src/main/java/com/simulator/core/SimulationEngine.java:
```
this.detectionEngine = new DetectionEngine(metrics, scheduler.getRandom(), 0.7); // 70% detection
```
Logging Level<br>
Edit src/main/resources/log4j2.xml:

```
<Logger name="com.simulator" level="info" additivity="false">
Options: debug, info, warn, error

```

Kibana Visualization Examples<br>
Attack Type Distribution (Pie Chart)<br>
Field: event_type.keyword<br>
Shows breakdown of phishing, lateral movement, and data exfiltration events.<br>
<br>
Severity Timeline (Area Chart)<br>
X-axis: @timestamp<br>
Y-axis: Count<br>
Breakdown by: severity.keyword<br>
<br>
Compromised Hosts (Bar Chart)<br>
X-axis: host_name.keyword<br>
Y-axis: Count of events<br>

Attack Chain (Table)<br>
Columns: details.source_host.keyword, details.target_host.keyword<br>
Filter: event_type: "lateral_movement"<br>

### Use Cases
SOC Training: Identify attack patterns and practice incident response.
SIEM Testing: Generate data to validate correlation rules and alerts.
Incident Response Drills: Simulate containment and recovery steps.
Security Research: Analyze attack timing and detection rates.
Academic Projects: Demonstrate cybersecurity principles and defense mechanisms.




### Execution:
```
cd src/docker
sudo systemctl start docker
docker-compose up -d
cd ../../
mvn clean compile
mvn exec:java -Dexec.mainClass="com.simulator.Main"
```
Elasticseach starts on: http://localhost:5601/
