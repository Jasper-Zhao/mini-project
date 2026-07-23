# Script 3 — Live Demo & Architectural Diagram
**Presenter: Simon | ~2.5 minutes**

---

## 6. Live Demo

Thanks Tony. Now let me show you the application live.

> *(Open browser at `http://localhost:8080`)*

**Step 1 — Initial load**
The page opens with tomorrow's date pre-filled. Desks load automatically. Point out the desk grid and the "Bookings for this date" section at the bottom.

**Step 2 — Filter by floor and monitor**
Select Floor 3 + "Monitor included" and click "Find desks".
Only Floor 3 desks with a monitor are shown.

**Step 3 — Book a desk**
Click "Choose desk" on `HEL-3F-01`. Type your name (e.g. *Simon*). Click "Book desk".
- Green notice: *"Desk booked successfully."*
- The desk disappears from the available grid.
- It appears in the bookings list below.

**Step 4 — Cancel that booking**
Click "Cancel" on the booking just created.
- Notice confirms cancellation.
- The desk reappears in the available grid immediately.

**Step 5 — Show conflict protection**
Change date to `2026-07-24`. Desk `HEL-2F-01` is already taken by Anna Kowalska (seed data). Try to book it. The API returns **409** and the UI shows:
> *"Desk HEL-2F-01 is already booked on 2026-07-24"*

**Step 6 — Show inactive desk rejection (optional API call)**
Desk `HEL-3F-03` (`id: 6`) is marked inactive. If you POST a booking for it the API returns **400**:
> *"Desk HEL-3F-03 is inactive and cannot be booked"*

---

## 7. Architectural Diagram

Let me step back and show the full picture of how the system is built.

```
┌──────────────────────────────────────────────────────────────┐
│                     Spring Boot Application                  │
│                                                              │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Presentation Layer  (src/main/resources/static/)   │    │
│  │  index.html · styles.css · app.js                   │    │
│  └──────────────────────┬──────────────────────────────┘    │
│                         │ HTTP (fetch API)                   │
│  ┌──────────────────────▼──────────────────────────────┐    │
│  │  Controller Layer                                   │    │
│  │  HealthController · DeskController · BookingController│   │
│  │  @RestController   @Valid input   @ResponseStatus   │    │
│  └──────────────────────┬──────────────────────────────┘    │
│                         │ Method calls                       │
│  ┌──────────────────────▼──────────────────────────────┐    │
│  │  Service Layer                                      │    │
│  │  DeskService · BookingService                       │    │
│  │  Business rules · Validation · Exception throwing   │    │
│  └──────────────────────┬──────────────────────────────┘    │
│                         │ Method calls                       │
│  ┌──────────────────────▼──────────────────────────────┐    │
│  │  Repository Layer                                   │    │
│  │  DeskRepository · BookingRepository                 │    │
│  │  JdbcTemplate · RowMapper · SQL                     │    │
│  └──────────────────────┬──────────────────────────────┘    │
│                         │ JDBC                               │
└─────────────────────────┼────────────────────────────────────┘
                          │
              ┌───────────▼───────────┐
              │      MySQL 8          │
              │  ┌─────┐  ┌────────┐  │
              │  │desk │  │booking │  │
              │  └─────┘  └────────┘  │
              │  UNIQUE(desk_id, date) │
              └───────────────────────┘
```

**Key design decisions:**
- Each layer is strictly separated — controllers have no SQL, repositories have no business rules.
- Java **records** are used for `Desk` and `Booking` — immutable, concise, no boilerplate.
- A global `@RestControllerAdvice` handles all exceptions and converts them to consistent JSON errors.
- The database `UNIQUE (desk_id, booking_date)` constraint is the final safeguard against concurrent double-bookings.

---

## Handoff

Over to **Jasper** for the tech stack, our conclusion, and what comes next.

---
