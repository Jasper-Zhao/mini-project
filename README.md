# DeskFlow

DeskFlow is a small Spring Boot + JDBC application for booking hot desks in a Finnish office. It provides the required REST API, a date-based availability feature, and a single-page browser interface.

## Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8+

The local MySQL Docker container already prepared for this project uses:

```text
Host: localhost
Port: 3306
Database: app_db
User: root
Password: mysql_local_dev
```

## Run

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
mvn spring-boot:run
```

Then open [http://localhost:8080](http://localhost:8080). SQL setup and sample data run automatically at startup.

## API quick check

```bash
curl http://localhost:8080/api/health
curl 'http://localhost:8080/api/desks/availability?date=2026-07-24'
curl 'http://localhost:8080/api/bookings?date=2026-07-24'
```

Create a booking:

```bash
curl -X POST http://localhost:8080/api/bookings \
  -H 'Content-Type: application/json' \
  -d '{"deskId":2,"employeeName":"Anna Kowalska","date":"2026-07-24"}'
```

## Tests

```bash
JAVA_HOME="$(/usr/libexec/java_home -v 17)" mvn test
```

The service tests cover inactive-desk rejection and duplicate-booking conflict handling. The database itself also enforces the unique `(desk_id, booking_date)` rule.
