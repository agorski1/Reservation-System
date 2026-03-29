# Hotel Reservation System – Backend

## 📌 Overview

This module is the backend service for a **complete hotel reservation system**. It exposes a REST API used by multiple client applications.

The system consists of three main parts:

* **Backend (this repository)** – Spring Boot REST API
* **Web Application (React)** – client interface for customers
* **Desktop Application (JavaFX)** – internal tool for hotel employees

### 🔗 Related Repositories

* 🌐 Web (React) – *customer-facing application*  
  👉 [React Repo](https://github.com/agorski1/Reservation-System-Web)

* 🖥 Desktop (JavaFX) – *employee management system*  
  👉 [Reservation System Desktop](https://github.com/agorski1/Reservation-System-Desktop)

---

The backend handles core business logic such as:

* reservations management
* user accounts and roles
* payments processing
* reporting and analytics


## 🛠 Tech Stack

* Java 21
* Spring Boot
* Spring Security (JWT authentication)
* Spring Data JPA (Hibernate)
* Oracle Database (Dockerized)
* Maven
* Docker Compose
* Postman (API testing)

---

## 🔐 Security

The application uses **JWT-based authentication** with Spring Security.

### Roles:

* `ADMIN`
* `EMPLOYEE`
* `CUSTOMER`

Authentication is required for most endpoints.

---

## 🏗 Architecture

The project follows a layered architecture:

* **Domain** – business logic and core entities
* **Presentation** – REST controllers (API layer)
* **Infrastructure** – database access, repositories, external integrations

---

## 🚀 Getting Started

### Prerequisites

* Java 21
* Maven
* Docker & Docker Compose

---

## 🐳 Running Oracle Database (Docker)

Start Oracle database using Docker Compose:

```bash
docker-compose up -d
```

Database configuration:

* URL: `jdbc:oracle:thin:@//localhost:1521/XEPDB1`
* Username: `rs_user`
* Password: `hotel`

---

## ▶️ Running the Application

```bash
mvn spring-boot:run
```

---

## ⚙️ Configuration

Located in:

```
src/main/resources/application.properties
```

### Key settings:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.username=rs_user
spring.datasource.password=hotel

spring.jpa.hibernate.ddl-auto=none

app.security.jwt-expiration-in-ms=3600000
```

---

## 📡 API Endpoints

### 🔑 Authentication

* `POST /hd/auth/login`
* `POST /hd/auth/register`

---

### 🧑 Users

* `POST /hd/users/create/employee`
* `GET /hd/users/employees`
* `PUT /hd/users/update`
* `PUT /hd/users/password/change`

---

### 🏨 Reservations

* `GET /hd/reservations`
* `POST /hd/reservations`
* `POST /hd/reservations/manual`
* `GET /hd/reservations/my`
* `GET /hd/reservations/my/current`
* `GET /hd/reservations/{id}`
* `GET /hd/reservations/employee/{id}`
* `PUT /hd/reservations/{reservationId}/status`
* `POST /hd/reservations/{reservationId}/cancel`

---

### 🛏 Rooms

* `GET /hd/rooms`
* `GET /hd/rooms/available`
* `GET /hd/rooms/{id}/status`
* `GET /hd/rooms/{deskId}/current-reservations`

---

### 🧾 Room Types

* `GET /hd/room-type`
* `GET /hd/room-type/available`
* `GET /hd/room-type/{id}`
* `GET /hd/room-type/price`

---

### 💳 Payments

* `POST /hd/payments/process`
* `GET /hd/payments/{reservationId}`

---

### 📊 Reports

* `GET /hd/reports/occupancy`
* `GET /hd/reports/payments`

---

### 📊 Dashboard

* `GET /hd/dashboard/today`

---

### 🔑 Roles

* `GET /hd/roles`

---

## 🗄 Database

* Oracle Database running in Docker
* Hibernate used as ORM
* `ddl-auto=none` (schema managed manually)

---

## 🧪 Testing

API was tested using **Postman**.

(Unit and integration tests are planned for future development.)

---

## 📂 Project Structure

```plaintext
src/main/java/com/reservio/reservation_system
├── domain
│   ├── model          # core business entities
│   ├── service        # business logic
│   ├── repository     # domain interfaces
│   └── exception
│
├── infrastructure
│   ├── entity         # JPA entities
│   ├── dao            # database access (implementations)
│   ├── security       # JWT, Spring Security config
│   ├── specification  # query filters
│   ├── report         # reporting logic
│   └── user           # user-related infrastructure
│
├── presentation
│   ├── controller     # REST endpoints
│   ├── dto            # request/response objects
│   ├── mapper         # DTO ↔ entity mapping
│   └── exception      # API exception handling
│
src/main/resources
└── db                 # database scripts / config
```


---

## ⚠️ Notes

* Make sure Oracle container is running before starting the backend
* JWT token is required for secured endpoints
* Default configuration is for local development

