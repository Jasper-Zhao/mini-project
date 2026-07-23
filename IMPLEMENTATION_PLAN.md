# DeskFlow — Implementation Plan

## 1. Goal and scope

Build a small, demo-ready Spring Boot REST API for Finnish-office hot-desk booking, backed by the local MySQL container. Include seed data, a one-page frontend, and one additional feature: desk availability by date.

The core success path is: choose a date → see available desks → create a booking → see it in that date's bookings → cancel it.

## 2. Technical design

| Area | Decision |
| --- | --- |
| Runtime | Java 21 and Spring Boot 3.x |
| API | Spring Web, JSON over HTTP under `/api` |
| Persistence | Spring JDBC (`JdbcTemplate`) with explicit SQL |
| Database | Local MySQL 8.4 (`mysql-local`, port `3306`, database `app_db`) |
| Schema and seed data | Versioned SQL scripts (`schema.sql` and `data.sql`) run on application startup |
| Frontend | Static HTML, CSS, and vanilla JavaScript served by Spring Boot |
| Validation and errors | Jakarta Bean Validation plus a central exception handler returning consistent JSON errors |

### Configuration

Use environment variables with development-safe defaults:

```text
DB_HOST=localhost
DB_PORT=3306
DB_NAME=app_db
DB_USER=root
DB_PASSWORD=mysql_local_dev
```

`application.yml` will construct the JDBC URL from these values and enable SQL initialization. Credentials will not be committed outside this development configuration.

## 3. Data model and integrity

### Desk

| Field | Type | Rules |
| --- | --- | --- |
| `id` | Long | primary key, generated |
| `code` | String | required and unique, e.g. `HEL-3F-12` |
| `floor` | Integer | required |
| `hasMonitor` | Boolean | required |
| `active` | Boolean | required; `false` desks cannot be booked |

### Booking

| Field | Type | Rules |
| --- | --- | --- |
| `id` | Long | primary key, generated |
| `desk` | many-to-one Desk | required foreign key |
| `employeeName` | String | required, non-blank, trimmed |
| `date` | LocalDate | required; one booking per desk/date |
| `createdAt` | Instant | created automatically and never supplied by a client |

Database-level protection against race conditions: add a unique constraint on `(desk_id, date)`. Service-level validation will return the intended `409 Conflict`; the database constraint remains the final guard.

## 4. API design

All API failures return a compact JSON body, for example:

```json
{ "status": 400, "error": "Bad Request", "message": "employeeName must not be blank" }
```

| Endpoint | Behaviour |
| --- | --- |
| `GET /api/health` | Returns `200 {"status":"ok"}`. |
| `GET /api/desks?floor=&hasMonitor=` | Lists desks; both optional filters may be combined. Each item contains `id`, `code`, `floor`, `hasMonitor`, and `active`. |
| `POST /api/bookings` | Validates `deskId`, non-blank `employeeName`, and ISO `date`. Returns `201` with the booking; `404` unknown desk; `400` invalid input or inactive desk; `409` duplicate desk/date. |
| `GET /api/bookings?date=YYYY-MM-DD` | Requires a valid date and returns readable booking rows including desk ID and code. |
| `DELETE /api/bookings/{id}` | Removes the booking and returns `204`; returns `404` when absent. |
| `GET /api/desks/availability?date=YYYY-MM-DD&floor=&hasMonitor=` | **Additional feature.** Lists active desks not booked for the selected date, with optional filters. Invalid/missing date is `400`. |

Request and response DTOs will be used throughout. `RowMapper` implementations will translate JDBC result rows to domain records, keeping SQL, relationships, and timestamps explicit and stable.

## 5. Seed-data plan

Seed 10 desks across floors 2, 3, and 4. Include a mix of monitored and unmonitored desks and one inactive desk. Seed three bookings on a fixed demo-friendly future date, plus one on another date.

This makes it easy to demonstrate each state without manual database setup:

- availability removes booked desks;
- an inactive desk is visible but cannot be booked;
- a successful new booking and cancellation can be demonstrated live.

## 6. Frontend design

Deliver one responsive page at `/` with:

1. a date picker defaulting to today (or the seeded demo date when documented);
2. optional floor and monitor filters;
3. an availability grid with desk code, floor, monitor badge, and a **Book** action;
4. a compact booking form that requests employee name and confirms the selected desk/date;
5. success/error feedback and refresh after booking or cancellation; and
6. a bookings-for-date section with a **Cancel** action.

The page will use `fetch` against the same-origin API, avoiding CORS setup and frontend build tooling.

## 7. Implementation sequence

1. **Scaffold and configure** — create the Maven Spring Boot project, add Web, Validation, JDBC, MySQL Driver, and test dependencies; configure MySQL and SQL initialization.
2. **Schema and repository layer** — create `schema.sql` with the tables, foreign key, and unique `(desk_id, date)` constraint; add `data.sql`; implement repositories with `JdbcTemplate` and `RowMapper` classes.
3. **Service and API layer** — implement DTOs, services, controllers, validation, and global exception mapping for all required endpoints plus availability.
4. **Frontend** — add static page assets and connect the booking, availability, and cancellation flows to the API.
5. **Automated verification** — add unit/service tests for validation and duplicate handling, repository tests against MySQL/Testcontainers where available, plus controller/integration tests for the status-code contract.
6. **Demo readiness** — write a short presentation outline and a reproducible demo script; verify startup and endpoints against local MySQL.

## 8. Verification checklist

- `GET /api/health` reports `ok`.
- Seed data is present after a clean application start.
- Desk filtering works for `floor` and `hasMonitor`.
- Valid booking returns `201` and appears in date-specific bookings.
- Unknown desk returns `404`; inactive desk and invalid input return `400`.
- Repeating the same desk/date returns `409`, including under concurrent attempts.
- Availability excludes inactive and already-booked desks.
- Cancellation returns `204`, and the desk becomes available again.
- The frontend completes the full booking flow against the running API.

## 9. Presentation outline (8–10 minutes, 8–10 slides)

1. Problem and user journey
2. Solution overview and live-demo agenda
3. Architecture and stack
4. Data model and booking integrity rule
5. API contract
6. Availability feature and frontend
7. Live demo: availability, book, conflict, cancel
8. Testing and acceptance checklist
9. Challenges/trade-offs
10. Next steps (authentication, employee directory, recurring bookings)

## 10. Out of scope for this delivery

Authentication, user accounts, production deployment, calendar integration, recurring bookings, and a separate frontend framework. These can be mentioned as future work without delaying the required vertical slice.
