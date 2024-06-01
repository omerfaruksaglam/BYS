CREATE TABLE User_Roles (
                            user_roles_id SERIAL PRIMARY KEY,
                            userType VARCHAR(50)
);

CREATE TABLE University (
                            uni_id SERIAL PRIMARY KEY,
                            Uni_name VARCHAR(255) NOT NULL,
                            Uni_logo_url VARCHAR(255)
);

CREATE TABLE Faculty (
                         fac_id SERIAL PRIMARY KEY,
                         Fac_name VARCHAR(255),
                         uni_id INTEGER REFERENCES University(uni_id) ON DELETE CASCADE
);

CREATE TABLE SecondGrader (
                              second_grader_id SERIAL PRIMARY KEY,
                              second_grader_name VARCHAR(255)
);

CREATE TABLE SecondGraderSinif (
                                   second_grader_id Integer REFERENCES SecondGrader(second_grader_id) ON DELETE CASCADE,
                                   sinif_numarasi Integer,
                                   PRIMARY KEY(second_grader_id, sinif_numarasi)
);

CREATE TABLE Bolum (
                       bolum_id SERIAL PRIMARY KEY,
                       bolum_name VARCHAR(255),
                       yil_sayisi INTEGER,
                       ogrenci_not_Verebilirmi BOOLEAN,
                       malzemeler_varmi BOOLEAN,
                       second_grader_id INTEGER REFERENCES SecondGrader(second_grader_id),
                       fac_id INTEGER REFERENCES Faculty(fac_id) ON DELETE CASCADE
);


CREATE TABLE Users (
                       user_id INTEGER,
                       namee VARCHAR(255) NOT NULL,
                       surname VARCHAR(255) NOT NULL,
                       kayit_tarihi DATE,
                       passwordd VARCHAR(255),
                       user_roles_id INTEGER REFERENCES User_Roles(user_roles_id) ON DELETE CASCADE,
                       bolum_id INTEGER REFERENCES Bolum(bolum_id) ON DELETE CASCADE,
                       isActive bool,
                       Primary Key(user_id, bolum_id)

);


CREATE TABLE Skills (
                        skill_id SERIAL PRIMARY KEY,
                        namee VARCHAR(255) NOT NULL,
                        Step_no INTEGER,
                        bolum_id INTEGER REFERENCES Bolum(bolum_id) ON DELETE CASCADE
);

CREATE TABLE Subskills (
                           subskill_id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           skill_id INTEGER REFERENCES Skills(skill_id) ON DELETE CASCADE,
                           Step_no INTEGER
);

CREATE TABLE Equipment (
                           equipment_id SERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           subskill_id INTEGER REFERENCES Subskills(subskill_id) ON DELETE CASCADE
);

CREATE TABLE SubskillSteps (
                               subskill_steps_id SERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               step_no INTEGER,
                               subskill_id INTEGER REFERENCES Subskills(subskill_id) ON DELETE CASCADE,
                               isHead BOOL,
                               Weight INTEGER DEFAULT 1
);

CREATE TABLE Statisticss (
                             class_level INTEGER,
                             success_rate DECIMAL(5, 2),
                             student_id INTEGER,
                             bolum_id INTEGER,
                             skill_id INTEGER REFERENCES Skills(skill_id) ON DELETE CASCADE,
                             FOREIGN KEY (student_id, bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                             PRIMARY KEY (student_id, skill_id, class_level, bolum_id)
);


CREATE TABLE Performs (
                          student_id INTEGER,
                          bolum_id INTEGER,
                          subskill_steps_id INTEGER REFERENCES subskillsteps(subskill_steps_id) ON DELETE CASCADE,
                          perform_date timestamp with time zone,
                          class_level INTEGER,
                          grade DECIMAL(5,2),
                          FOREIGN KEY (student_id, bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                          PRIMARY KEY (student_id, subskill_steps_id, class_level, bolum_id)
);
CREATE TABLE grades (
                        student_id INTEGER,
                        student_bolum_id INTEGER,
                        grader_id INTEGER,
                        grader_bolum_id INTEGER,
                        grader_role INTEGER,
                        subskill_steps_id INTEGER REFERENCES subskillsteps(subskill_steps_id) ON DELETE CASCADE,
                        grade_date timestamp with time zone,
                        class_level INTEGER,
                        grade DECIMAL(5,2),
                        FOREIGN KEY (student_id, student_bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                        FOREIGN KEY (grader_id, grader_bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                        PRIMARY KEY (student_id, subskill_steps_id, class_level, student_bolum_id, grader_role)
);

CREATE OR REPLACE FUNCTION check_grader_role()
RETURNS TRIGGER AS $$
DECLARE
v_user_roles_id INTEGER;
BEGIN
  -- Find the user_roles_id for the grader
SELECT user_roles_id INTO v_user_roles_id
FROM Users
-- Check if the user_roles_id is valid (0 or 2)
WHERE user_id = NEW.grader_id AND bolum_id = NEW.grader_bolum_id;

IF v_user_roles_id IS NULL OR (v_user_roles_id <> 0 AND v_user_roles_id <> 2) THEN
    RAISE EXCEPTION 'Invalid grader role. Must be 0 or 2.';
ELSE
    -- Set the grader_role to the found user_roles_id
    NEW.grader_role := v_user_roles_id;
	--NEW.grade_date := CURRENT_TIMESTAMP;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_grader_role
    BEFORE INSERT OR UPDATE ON grades
                         FOR EACH ROW
                         EXECUTE FUNCTION check_grader_role();

CREATE TABLE StudentSubSkillGrade (
                                      student_id INTEGER,
                                      bolum_id INTEGER,
                                      subskill_id INTEGER REFERENCES subskills(subskill_id) ON DELETE CASCADE,
                                      perform_date timestamp with time zone,
                                      class_level INTEGER,
                                      grade DECIMAL(5,2),
                                      FOREIGN KEY (student_id, bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                                      PRIMARY KEY (student_id, subskill_id, class_level, bolum_id)
);
CREATE TABLE GraderStudentSubSkillGrade (
                                            student_id INTEGER,
                                            student_bolum_id INTEGER,
                                            grader_role INTEGER,
                                            subskill_id INTEGER REFERENCES subskills(subskill_id) ON DELETE CASCADE,
                                            grade_date timestamp with time zone,
                                            class_level INTEGER,
                                            grade DECIMAL(5,2),
                                            FOREIGN KEY (student_id, student_bolum_id) REFERENCES Users(user_id, bolum_id) ON DELETE CASCADE,
                                            PRIMARY KEY (student_id, subskill_id, class_level, student_bolum_id, grader_role)
);

CREATE TABLE login_records(
                              actiontime timestamp with time zone NOT NULL,
                              ip_address VARCHAR(255) NOT NULL,
                              requested_user_id INTEGER NOT NULL,
                              requested_bolum_id INTEGER NOT NULL,
                              isSuccess BOOL NOT NULL,
                              failedReason VARCHAR(255),
                              PRIMARY KEY (actiontime, requested_user_id, requested_bolum_id)
);

CREATE TABLE action_records(
                               actiontime timestamp with time zone NOT NULL,
                               ip_address VARCHAR(255) NOT NULL,
                               userid INTEGER NOT NULL,
                               bolumid INTEGER NOT NULL,
                               actiontext text not null,
                               PRIMARY KEY (actiontime, userid, bolumid)
);





CREATE OR REPLACE FUNCTION update_student_subskill_grade()
RETURNS TRIGGER AS $$
DECLARE
subskill_id_for_step INTEGER;
    total_steps INTEGER;
    completed_steps INTEGER;
    total_weight INTEGER;
    total_weighted_grade DECIMAL(10,4);
    average_grade DECIMAL(5,2);
BEGIN
    -- subskillsteps_id kullanarak subskill_id'yi bul
SELECT subskill_id INTO subskill_id_for_step
FROM SubskillSteps
WHERE subskill_steps_id = NEW.subskill_steps_id;

RAISE NOTICE 'Subskill ID for step: %', subskill_id_for_step;

    -- Belirli bir subskill için isHead değeri false olan subskillsteps sayısını bul
SELECT COUNT(*) INTO total_steps
FROM SubskillSteps
WHERE subskill_id = subskill_id_for_step AND isHead = false;

RAISE NOTICE 'Total steps (isHead = FALSE): %', total_steps;

    -- Öğrencinin tamamladığı subskillsteps sayısını ve ortalama notunu bul
SELECT COUNT(DISTINCT Performs.subskill_steps_id) INTO completed_steps
FROM Performs
         JOIN SubskillSteps ON Performs.subskill_steps_id = SubskillSteps.subskill_steps_id AND SubskillSteps.isHead = FALSE
WHERE Performs.student_id = NEW.student_id
  AND Performs.bolum_id = NEW.bolum_id
  AND SubskillSteps.subskill_id = subskill_id_for_step
  AND Performs.class_level = NEW.class_level
GROUP BY SubskillSteps.subskill_id;

-- completed_Steps += 1;
RAISE NOTICE 'Completed steps: %, Average grade: %', completed_steps, average_grade;

    -- Eğer öğrenci tüm adımları (isHead değeri false olan) tamamladıysa
    IF completed_steps >= total_steps THEN
SELECT SUM(Weight), SUM(Performs.grade * SubskillSteps.Weight) INTO total_weight, total_weighted_grade
FROM Performs
         INNER JOIN SubskillSteps ON Performs.subskill_steps_id = SubskillSteps.subskill_steps_id
WHERE Performs.student_id = NEW.student_id AND Performs.bolum_id = NEW.bolum_id AND SubskillSteps.subskill_id = subskill_id_for_step AND Performs.class_level = NEW.class_level;


IF total_weight > 0 THEN
            average_grade := ROUND((total_weighted_grade / total_weight)::numeric);
            -- Ağırlıklı ortalama notu StudentSubSkillGrade tablosuna kaydet
INSERT INTO StudentSubSkillGrade(student_id, bolum_id, subskill_id, perform_date, class_level, grade)
VALUES (NEW.student_id, NEW.bolum_id, subskill_id_for_step, new.perform_date, NEW.class_level, average_grade)
    ON CONFLICT (student_id, subskill_id, class_level, bolum_id)
            DO UPDATE SET grade = average_grade, perform_date = new.perform_date;
END IF;
ELSE
        RAISE NOTICE 'Not all steps completed for student_id: %, subskill_id: %', NEW.student_id, subskill_id_for_step;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_student_subskill_grade
    AFTER INSERT OR UPDATE ON Performs
                        FOR EACH ROW
                        EXECUTE FUNCTION update_student_subskill_grade();




CREATE OR REPLACE FUNCTION update_student_grader_subskill_grade()
RETURNS TRIGGER AS $$
DECLARE
subskill_id_for_step INTEGER;
    total_steps INTEGER;
    completed_steps INTEGER;
    total_weight INTEGER;
    total_weighted_grade DECIMAL(10,4);
    average_grade DECIMAL(5,2);
BEGIN
    -- subskillsteps_id kullanarak subskill_id'yi bul
SELECT subskill_id INTO subskill_id_for_step
FROM SubskillSteps
WHERE subskill_steps_id = NEW.subskill_steps_id;

RAISE NOTICE 'Subskill ID for step: %', subskill_id_for_step;

    -- Belirli bir subskill için isHead değeri false olan subskillsteps sayısını bul
SELECT COUNT(*) INTO total_steps
FROM SubskillSteps
WHERE subskill_id = subskill_id_for_step AND isHead = false;

RAISE NOTICE 'Total steps (isHead = FALSE): %', total_steps;

    -- Öğrencinin tamamladığı subskillsteps sayısını ve ortalama notunu bul
SELECT COUNT(DISTINCT Grades.subskill_steps_id) INTO completed_steps
FROM Grades
         JOIN SubskillSteps ON Grades.subskill_steps_id = SubskillSteps.subskill_steps_id AND SubskillSteps.isHead = FALSE
WHERE Grades.student_id = NEW.student_id
  AND Grades.student_bolum_id = NEW.student_bolum_id
  AND SubskillSteps.subskill_id = subskill_id_for_step
  AND Grades.class_level = NEW.class_level
  AND Grades.grader_role = NEW.grader_role
GROUP BY SubskillSteps.subskill_id;

-- completed_Steps += 1;
RAISE NOTICE 'Completed steps: %, Average grade: %', completed_steps, average_grade;

    -- Eğer öğrenci tüm adımları (isHead değeri false olan) tamamladıysa
    IF completed_steps >= total_steps THEN
SELECT SUM(SubskillSteps.Weight) INTO total_weight
FROM SubskillSteps
WHERE subskill_id = subskill_id_for_step AND isHead = false;


SELECT SUM(Grades.grade * SubskillSteps.Weight) INTO total_weighted_grade
FROM Grades
         JOIN SubskillSteps ON Grades.subskill_steps_id = SubskillSteps.subskill_steps_id AND SubskillSteps.isHead = FALSE
WHERE Grades.student_id = NEW.student_id
  AND Grades.student_bolum_id = NEW.student_bolum_id
  AND SubskillSteps.subskill_id = subskill_id_for_step
  AND Grades.class_level = NEW.class_level
  AND Grades.grader_role = NEW.grader_role;


IF total_weight > 0 THEN
            average_grade := ROUND((total_weighted_grade / total_weight)::numeric);

        RAISE NOTICE 'Updating StudentSubSkillGrade for student_id: %, subskill_id: %', NEW.student_id, subskill_id_for_step;
        -- StudentSubSkillGrade tablosunu güncelle veya yeni kayıt ekle
INSERT INTO GraderStudentSubSkillGrade(student_id, student_bolum_id, grader_role, subskill_id, grade_date, class_level, grade)
VALUES (NEW.student_id, NEW.student_bolum_id, NEW.grader_role, subskill_id_for_step, new.grade_date, NEW.class_level, ROUND(average_grade))
    ON CONFLICT (student_id, student_bolum_id, grader_role, subskill_id, class_level)
        DO UPDATE SET grade = average_grade, grade_date = new.grade_date;
END IF;
ELSE
        RAISE NOTICE 'Not all steps completed for student_id: %, subskill_id: %', NEW.student_id, subskill_id_for_step;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_update_grader_student_subskill_grade
    AFTER INSERT OR UPDATE ON Grades
                        FOR EACH ROW
                        EXECUTE FUNCTION update_student_grader_subskill_grade();

select ROUND(3.50);

CREATE OR REPLACE FUNCTION update_student_skill_statistics()
RETURNS TRIGGER AS $$
DECLARE
skill_id_for_subskill INTEGER;
    total_subskills INTEGER;
    completed_subskills INTEGER;
    average_grade DECIMAL(5,2);
BEGIN
    -- subskill_id kullanarak skill_id'yi bul
SELECT skill_id INTO skill_id_for_subskill
FROM Subskills
WHERE subskill_id = NEW.subskill_id;

RAISE NOTICE 'Skill ID for subskill: %', skill_id_for_subskill;

    -- Belirli bir skill için tüm subskills sayısını bul
SELECT COUNT(*) INTO total_subskills
FROM Subskills
WHERE skill_id = skill_id_for_subskill;

RAISE NOTICE 'Total subskills: %', total_subskills;

    -- Öğrencinin tamamladığı subskills sayısını ve ortalama notunu bul
SELECT COUNT(DISTINCT GraderStudentSubSkillGrade.subskill_id), AVG(GraderStudentSubSkillGrade.grade) INTO completed_subskills, average_grade
FROM GraderStudentSubSkillGrade
WHERE GraderStudentSubSkillGrade.student_id = NEW.student_id
  AND GraderStudentSubSkillGrade.student_bolum_id = NEW.student_bolum_id
  AND GraderStudentSubSkillGrade.class_level = NEW.class_level
  AND GraderStudentSubSkillGrade.grader_role = 0
GROUP BY GraderStudentSubSkillGrade.student_id, GraderStudentSubSkillGrade.student_bolum_id, GraderStudentSubSkillGrade.class_level, GraderStudentSubSkillGrade.grader_role;

RAISE NOTICE 'Completed subskills: %, Average grade: %', completed_subskills, average_grade;

    -- Eğer öğrenci tüm subskills tamamladıysa
    IF completed_subskills = total_subskills THEN
        RAISE NOTICE 'Updating Statistics for student_id: %, skill_id: %', NEW.student_id, skill_id_for_subskill;
        -- Statistics tablosunu güncelle veya yeni kayıt ekle
INSERT INTO Statisticss(class_level, success_rate, student_id, bolum_id, skill_id)
VALUES (NEW.class_level, average_grade, NEW.student_id, NEW.student_bolum_id, skill_id_for_subskill)
    ON CONFLICT (student_id, skill_id, class_level, bolum_id)
        DO UPDATE SET success_rate = average_grade;
ELSE
        RAISE NOTICE 'Not all subskills completed for student_id: %, skill_id: %', NEW.student_id, skill_id_for_subskill;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_student_skill_statistics
    AFTER INSERT OR UPDATE ON GraderStudentSubSkillGrade
                        FOR EACH ROW
                        EXECUTE FUNCTION update_student_skill_statistics();


CREATE OR REPLACE FUNCTION update_action_record_grader()
RETURNS TRIGGER AS $$
DECLARE
action_text VARCHAR;
BEGIN
    IF NEW.grader_role = 0 THEN
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri ogretim elemani gozleminde notu trigger tarafından güncellendi';
    ELSIF NEW.grader_role = 2 THEN
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri ikincil puanlandirici notu trigger tarafından güncellendi';
ELSE
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri notu trigger tarafından güncellendi';
END IF;

INSERT INTO action_records(actiontime, ip_address, userid, bolumid, actiontext)
VALUES (CURRENT_TIMESTAMP, '0.0.0.0', NEW.student_id, NEW.student_bolum_id, action_text);

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER insert_action_record_after_update_graderstudentsubskillgrade
    AFTER UPDATE ON graderstudentsubskillgrade
    FOR EACH ROW
    EXECUTE FUNCTION update_action_record_grader();




CREATE OR REPLACE FUNCTION insert_action_record_grader()
RETURNS TRIGGER AS $$
DECLARE
action_text VARCHAR;
BEGIN
    IF NEW.grader_role = 0 THEN
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri ogretim elemani gozleminde notu trigger tarafından oluşturuldu';
    ELSIF NEW.grader_role = 2 THEN
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri ikincil puanlandirici notu trigger tarafından oluşturuldu';
ELSE
        action_text := '[' || NEW.student_id || ', ' || NEW.student_bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri notu trigger tarafından oluşturuldu';
END IF;

INSERT INTO action_records(actiontime, ip_address, userid, bolumid, actiontext)
VALUES (CURRENT_TIMESTAMP, '0.0.0.0', NEW.student_id, NEW.student_bolum_id, action_text);

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER insert_action_record_after_insert_graderstudentsubskillgrade
    AFTER INSERT ON graderstudentsubskillgrade
    FOR EACH ROW
    EXECUTE FUNCTION insert_action_record_grader();



CREATE OR REPLACE FUNCTION insert_action_record_student()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO action_records(actiontime, ip_address, userid, bolumid, actiontext)
VALUES (CURRENT_TIMESTAMP, '0.0.0.0', NEW.student_id, NEW.bolum_id,'[' || NEW.student_id || ', ' || NEW.bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri kendi başına notu trigger tarafından oluşturuldu');
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_action_record_after_insert_studentsubskillgrade
    AFTER INSERT ON studentsubskillgrade
    FOR EACH ROW
    EXECUTE FUNCTION insert_action_record_student();




CREATE OR REPLACE FUNCTION update_action_record_student()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO action_records(actiontime, ip_address, userid, bolumid, actiontext)
VALUES (CURRENT_TIMESTAMP, '0.0.0.0', NEW.student_id, NEW.bolum_id,'[' || NEW.student_id || ', ' || NEW.bolum_id || '] numarali userId ve bolumId ye sahip öğrencinin [' || NEW.class_level || ']. sinif icin [' || NEW.subskill_id || '] numarali id ye sahip alt beceri kendi başına notu trigger tarafından güncellendi');
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_action_record_after_update_studentsubskillgrade
    AFTER UPDATE ON studentsubskillgrade
    FOR EACH ROW
    EXECUTE FUNCTION update_action_record_student();


insert into user_roles(user_roles_id, userType)values(0, 'TEACHER');
insert into user_roles(user_roles_id, userType)values(1, 'STUDENT');
insert into user_roles(user_roles_id, userType)values(2, 'SECOND_GRADER');
insert into user_roles(user_roles_id, userType)values(3, 'ADMIN');

insert into University(Uni_name, Uni_logo_url)values('Başkent Üniversitesi','asdf');
insert into Faculty(Fac_name, uni_id)values('Sağlık Bilimleri Fakültesi',1);
insert into SecondGrader(second_grader_id,second_grader_name)values(0,'Second Grader Yok');
insert into SecondGrader(second_grader_name)values('Hemşire');
insert into SecondGraderSinif(second_grader_id, sinif_numarasi)values(1,2);
insert into SecondGraderSinif(second_grader_id, sinif_numarasi)values(1,3);
insert into SecondGraderSinif(second_grader_id, sinif_numarasi)values(1,4);
insert into Bolum(bolum_id,bolum_name, fac_id, yil_sayisi, ogrenci_not_verebilirmi, malzemeler_varmi,  second_grader_id )values(1,'Hemşirelik Bölümü',1,4, TRUE, TRUE, 1);

insert into Users (user_id,namee,surname,kayit_tarihi,passwordd,user_roles_id,bolum_id, isActive) values(4,'Admin','','03/23/2001','$2a$10$iebfP0GHPIidh3n88mE3luH/9y3rKs77Y178aTsbJpF.ajvDK2qyW',3,1,TRUE);


CREATE OR REPLACE FUNCTION subskill_with_grades(
    student_id_param INTEGER,
    bolum_id_param INTEGER,
    class_level_param INTEGER,
    skill_id_param INTEGER
)
RETURNS TABLE(
    subskill_id BIGINT,
    name VARCHAR,
    skill_id INTEGER,
    step_no INTEGER,
    student_grade_date timestamp with time zone,
    student_grade DECIMAL(5,2),
    teacher_grade_date timestamp with time zone,
    teacher_grade DECIMAL(5,2),
    nurse_grade_date timestamp with time zone,
    nurse_grade DECIMAL(5,2)
) AS $$
BEGIN
RETURN QUERY
SELECT
    ss.subskill_id,
    ss.name,
    ss.skill_id,
    ss.step_no,
    p.perform_date as student_grade_date,
    p.grade as student_grade,
    g0.grade_date as teacher_grade_date,
    g0.grade as teacher_grade,
    g2.grade_date as nurse_grade_date,
    g2.grade as nurse_grade
FROM
    subskills ss
        LEFT JOIN studentsubskillgrade p ON p.subskill_id = ss.subskill_id
        AND p.student_id = student_id_param
        AND p.bolum_id = bolum_id_param
        AND p.class_level = class_level_param
        LEFT JOIN graderstudentsubskillgrade g0 ON g0.subskill_id = ss.subskill_id
        AND g0.grader_role = 0
        AND g0.student_id = student_id_param
        AND g0.student_bolum_id = bolum_id_param
        AND g0.class_level = class_level_param
        LEFT JOIN graderstudentsubskillgrade g2 ON g2.subskill_id = ss.subskill_id
        AND g2.grader_role = 2
        AND g2.student_id = student_id_param
        AND g2.student_bolum_id = bolum_id_param
        AND g2.class_level = class_level_param
WHERE
    ss.skill_id = skill_id_param
ORDER BY
    ss.step_no ASC;
END;
$$ LANGUAGE plpgsql;



SELECT setval('subskillsteps_subskill_steps_id_seq', (SELECT MAX(subskill_steps_id) FROM subskillsteps));
SELECT setval('skills_skill_id_seq', (SELECT MAX(skill_id) FROM skills));
SELECT setval('subskills_subskill_id_seq', (SELECT MAX(subskill_id) FROM subskills));


CREATE OR REPLACE FUNCTION subskillsteps_with_grades(
    student_id_param bigint,
    bolum_id_param bigint,
    class_level_param bigint,
	subskill_id_param bigint
)
RETURNS TABLE(
    subskill_steps_id BIGINT, -- Veritabanınızda bu sütun INTEGER tipinde
    name VARCHAR,
    step_no INTEGER, -- Veritabanınızda bu sütun INTEGER tipinde
    subskill_id BIGINT, -- Veritabanınızda bu sütun INTEGER tipinde
    ishead BOOLEAN,
    perform_date timestamp with time zone,
    perform_grade DOUBLE PRECISION,
    Grade_Date_Teacher timestamp with time zone,
    Teacher_id BIGINT, -- Teacher_id ve Nurse_id için INTEGER yeterli olacaktır, eğer farklı değilse
    Teacher_Grade DOUBLE PRECISION,
    Grade_Date_Nurse timestamp with time zone,
    Nurse_id BIGINT, -- Nurse_id için de INTEGER kullanılmalıdır
    Nurse_Grade DOUBLE PRECISION
) AS $$
BEGIN
RETURN QUERY
SELECT
    ss.subskill_steps_id,
    ss.name,
    ss.step_no,
    ss.subskill_id,
    ss.ishead,
    p.perform_date,
    p.grade AS perform_grade,
    g0.grade_date AS Grade_Date_Teacher,
    g0.grader_id AS Teacher_id,
    g0.grade AS Teacher_Grade,
    g2.grade_date AS Grade_Date_Nurse,
    g2.grader_id AS Nurse_id,
    g2.grade AS Nurse_Grade
FROM
    subskillsteps ss
        LEFT JOIN performs p ON p.subskill_steps_id = ss.subskill_steps_id
        AND (p.student_id = student_id_param OR p.student_id IS NULL)
        AND (p.bolum_id = bolum_id_param OR p.bolum_id IS NULL)
        AND (p.class_level = class_level_param OR p.class_level IS NULL)
        LEFT JOIN grades g0 ON g0.subskill_steps_id = ss.subskill_steps_id
        AND (g0.grader_role = 0 OR g0.grader_role IS NULL)
        AND (g0.student_id = student_id_param OR g0.student_id IS NULL)
        AND (g0.student_bolum_id = bolum_id_param OR g0.student_bolum_id IS NULL)
        AND (g0.class_level = class_level_param OR g0.class_level IS NULL)
        LEFT JOIN grades g2 ON g2.subskill_steps_id = ss.subskill_steps_id
        AND (g2.grader_role = 2 OR g2.grader_role IS NULL)
        AND (g2.student_id = student_id_param OR g2.student_id IS NULL)
        AND (g2.student_bolum_id = bolum_id_param OR g2.student_bolum_id IS NULL)
        AND (g2.class_level = class_level_param OR g2.class_level IS NULL)
WHERE ss.subskill_id = subskill_id_param
ORDER BY ss.step_no ASC;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION calculate_yearly_skill_averages(skill_id_input bigint)
RETURNS TABLE(academic_year VARCHAR, skill_name VARCHAR, average_grade DECIMAL(5,2)) AS $$
BEGIN
RETURN QUERY
    WITH academic_years AS (
        SELECT
            g.grade_date,
            s.namee AS skill_name,
            g.grade,
            CASE
                WHEN EXTRACT(MONTH FROM g.grade_date) >= 9 THEN EXTRACT(YEAR FROM g.grade_date)::integer
                ELSE EXTRACT(YEAR FROM g.grade_date)::integer - 1
            END AS academic_year_start
        FROM
            GraderStudentSubSkillGrade g
        JOIN
            Subskills ss ON g.subskill_id = ss.subskill_id
        JOIN
            Skills s ON ss.skill_id = s.skill_id
        WHERE
            s.skill_id = skill_id_input
    )
SELECT
    (ay.academic_year_start || '-' || (ay.academic_year_start + 1))::VARCHAR AS academic_year,
        ay.skill_name,
    ROUND(AVG(ay.grade)::numeric, 2) AS average_grade
FROM
    academic_years ay
GROUP BY
    ay.academic_year_start, ay.skill_name
ORDER BY
    ay.academic_year_start;
END;
$$ LANGUAGE plpgsql;

SELECT * FROM subskillsteps_with_grades(1, 1, 1, 1);