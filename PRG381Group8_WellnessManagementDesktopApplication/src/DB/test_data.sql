-- Insert sample counsellors
INSERT INTO Counsellors (name, specialization, availability)
VALUES 
  ('Dr. Jane Smith', 'Stress Management', TRUE),
  ('Mr. Peter Johnson', 'Depression', TRUE),
  ('Ms. Linda Mokoena', 'Career Guidance', FALSE);

-- Insert sample appointments
INSERT INTO Appointments (student_name, counsellor_id, appointment_date, appointment_time, status)
VALUES 
  ('Thabo Nkosi', 1, '2025-07-18', '09:00:00', 'Scheduled'),
  ('Ayanda Zulu', 2, '2025-07-18', '11:00:00', 'Scheduled'),
  ('Sipho Dlamini', 1, '2025-07-19', '10:30:00', 'Cancelled');

-- Insert sample feedback
INSERT INTO Feedback (student_name, rating, comments)
VALUES 
  ('Thabo Nkosi', 5, 'Excellent support, very professional.'),
  ('Ayanda Zulu', 4, 'Good advice, helped me a lot.'),
  ('Sipho Dlamini', 2, 'Session was too short.');
