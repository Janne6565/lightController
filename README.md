# Light Controller

A Java Spring Boot application designed to control lighting systems using ESP8266 buzzers and ARTnet protocol. The system acts as a central hub running on a Raspberry Pi, receiving UDP signals from ESP devices and converting them into ARTnet commands for light control.

## System Architecture

### Hardware Components
- **Central Hub**: Raspberry Pi running this Spring Boot application
- **Input Devices**: ESP8266 modules connected to physical buzzers (5V signals)
- **Output**: ARTnet-compatible lighting equipment

### Communication Flow
1. Physical buttons/buzzers trigger ESP8266 modules
2. ESP8266 modules send UDP broadcast signals
3. Central application receives and processes UDP signals
4. Corresponding ARTnet commands are sent to lighting equipment based on predefined triggers

## Prerequisites

- Java 21 or higher
- Maven
- MySQL/MariaDB database
- Raspberry Pi (or any other computer for development)
- Network connection between ESP devices and the central hub

## Configuration

### Application Properties

Create an `application.properties` file in `src/main/resources` with the following content:
```
properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/lightcontroller
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8080

# UDP Configuration
udp.listen.port=8888
```
## Building and Running

### Building the Project
```bash
mvn clean package
```
### Running the Application

```bash
java -jar target/lightcontroller-0.0.1-SNAPSHOT.jar
```
For development, you can also run it directly using:
```shell script
mvn spring-boot:run
```


## API Endpoints

### Device Management
- `GET /api/devices` - List all registered devices
- `GET /api/devices/{uuid}` - Get device details
- `POST /api/devices` - Register a new device
- `PUT /api/devices/{uuid}` - Update device information
- `DELETE /api/devices/{uuid}` - Remove a device

### Trigger Management
- `GET /api/triggers` - List all triggers
- `POST /api/triggers` - Create a new trigger
- `PUT /api/triggers/{uuid}` - Update trigger configuration

## Entity Relationships

### Device
Represents an ESP8266 device with properties:
- UUID (unique identifier)
- MAC Address
- Name
- Description

### Trigger
Represents a mapping between input signals and actions:
- UUID
- Identifier (for matching incoming signals)
- List of actions to execute
- Last execution timestamp

### Action
Base class for different types of actions:
- ARTnet Signal Action
- Sleep Action

## Development

### Adding New Action Types

1. Create a new class extending `Action`
2. Add the new action type to `@JsonSubTypes` in `Action.java`
3. Implement the `execute` method

Example:
```java
@Entity
public class NewAction extends Action {
    @Override
    public void execute(ArtNetClient artnet) {
        // Implementation
    }
}
```


## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

[Add your license information here]

## Contact

Janne Keipert ~ jabbekeipert@gmail.com