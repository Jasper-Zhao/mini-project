INSERT IGNORE INTO desk (id, code, floor, has_monitor, is_active) VALUES
    (1, 'HEL-2F-01', 2, TRUE, TRUE),
    (2, 'HEL-2F-02', 2, FALSE, TRUE),
    (3, 'HEL-2F-03', 2, TRUE, TRUE),
    (4, 'HEL-3F-01', 3, TRUE, TRUE),
    (5, 'HEL-3F-02', 3, FALSE, TRUE),
    (6, 'HEL-3F-03', 3, TRUE, FALSE),
    (7, 'HEL-4F-01', 4, FALSE, TRUE),
    (8, 'HEL-4F-02', 4, TRUE, TRUE),
    (9, 'HEL-4F-03', 4, FALSE, TRUE),
    (10, 'HEL-4F-04', 4, TRUE, TRUE);

INSERT IGNORE INTO booking (id, desk_id, employee_name, booking_date, created_at) VALUES
    (1, 1, 'Anna Kowalska', '2026-07-24', '2026-07-22 09:00:00'),
    (2, 4, 'Mika Laine', '2026-07-24', '2026-07-22 09:05:00'),
    (3, 8, 'Sofia Nieminen', '2026-07-24', '2026-07-22 09:10:00'),
    (4, 2, 'Olli Virtanen', '2026-07-25', '2026-07-22 09:15:00');
