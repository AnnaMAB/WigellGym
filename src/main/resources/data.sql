INSERT INTO instructor (name, secret_info) VALUES ('Zebbe', 'secret info, only for admin: Is strongest');
INSERT INTO instructor (name, secret_info) VALUES ('Zara', 'secret info, only for admin: Is leanest');
INSERT INTO instructor (name, secret_info) VALUES ('Zoran', 'secret info, only for admin: is meanest');

INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Yoga', '5', 2);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Cardio', '4', 2);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Cardio', '4', 1);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Styrke', '4', 1);
INSERT INTO speciality (type, certificate_level, instructor_id) VALUES ('Styrke', '5', 3);


INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Yin Yoga', 'Yoga', 2, 'Stora salen', 22, 20, 200, 19.1, '2025-06-28 17:00:00', '2025-06-28 17:45:00', false, 1.5);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Hantlar', 'Styrke', 3, 'Lilla salen', 12, 10, 300,  19.2, '2025-06-28 18:00:00', '2025-06-28 18:45:00', false, 1.5);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Fun run', 'Cardio', 1, 'Ute', 12, 10, 200,  19.1, '2025-09-12 18:30:00', '2025-09-12 19:15:00',false, 1.4);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Fun run', 'Cardio', 1, 'Ute', 12, 10, 200,19.1, '2025-10-22 16:15:00', '2025-10-22 16:45:00',false, 1.4);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Hatha Yoga', 'Yoga', 2, 'Stora salen', 22, 20, 200,  19.1, '2025-11-28 17:00:00', '2025-11-28 17:45:00', false, 1.5);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Power lift', 'Styrke', 3, 'Lilla salen', 12, 10, 300,  29.2, '2025-10-26 17:30:00', '2025-10-26 18:15:00', false, 1.5);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Obokad uppcoming', 'Test pass', 3, 'Lilla salen', 12, 10, 300,  29.2, '2025-10-27 17:30:00', '2025-10-27 18:30:00', false, 1.2);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Obokad passed', 'Test pass', 3, 'Lilla salen', 12, 10, 300,  29.2, '2025-08-28 17:30:00', '2025-08-28 17:50:00', false, 1.2);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Obokad idag toSoon', 'Test pass', 3, 'Lilla salen', 12, 10, 300,  29.2, '2025-09-24 12:30:00', '2025-09-24 13:30:00', false, 1.2);

INSERT INTO workout (name, type_of_workout, instructor_id, location, max_participants, free_spots, price_sek, preliminary_price_euro, date_time, end_time, cancelled, instructor_skill_price_multiplier)
VALUES ('Obokad idag ikväll', 'Test pass', 3, 'Lilla salen', 12, 10, 300,  29.2, '2025-09-24 17:30:00', '2025-09-24 18:30:00', false, 1.2);


----------1---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','anna.andersson@mail.se', 1, 200, 19.2, true);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','erik.eriksson@mail.se', 1, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 17:00:00','maria.malm@mail.se', 1, 200, 19.2, false);

----------2---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 18:00:00','anna.andersson@mail.se', 2, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 18:00:00','erik.eriksson@mail.se', 2, 200, 19.2, true);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-06-28 18:00:00','maria.malm@mail.se', 2, 200, 19.2, false);

----------3---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','anna.andersson@mail.se', 3, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','erik.eriksson@mail.se', 3, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-09-12 18:30:00','maria.malm@mail.se', 3, 200, 19.2, true);

----------4---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-22 16:15:00','anna.andersson@mail.se', 4, 200, 19.2, true);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-22 16:15:00','erik.eriksson@mail.se', 4, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-22 16:15:00','maria.malm@mail.se', 4, 200, 19.2, false);

----------5---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-11-28 17:00:00','anna.andersson@mail.se', 5, 200, 19.2, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-11-28 17:00:00','erik.eriksson@mail.se', 5, 200, 19.2, true);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-11-28 17:00:00','maria.malm@mail.se', 5, 200, 19.2, false);

----------6---------------
INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-26 17:30:00','anna.andersson@mail.se', 6, 300, 29.4, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-26 17:30:00','erik.eriksson@mail.se', 6, 300, 29.4, false);

INSERT INTO gym_booking (booking_date, workout_date, customer_username, workout_id, total_price_sek, total_price_euro, cancelled)
VALUES ('2025-06-22 18:36:55','2025-10-26 17:30:00','maria.malm@mail.se', 6, 300, 29.4, true);
