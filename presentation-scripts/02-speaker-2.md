# Speaker 2 — Slides 5–7

Estimated time: 3 minutes

## Slide 5 — The browser interface keeps the journey visible

This is the DeskFlow browser interface. It starts with filters for date, floor, and monitor availability.

When the user selects a date, the page calls the availability endpoint and shows only active desks that are not already booked. The user can then choose a desk, enter a name, and create a booking.

The same page also shows existing bookings for the selected date and provides a cancel action. After a booking or cancellation, the page refreshes both the available desks and the booking list, so the user immediately sees the updated state.

For the seeded demo date, 24 July 2026, three desks are already booked and one desk is inactive, leaving six available desks. This makes the application behavior easy to demonstrate without manual setup.

## Slide 6 — Demo: prove the complete booking loop

For the demo, we show the complete booking loop in four steps.

First, we select 24 July 2026 and load the free desks. Second, we choose one available desk and create a booking with an employee name. The new booking appears in the booking list, and that desk disappears from availability.

Third, we try to book the same desk again for the same date. The API correctly returns a 409 conflict, proving that duplicate bookings are prevented.

Finally, we cancel the booking. The booking is removed, and the desk becomes available again.

This demonstration shows that the main user journey works and that the system handles the important failure case as well.

## Slide 7 — Architecture stays deliberately small and traceable

The architecture is deliberately small so that each layer has a clear responsibility.

The browser uses plain HTML, CSS, and JavaScript. It sends same-origin requests to the Spring Boot application. Spring Boot contains the controllers and services, where requests are validated and booking rules are applied.

The repository layer uses Spring JDBC. This keeps our SQL explicit and makes it easy to see how application records map to database rows.

At the bottom is MySQL, with a `desk` table and a `booking` table. The booking table references the desk table, and the unique desk-and-date rule prevents duplicate reservations. A central exception handler returns compact, consistent JSON errors across the API.

Next, Speaker 3 will cover the technology choices and how DeskFlow meets the core brief.
