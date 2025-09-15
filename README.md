# Parking Lot System - Spring Boot

This is a complete example Parking Lot Management System implemented with Spring Boot.

Features:
- Google OAuth2 (placeholder configuration)
- Roles: USER and ADMIN
- Vehicle entry/exit with ticket generation
- Slot allocation strategies (Nearest, Level-wise) using Strategy pattern
- Pessimistic locking to prevent double allocation
- Payment handling with transactional processing
- H2 in-memory database with sample data (data.sql)
- Postman collection for quick testing
- Unit tests (basic)

Run:
1. mvn spring-boot:run
2. H2 console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:parkingdb)

Notes:
- Fill Google OAuth2 client id/secret in application.yml if you want to test OAuth flows.
- For quick local testing, you can disable security in SecurityConfig.
