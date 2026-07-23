# Speaker 3 — Slides 8–9

Estimated time: 2 minutes

## Slide 8 — A pragmatic stack supports a thin, working slice

Our technology choices support the goal of a simple, working solution.

We use Java 17 and Spring Boot 3.4 for the backend, including the web, validation, and JDBC modules. MySQL provides the relational database. Spring JDBC lets us use explicit SQL and row mapping without introducing unnecessary complexity.

For the frontend, we use vanilla JavaScript. This means the browser interface needs no separate build tool and can be served directly by Spring Boot.

Finally, Maven manages the build and JUnit supports service-level tests. Together, this stack helped us focus on correct behavior instead of infrastructure overhead.

## Slide 9 — DeskFlow meets the core hot-desk booking brief

DeskFlow meets the required acceptance points for the project.

The health endpoint returns an OK status. The API supports listing desks, creating bookings, listing bookings for a date, and cancelling a booking. We also added availability as our additional feature, so users can directly ask which active desks are free on a given date, with optional filters.

The key integrity rule is one booking per desk per date. This is enforced in the service layer and again in the database.

The frontend completes the full user journey in one place: browse available desks, make a booking, and cancel it when needed.

I’ll hand over to Speaker 4 for our future direction and conclusion.
