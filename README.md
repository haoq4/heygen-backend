# Heygen Backend

Heygen Backend is a simulation of a video translation backend that allows you to configure delays and results for testing purposes. This backend is paired with a client library to implement intelligent polling and interact with the `/status` API efficiently.

## Features

- Simulates a video translation backend with configurable delay and result (`pending`, `completed`, `error`).
- Provides APIs for setting up test scenarios and fetching task statuses.
- Includes a client library with intelligent polling and exponential backoff mechanism.
- Logs detailed task execution steps and results for easy debugging.

---

## Technologies Used

- **Java**: JDK 11 or higher for backend logic.
- **Spring Boot**: To create RESTful APIs.
- **Maven**: For dependency management and build automation.
- **HTML & CSS**: For the test configuration page.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven for dependency management
- Git for version control

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/haoq4/heygen-backend.git
   cd heygen-backend
   ```

2. Start the backend server:
   ```bash
   mvn spring-boot:run
   ```

3. Open your browser and navigate to:
   - `http://localhost:8080` to access the instruction page.
   - `http://localhost:8080/status` to check the API status.
   - `http://localhost:8080/test` to configure and test the backend.

---

## How to Use

### Client Library Example

To use the client library, add the `VideoTranslationClient` to your project and configure it as shown below:

```java
import com.heygen.client.VideoTranslationClient;

public class ClientLibraryExample {
    public static void main(String[] args) {
        try {
            // Initialize the client
            VideoTranslationClient client = new VideoTranslationClient("http://localhost:8080", 10, 1000);

            // Fetch the task status
            String finalStatus = client.getStatus();
            System.out.println("Final Task Status: " + finalStatus);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
```

---

## API Endpoints

### 1. `/status` [GET]
Fetches the current task status.
- **Responses**:
  - `{"result": "pending"}`
  - `{"result": "completed"}`
  - `{"result": "error"}`

### 2. `/test` [POST]
Sets up the backend with custom parameters for testing.
- **Request Parameters**:
  - `result`: The desired final state (`completed` or `error`).
  - `delay`: The time in seconds before the task transitions to the final state.

### 3. `/run-test` [GET]
Runs the integration test and returns detailed logs.

---

## Logs Example

```plaintext
Starting integration test...
Request #1 at 2024-11-20T05:15:07.951 - Status: pending
Task is still pending, retrying... (Retry #1, Delay: 1000 ms)
Request #2 at 2024-11-20T05:16:10.015 - Status: pending
Request #3 at 2024-11-20T05:18:20.230 - Status: completed
Task completed successfully!
Integration Test Completed - Final Result: completed
```

---

## Project Structure

```
heygen-backend/
├── src/
│   ├── main/
│   │   ├── java/com/heygen/          # Source code
│   │   │   ├── controllers/          # API Controllers
│   │   │   ├── client/               # Client Library
│   │   │   ├── model/                # Data Models
│   │   └── resources/
│   │       ├── static/               # Static resources (e.g., HTML)
│   │       └── application.properties
│   └── test/                         # Test cases
├── pom.xml                           # Maven configuration
├── README.md                         # Project documentation
```


