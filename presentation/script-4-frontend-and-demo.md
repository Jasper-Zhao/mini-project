# Script 4 — Tech Stack, Conclusion & The Road Ahead
**Presenter: Jasper | ~2 minutes**

---

## 8. Tech Stack

Thanks Simon. Let me walk you through the technology choices behind DeskFlow and why we picked them.

| Layer | Technology | Reason |
|---|---|---|
| Language | **Java 17** | LTS release, strong typing, team familiarity. Java records simplify model classes. |
| Framework | **Spring Boot 3.4** | Auto-configuration, embedded Tomcat, validation, and static file serving out of the box. |
| Persistence | **Spring JDBC / JdbcTemplate** | Lightweight — no ORM mapping complexity, SQL stays explicit and readable. |
| Database | **MySQL 8** | Reliable, widely understood. `UNIQUE` constraint enforces the no-double-booking rule at the DB level. |
| Frontend | **Vanilla JS + HTML + CSS** | Zero build step, zero npm. Served directly by Spring Boot. Works in any browser. |
| Build | **Maven 3.9** | Standard Java build tool. `mvn spring-boot:run` starts everything in one command. |
| Testing | **JUnit 5 + Spring Boot Test** | Unit tests cover service-layer rules — inactive desk rejection and conflict detection. |

### Why no ORM (JPA/Hibernate)?

For a project of this scope, JPA would add complexity without benefit. With JdbcTemplate every SQL query is visible in the source. Debugging is straightforward and performance is predictable.

### Why vanilla frontend?

No React, no Vue, no build pipeline to configure or maintain. The entire UI is three files totalling under 200 lines. It loads instantly and has no client-side dependencies to go out of date.

---

## 9. Conclusion

Let me bring this together.

In one day, Group 1 delivered:

- ✅ A running Java REST API connected to MySQL
- ✅ Schema and seed data loaded automatically on startup
- ✅ All 5 required endpoints — health, list desks, create booking, list bookings, cancel booking
- ✅ Correct HTTP status codes — 201, 204, 400, 404, 409
- ✅ Double-booking prevention at both the application and database layer
- ✅ A bonus availability endpoint (`GET /api/desks/availability`)
- ✅ A working single-page frontend that talks to our own API

Every item on the acceptance checklist is green.

---

## 10. The Road Ahead

If we were to continue this project, here is what we would build next:

| Priority | Feature | Why |
|---|---|---|
| High | **User authentication** | Right now anyone can book or cancel any desk. A login system would tie bookings to real employee accounts. |
| High | **Recurring bookings** | "Book every Tuesday for a month" is a common real-world need. |
| Medium | **Admin panel** | Let office managers add/deactivate desks, view utilisation stats, and manage bookings. |
| Medium | **Email / calendar confirmation** | Send an `.ics` file on booking so it appears in the employee's calendar automatically. |
| Low | **Floor map UI** | Show a visual map of the floor instead of a card grid — click a desk on the map to book it. |
| Low | **Docker Compose setup** | A `docker-compose.yml` that spins up both MySQL and the API with one command for easy onboarding. |

DeskFlow is intentionally thin right now — a solid vertical slice. But the layered architecture means any of these features can be added without rewriting what already works.

---

## Closing (all four together)

That's DeskFlow from Group 1 — Ezio, Tony, Simon, and Jasper.

Thank you for listening. We're happy to take any questions.

---
   - Show the red error notice: "Desk HEL-2F-01 is already booked on 2026-07-24".

6. **Show the 400 for inactive desk** — optionally use curl or browser devtools to POST `deskId: 6` (HEL-3F-03 is inactive).
   - API returns 400: "Desk HEL-3F-03 is inactive and cannot be booked".

---

## Closing (all four together)

That's DeskFlow. A running Java API backed by MySQL, seeded data, five required endpoints plus the availability feature, and a working single-page frontend — all built and deployed in one day.

Thank you. Happy to take questions.

---
