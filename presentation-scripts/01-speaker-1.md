# Speaker 1 — Slides 1–4

Estimated time: 3 minutes

## Slide 1 — A desk, when you need it

Hello everyone. We are the DeskFlow team, and our project is a hot-desk booking application for the Helsinki office.

The idea is simple: when an employee plans to work in the office, they should be able to find a suitable desk for a specific date and reserve it in a few clicks. At the same time, the system must prevent two people from booking the same desk on the same day.

DeskFlow delivers that complete journey using Spring Boot, JDBC, MySQL, and a lightweight browser interface.

## Slide 2 — The team behind the vertical slice

Here is how we divided the project work.

The API Lead focused on the Spring Boot API, REST controllers, and validation. The Data Lead focused on the schema, seed data, and booking integrity rules. The Feature Lead implemented the availability experience and browser flow. Finally, the Demo and Slides Lead prepared the testing story, live demonstration, and presentation.

## Slide 3 — Our project makes hot-desk booking simple and safe

The problem we focused on is everyday workspace coordination. Employees need a quick and reliable answer to one question: which desk can I use on this date?

DeskFlow turns that into one clear journey. First, the employee chooses a date. Next, they see desks that are free for that date. Then they make a booking. If their plans change, they can cancel it and the desk becomes available again.

We intentionally kept the scope focused on this vertical slice. Instead of adding many unfinished features, we made the core availability, booking, and cancellation flow work end to end.

## Slide 4 — System flow protects every booking

This is the flow behind a booking request.

The browser interface sends the selected desk, employee name, and date to the REST API. The controller validates the request format, and the booking service applies the business rules. It checks that the desk exists, that it is active, and that it has not already been booked for that date.

Finally, MySQL stores the booking. The database has a unique constraint on desk ID and booking date, so it provides a final safeguard even if two requests arrive at almost the same time.

That gives us clear outcomes: a successful booking returns 201, a duplicate booking returns 409, and an inactive desk returns 400.

I’ll now hand over to Speaker 2, who will show the user interface, demo flow, and system architecture.
