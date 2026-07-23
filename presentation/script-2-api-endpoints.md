# Script 2 — System Flow & Screenshots
**Presenter: Tony | ~2.5 minutes**

---

## 4. System Flow

Thanks Jasper. Let me walk you through how the whole system fits together — from the moment a user opens the page to the moment a booking is confirmed.

```
┌─────────────┐    HTTP/JSON    ┌──────────────────────────────────────────┐
│  Browser UI │ ◄────────────► │           Spring Boot (port 8080)        │
│ (index.html │                │                                          │
│  app.js     │                │  ┌────────────┐   ┌──────────────────┐  │
│  styles.css)│                │  │ Controller │──►│    Service       │  │
└─────────────┘                │  │  (HTTP)    │   │  (Business Logic)│  │
                                │  └────────────┘   └────────┬─────────┘  │
                                │                            │             │
                                │                   ┌────────▼─────────┐  │
                                │                   │   Repository     │  │
                                │                   │  (JdbcTemplate)  │  │
                                │                   └────────┬─────────┘  │
                                └────────────────────────────┼────────────┘
                                                             │ SQL
                                                    ┌────────▼─────────┐
                                                    │   MySQL 8        │
                                                    │  desk  booking   │
                                                    └──────────────────┘
```

**Step-by-step user journey:**

1. **User opens** `http://localhost:8080` → Spring Boot serves `index.html`
2. **Page loads** → `app.js` fires two parallel API calls:
   - `GET /api/desks/availability?date=…` — free desks for today
   - `GET /api/bookings?date=…` — existing bookings
3. **User picks a desk** → desk card is highlighted, name field is unlocked
4. **User submits form** → `POST /api/bookings` is sent with `deskId`, `employeeName`, `date`
5. **Server validates** → checks desk exists, is active, is not already booked
6. **201 Created** → page reloads automatically; booked desk disappears from the grid
7. **User cancels** → `DELETE /api/bookings/{id}` → **204 No Content** → desk reappears

At every error point the API returns a consistent JSON error with a `message` field. The UI picks that up and shows it in a notice bar — no guessing what went wrong.

---

## 5. Screenshots

> *(Switch to browser — `http://localhost:8080`)*

**Screenshot 1 — Page on load**
Point out: the page header "Helsinki office / Find your desk", the date input (pre-filled), and the floor / monitor filter dropdowns. The available desk grid is already populated from seed data.

**Screenshot 2 — Filtered by Floor 3**
Select Floor 3 and click "Find desks". The grid shrinks to show only Floor 3 desks. The count badge updates (e.g. "2 free").

**Screenshot 3 — Desk selected**
Click "Choose desk" on one card. The card is highlighted, the "Create a booking" panel becomes active — name field is enabled, the Book button is enabled.

**Screenshot 4 — Booking confirmed**
Type a name and click "Book desk". A green success notice appears: "Desk booked successfully." The booked desk disappears from the available grid and appears in the "Bookings for this date" list below.

**Screenshot 5 — Error state (409 conflict)**
Navigate to date `2026-07-24`. Desk `HEL-2F-01` is already booked by Anna Kowalska in the seed data. Try to book it anyway. A red notice shows:
> *"Desk HEL-2F-01 is already booked on 2026-07-24"*

---

## Handoff

Over to **Simon** for the live demo and a look at the architectural diagram.

---
