# Mini-Project: DeskFlow — Hot Desk Booking API

**Theme:** Help the Finnish office manage hot desks. Employees need a simple way to see which desks are free and book one for a given day to work.

**Objective:** As a group, build a small Java REST API backed by a database, then present your work in **8~10 minutes**.

---

## Outcome

By the end of the day your team should have:

1. A running Java API (Spring Boot recommended) connected to a database
2. Seed data so a demo works without manual setup

---

## Tech expectations

Keep the stack simple:

- **Java** + **Spring Boot** (or equivalent REST framework you already know)
- **Database:** **MySQL recommended**; H2, PostgreSQL, or another DB is fine if you prefer
- **Persistence:** JPA / JDBC — your choice
- **API style:** JSON over HTTP

You do **not** need authentication, Docker, or cloud deploy for the core brief. A frontend is **not** required for the core — see creative bonus below.

---

## Domain model (prescribed)

Use at least these concepts. Column names can vary; meaning should not.

### `desk`

| Field         | Notes                                     |
| ------------- | ----------------------------------------- |
| `id`          | Primary key                               |
| `code`        | Unique short code, e.g. `KRK-3F-12`       |
| `floor`       | Integer floor number                      |
| `has_monitor` | Boolean                                   |
| `is_active`   | Boolean — inactive desks cannot be booked |

### `booking`

| Field           | Notes                                  |
| --------------- | -------------------------------------- |
| `id`            | Primary key                            |
| `desk_id`       | FK → desk                              |
| `employee_name` | String                                 |
| `date`          | Booking date (one day per booking)     |
| `created_at`    | Timestamp when the booking was created |

**Core rule:** A desk may have **at most one booking per date**.

Seed at least **8 desks** across **2+ floors**, and a few sample bookings.

---

## Required API (follow these contracts)

Implement these so you can demo clear, consistent behaviour.

### 1. Health

```http
GET /api/health
```

**200** response example:

```json
{ "status": "ok" }
```

### 2. List desks

```http
GET /api/desks
```

Optional query params (implement at least one):

- `floor` — filter by floor
- `hasMonitor` — `true` / `false`

**200** — array of desks (include `id`, `code`, `floor`, `hasMonitor`, `active`).

### 3. Create a booking

```http
POST /api/bookings
Content-Type: application/json
```

Request body:

```json
{
    "deskId": 1,
    "employeeName": "Anna Kowalska",
    "date": "2026-07-24"
}
```

Behaviour:

- **201** + created booking JSON on success
- **404** if desk does not exist
- **400** if desk is inactive, date is missing/invalid, or `employeeName` is blank
- **409** if that desk is already booked on that date

### 4. List bookings for a date

```http
GET /api/bookings?date=2026-07-24
```

**200** — array of bookings for that date (include desk `code` or `deskId` so the demo is readable).

### 5. Cancel a booking

```http
DELETE /api/bookings/{id}
```

- **204** (or **200** with a short message) if deleted
- **404** if booking id does not exist

---

## Additional Tasks

Ship **one+** additional API feature that fits the DeskFlow theme. Your should designs the endpoint(s), request/response shape, and rules.

- Availability: “which desks are free on date X?”


you should also design and create a small **frontend** (even a single HTML page) that talks to your API — e.g. list free desks for a date and create a booking.

---

## Done means (acceptance checklist)

- [ ] App starts and `/api/health` returns ok
- [ ] Seed data loads on startup
- [ ] All 5 required endpoints work
- [ ] Double-booking the same desk/date returns **409**
- [ ] At least one creative feature works end-to-end
- [ ] Slide deck ready (**8–15 slides**, **10-minute** presentation)

---

## Constraints (on purpose)

- Stay inside the **hot desk booking** theme — no unrelated apps
- Prefer a thin, working vertical slice over a large unfinished design

---