CREATE TABLE IF NOT EXISTS desk (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL UNIQUE,
    floor INT NOT NULL,
    has_monitor BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    desk_id BIGINT NOT NULL,
    employee_name VARCHAR(120) NOT NULL,
    booking_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_desk FOREIGN KEY (desk_id) REFERENCES desk(id),
    CONSTRAINT uq_booking_desk_date UNIQUE (desk_id, booking_date)
);
