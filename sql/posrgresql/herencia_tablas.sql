-- =====================================================
-- POSTGRESQL - HERENCIA DE TABLAS
-- =====================================================

-- Limpiar
DROP TABLE IF EXISTS estudiantes CASCADE;
DROP TABLE IF EXISTS personas CASCADE;

-- Tabla padre: Personas
CREATE TABLE personas (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100),
                          edad INTEGER
);

-- Tabla hija: Estudiantes (hereda de personas)
CREATE TABLE estudiantes (
                             matricula VARCHAR(20) UNIQUE,
                             carrera VARCHAR(100),
                             promedio NUMERIC(4,2)
) INHERITS (personas);

-- Insertar en personas
INSERT INTO personas (nombre, edad) VALUES
                                        ('Roberto González', 45),
                                        ('Carmen Díaz', 38);

-- Insertar en estudiantes
INSERT INTO estudiantes (nombre, edad, matricula, carrera, promedio) VALUES
                                                                         ('Ana García', 20, 'E001', 'Ingeniería Informática', 9.2),
                                                                         ('Carlos Ruiz', 22, 'E002', 'Medicina', 8.7),
                                                                         ('Laura Sánchez', 21, 'E003', 'Derecho', 7.5);

-- Consultar solo personas (sin estudiantes)
SELECT * FROM ONLY personas;

-- Consultar todas las personas (incluye estudiantes)
SELECT
    nombre,
    edad,
    tableoid::regclass AS tabla_origen
FROM personas
ORDER BY edad;

-- Consultar solo estudiantes
SELECT
    nombre,
    edad,
    matricula,
    carrera,
    promedio,
    CASE
        WHEN promedio >= 8.5 THEN 'Con Beca'
        ELSE 'Sin Beca'
        END AS estado_beca
FROM estudiantes
ORDER BY promedio DESC;