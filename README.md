# gRPC Real-Time Chat Application

A real-time, bidirectional chat application built with Kotlin, Spring Boot, and gRPC. This project was developed as a hands-on assignment to demonstrate active streaming connections and database persistence using PostgreSQL.

## 🚀 Tech Stack
* **Language:** Kotlin / Java 21
* **Framework:** Spring Boot 3.x
* **API Protocol:** gRPC (Bidirectional Streaming) & Protocol Buffers (`.proto`)
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Gradle

## ✨ Features
* **Bidirectional gRPC Streaming:** Allows real-time, continuous two-way communication between the server and multiple clients.
* **Concurrent User Handling:** Utilizes thread-safe collections (`ConcurrentHashMap`) to manage multiple active users simultaneously.
* **Data Persistence:** Automatically saves all incoming chat messages to a PostgreSQL database before broadcasting them to the chat room.
* **Kotlin Test Client:** Includes a built-in command-line client to simulate users joining, sending messages, and receiving broadcasts.

## 🛠️ Prerequisites
Before running this application, ensure you have the following installed:
* Java Development Kit (JDK) 21
* PostgreSQL (Running on default port `5432`)
* Gradle

## ⚙️ Setup & Installation

**1. Database Configuration**
Ensure your local PostgreSQL instance is running. The application is configured to connect to a database named `postgres`. 
Update the `src/main/resources/application.properties` (or `.yml`) file with your local database credentials if they differ:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
**2. Clone and Build**

```bash
git clone [https://github.com/hetvi3012/chat-server.git](https://github.com/hetvi3012/chat-server.git)
cd YourRepoName
./gradlew build
```
## 🏃‍♂️ Running the Application

**Step 1: Start the gRPC Server**
Run the Spring Boot application. The server will start and listen for incoming gRPC connections on port 9090.

```bash
./gradlew bootRun
```
**Step 2: Start the Clients (The Demo)**
To demonstrate two clients communicating via the server, you can run multiple instances of the included test client.

1. Open a new terminal window and execute the `ChatTestClientKt` main function.
2. Open a second terminal window and execute the same client again.
3. You will see messages sent from one client broadcasted by the server and received by the other client in real-time.

## 📂 Project Structure Highlights
* `src/main/proto/`: Contains the `chat.proto` file defining the gRPC contract and message structures.
* `src/main/kotlin/.../service/ChatServiceImpl.kt`: The core gRPC service managing the bidirectional stream and broadcasting logic.
* `src/main/kotlin/.../entity/ChatMessageEntity.kt`: The JPA entity mapping the chat data to the PostgreSQL table.
* `src/main/kotlin/.../ChatTestClient.kt`: The simulated frontend client used for testing the streams.
