# 🛠️ HelpDesk — IT Ticket Management API

> A robust, production-ready RESTful backend API built with **Java 21** and **Spring Boot**, engineered to handle enterprise IT support workflows, secure multi-role access, and real-time ticket state transitions.

---

## 🎯 Executive Summary

The **HelpDesk IT System** was developed during an intensive backend engineering sprint. Designed as a clean **Modular Monolith**, the application emphasizes stateless security, strict business rule enforcement (state machines), and clean data isolation between database entities and API consumers.

---

## 🧰 Tech Stack

* **Language & Runtime:** Java 21 (JDK 21)
* **Framework:** Spring Boot 3.x (Spring Web, Spring Security)
* **Persistence & ORM:** Spring Data JPA / Hibernate, MySQL Database
* **Security & Auth:** Stateless JWT (JSON Web Tokens), BCrypt Password Hashing
* **Testing & Quality Assurance:** JUnit 5, Mockito, Postman API Testing
* **Version Control:** Git, GitHub (Feature Branch Workflow)

---

## 👥 Engineering Team & Key Contributions

### 🔒 Developer 1 (David) — Authentication, Users & Security Infrastructure
* **Security Architecture:** Implemented a custom stateless filter (`JwtAuthenticationFilter`) hooked into Spring Security, validating HMAC-SHA256 signed Bearer tokens on protected routes.
* **Credential Protection:** Configured `BCryptPasswordEncoder` for salted password hashing, ensuring zero plain-text passwords ever touch persistence storage.
* **DTO & Data Sanitization Layer:** Engineered request/response DTOs (`UserResponse`, `LoginResponse`, `RegisterRequest`) to strictly decouple JPA entity models from external API contracts, preventing sensitive field exposure.
* **Endpoints Delivered:**
    * `POST /api/auth/register` — Account creation with role mapping and password policy validation.
    * `POST /api/auth/login` — Authentication handling and structured JWT token issuance.
    * `GET /api/users`, `GET /api/users/{id}`, `PUT /api/users/{id}` — User administration and profile updates.

### 🎫 Developer 2 (Miguel) — Ticket Core Domain & Workflow State Machine
* **Domain Modeling:** Designed the primary `Ticket` lifecycle and relationship architecture.
* **State Machine Logic:** Enforced deterministic ticket transitions (`OPEN` ➔ `IN_PROGRESS` ➔ `RESOLVED` ➔ `CLOSED`), programmatically preventing invalid state jumps or edits on closed tickets.
* **Role-Based Authorization:** Implemented access control rules ensuring `EMPLOYEE` users can only view their own tickets, while `AGENT` and `ADMIN` users can claim, manage, and transition queue items.
* **Endpoints Delivered:**
    * `POST /api/tickets` & `GET /api/tickets` — Ticket creation and scoped listing.
    * `PATCH /api/tickets/{id}/status` — Status transition processing.
    * `PATCH /api/tickets/{id}/assign` — Agent ticket assignment logic.

### 📊 Developer 3 (Eva) — Dashboard, Comments & Testing Suite
* **Analytics Layer:** Built aggregate status metrics endpoints providing real-time system visibility (`Total`, `Open`, `In-Progress`, `Resolved`).
* **Interactive Commentary:** Built the ticket comment system allowing asynchronous communication between Agents and Employees.
* **Testing & Error Handling:** Developed unit test suites (JUnit 5/Mockito) verifying core domain rules and access boundaries. Configured global exception handlers for standardized HTTP error responses.
* **Endpoints Delivered:**
    * `GET /api/dashboard/statistics` — Operational KPI statistics.
    * `POST /api/tickets/{id}/comments` & `GET /api/tickets/{id}/comments` — Threaded commentary on active tickets.

---

## 🛡️ Architectural & Security Highlights

* **Stateless Token Management:** No HTTP sessions stored on the server. Requests are independently authenticated via signed JWT payloads.
* **Clean DTO Decoupling:** JPA Entities (`User`) never bleed into the API layer; all controller responses map to sanitized data structures (`UserResponse`).
* **Explicit RBAC Boundaries:** Spring Security rules strictly divide operational capabilities between `EMPLOYEE`, `AGENT`, and `ADMIN` roles.