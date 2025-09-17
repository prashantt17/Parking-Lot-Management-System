## 🚗 Parking Lot Management System - Spring Boot

This is a **Spring Boot Parking Lot Management System** that demonstrates authentication, parking slot allocation, ticketing, and payments. It’s designed as a **production-ready backend project** with OAuth2 security, JPA persistence, concurrency-safe operations, and extensibility.

---

## ✨ Features

### 🔐 Authentication & Authorization

* **Google & GitHub OAuth2 Login** via Spring Security
* Role-based access:

  * **USER** → Park & retrieve vehicles, pay fees
  * **ADMIN** → Configure pricing, manage slots
* Unauthorized users are blocked from protected APIs

### 🅿️ Core Parking Lot Functionality

* Multiple floors and slot types (`CAR`, `BIKE`, `TRUCK`)
* Smart slot allocation strategy (nearest available by type)
* Ticket generation on entry:

  * Vehicle number
  * Slot number
  * Entry timestamp
* Exit flow:

  * Calculate charges by vehicle type + duration
  * Require payment before freeing slot

### 💳 Payment Handling

* Configurable pricing rules (e.g., first 2 hours free, hourly after)
* Atomic payments:

  * Slot freed **only after successful payment**
  * If payment fails → slot remains occupied

### 🛠️ System Design

* Concurrency-safe slot allocation with DB locks
* Prevents double allocation & duplicate entries
* Error handling for full lot, duplicate plate, or unpaid exits

---

## 🏗️ Architecture

```text
┌───────────────┐
│  Controller   │  → REST APIs (entry, exit, admin, user info)
└───────┬───────┘
        │
┌───────▼───────┐
│   Services    │  → Business logic (allocation, payment, pricing)
└───────┬───────┘
        │
┌───────▼───────┐
│ Repositories  │  → Spring Data JPA (H2/MySQL)
└───────┬───────┘
        │
┌───────▼───────┐
│    Entities   │  → Ticket, Vehicle, ParkingSlot, Payment, User
└───────────────┘
```

---

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA + Hibernate**
* **Spring Security (OAuth2 + Google/GitHub login)**
* **H2 (in-memory)** for testing
* **MySQL** (optional for production)
* **Maven** build tool

---

## 📦 Database Schema

### ParkingSlot

* `id`, `type (CAR|BIKE|TRUCK)`, `status (FREE|OCCUPIED|RESERVED)`, `floor`, `slot_number`, `last_used`

### Vehicle

* `id`, `plate_no (unique)`, `type`, `owner_name`

### Ticket

* `id`, `vehicle_id`, `slot_id`, `entry_time`, `exit_time`, `status (ACTIVE|PAID|EXITED)`

### Payment

* `id`, `ticket_id`, `amount`, `status (PENDING|SUCCESS|FAILED)`, `timestamp`

---

## 🚀 Setup & Run

### 1️⃣ Prerequisites

* Java 17+
* Maven
* Google/GitHub OAuth credentials

### 2️⃣ Configure OAuth

Edit `src/main/resources/application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: [openid, profile, email]
          github:
            client-id: ${GITHUB_CLIENT_ID}
            client-secret: ${GITHUB_CLIENT_SECRET}
            scope: [read:user, user:email]
```

👉 **Never commit real secrets.** Use environment variables.

### 3️⃣ Run App

```bash
mvn spring-boot:run
```

* App starts on `http://localhost:8080`
* H2 console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:parkingdb`)

---

## 🔑 Login URLs

* Google Login → [http://localhost:8080/oauth2/authorization/google](http://localhost:8080/oauth2/authorization/google)
* GitHub Login → [http://localhost:8080/oauth2/authorization/github](http://localhost:8080/oauth2/authorization/github)

After login → redirected to `/me`, showing your user info.

---

## 📡 REST API Endpoints

### User APIs

| Method | Endpoint                 | Role | Description               |
| ------ | ------------------------ | ---- | ------------------------- |
| POST   | `/api/parking/entry`     | USER | Park vehicle (get ticket) |
| POST   | `/api/parking/exit/{id}` | USER | Exit & pay                |
| GET    | `/ticket/{id}`           | USER | Get ticket details        |
| GET    | `/me`                    | USER | Get current user info     |

### Admin APIs

| Method | Endpoint             | Role  | Description            |
| ------ | -------------------- | ----- | ---------------------- |
| POST   | `/api/admin/slots`   | ADMIN | Add new parking slot   |
| PUT    | `/api/admin/pricing` | ADMIN | Update pricing rules   |
| GET    | `/api/admin/me`      | ADMIN | Test admin role access |

---

## 🧪 Sample Test Data

```sql
INSERT INTO parking_slot (type, status, floor, slot_number) VALUES
('CAR','FREE',1,'F1-001'),
('BIKE','FREE',1,'F1-002');

INSERT INTO vehicle (plate_no, type, owner_name) VALUES
('KA01AB1234','CAR','Alice'),
('KA02CD5678','BIKE','Bob');
```

---

## ⚡ Key Design Choices

* **Strategy Pattern** for pricing rules
* **@Transactional + PESSIMISTIC\_WRITE locks** for concurrency-safe slot allocation
* **Custom Exception Handling** → proper error messages instead of whitelabel pages
* **Role Mapping** stored in DB for extensibility

---

