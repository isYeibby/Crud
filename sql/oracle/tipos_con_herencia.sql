-- =====================================================
-- ORACLE - HERENCIA DE TIPOS
-- =====================================================

-- Limpiar
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE tabla_estudiantes CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE tipo_estudiante FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE tipo_persona FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Tipo base: Persona
CREATE OR REPLACE TYPE tipo_persona AS OBJECT (
    nombre VARCHAR2(100),
    edad NUMBER,
    MEMBER FUNCTION presentarse RETURN VARCHAR2
) NOT FINAL;
/

CREATE OR REPLACE TYPE BODY tipo_persona AS
    MEMBER FUNCTION presentarse RETURN VARCHAR2 IS
BEGIN
RETURN 'Hola, soy ' || nombre || ' y tengo ' || edad || ' años';
END;
END;
/

-- Tipo derivado: Estudiante
CREATE OR REPLACE TYPE tipo_estudiante UNDER tipo_persona (
    matricula VARCHAR2(20),
    carrera VARCHAR2(100),
    promedio NUMBER(4,2),
    OVERRIDING MEMBER FUNCTION presentarse RETURN VARCHAR2,
    MEMBER FUNCTION tiene_beca RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tipo_estudiante AS
    OVERRIDING MEMBER FUNCTION presentarse RETURN VARCHAR2 IS
BEGIN
RETURN 'Estudiante: ' || nombre ||
       ', Matrícula: ' || matricula ||
       ', Carrera: ' || carrera ||
       ', Promedio: ' || promedio;
END;

    MEMBER FUNCTION tiene_beca RETURN VARCHAR2 IS
BEGIN
        IF promedio >= 8.5 THEN
            RETURN 'SÍ';
ELSE
            RETURN 'NO';
END IF;
END;
END;
/

-- Crear tabla de objetos
CREATE TABLE tabla_estudiantes OF tipo_estudiante;

-- Insertar datos
INSERT INTO tabla_estudiantes VALUES (
                                         tipo_estudiante('Ana García', 20, 'E001', 'Ingeniería Informática', 9.2)
                                     );

INSERT INTO tabla_estudiantes VALUES (
                                         tipo_estudiante('Carlos Ruiz', 22, 'E002', 'Medicina', 8.7)
                                     );

INSERT INTO tabla_estudiantes VALUES (
                                         tipo_estudiante('Laura Sánchez', 21, 'E003', 'Derecho', 7.5)
                                     );

COMMIT;

-- Consultar
SELECT
    e.nombre,
    e.edad,
    e.matricula,
    e.carrera,
    e.promedio,
    e.presentarse() AS presentacion,
    e.tiene_beca() AS beca
FROM tabla_estudiantes e
ORDER BY e.promedio DESC;