INSERT INTO instructor (name, secret_info) VALUES ('Zebbe', 'secret info, only for admin: Is strongest');
INSERT INTO instructor (name, secret_info) VALUES ('Zara', 'secret info, only for admin: Is leanest');
INSERT INTO instructor (name, secret_info) VALUES ('Zoran', 'secret info, only for admin: is meanest');

INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Yoga', '5', 2);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Cardio', '4', 2);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Cardio', '4', 1);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Styrke', '4', 1);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Styrke', '5', 3);


INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Yin Yoga', 'Yoga', 2, 'Stora salen', 22, 10, 200, 19.1, '2025-06-28 17:00:00');

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Hantlar', 'Styrke', 3, 'Lilla salen', 12, 10, 200,  19.2, '2025-06-28 18:00:00');

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Fun run', 'Cardio', 1, 'Ute', 12, 3, 200,  19.1, '2025-09-12 18:30:00');

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Fun run', 'Cardio', 1, 'Lilla salen', 12, 3, 300,29.2, '2025-10-22 16:15:00');

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Hatha Yoga', 'Yoga', 2, 'Stora salen', 22, 10, 200,  19.1, '2025-11-28 17:00:00');



INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time)
VALUES ('Power lift', 'Styrke', 3, 'Lilla salen', 12, 3, 200,  19.1, '2025-10-26 17:30:00');

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','19850101-1234', 1, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','19751230-9101', 3, 200, 19, true);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','19850101-1234', 3, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','19751230-9101', 3, 200, 19, true);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','19850101-1234', 1, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:002','19751230-9101', 3, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','19850101-1234', 1, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 18:00:00','19751230-9101', 5, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-26 17:30:00','19850101-1234', 2, 200, 19, false);

INSERT INTO booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-26 17:30:00','19751230-9101', 2, 200, 19,false);