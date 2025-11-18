-- =====================================================
-- POSTGRESQL - ARRAYS Y JSONB
-- =====================================================

-- Limpiar
DROP TABLE IF EXISTS proyectos CASCADE;
DROP TABLE IF EXISTS productos_tech CASCADE;

-- =====================================================
-- EJEMPLO 1: ARRAYS
-- =====================================================

CREATE TABLE proyectos (
                           id SERIAL PRIMARY KEY,
                           nombre VARCHAR(100),
                           tecnologias TEXT[],
                           miembros_equipo INTEGER[]
);

INSERT INTO proyectos (nombre, tecnologias, miembros_equipo) VALUES
                                                                 ('Sistema Web', ARRAY['Java', 'Spring', 'PostgreSQL', 'React'], ARRAY[1, 3, 5]),
                                                                 ('App Móvil', ARRAY['React Native', 'Node.js', 'MongoDB'], ARRAY[2, 4]),
                                                                 ('API REST', ARRAY['Python', 'FastAPI', 'PostgreSQL'], ARRAY[1, 2, 3]);

-- Consultar arrays
SELECT
    nombre,
    array_to_string(tecnologias, ', ') AS tecnologias_usadas,
    array_length(tecnologias, 1) AS num_tecnologias,
    array_length(miembros_equipo, 1) AS num_miembros
FROM proyectos;

-- Buscar proyectos que usan PostgreSQL
SELECT nombre, tecnologias
FROM proyectos
WHERE 'PostgreSQL' = ANY(tecnologias);

-- =====================================================
-- EJEMPLO 2: JSONB
-- =====================================================

CREATE TABLE productos_tech (
                                id SERIAL PRIMARY KEY,
                                nombre VARCHAR(100),
                                especificaciones JSONB
);

INSERT INTO productos_tech (nombre, especificaciones) VALUES
                                                          ('Laptop Dell XPS', '{
                                                            "marca": "Dell",
                                                            "ram": 16,
                                                            "procesador": "Intel i7",
                                                            "pantalla": {"tamaño": 15.6, "resolucion": "1920x1080"}
                                                          }'::jsonb),
                                                          ('iPhone 15', '{
                                                            "marca": "Apple",
                                                            "ram": 8,
                                                            "almacenamiento": 256,
                                                            "camara": {"principal": 48, "frontal": 12}
                                                          }'::jsonb),
                                                          ('Samsung Galaxy', '{
                                                            "marca": "Samsung",
                                                            "ram": 12,
                                                            "almacenamiento": 128,
                                                            "pantalla": {"tamaño": 6.5, "tipo": "AMOLED"}
                                                          }'::jsonb);

-- Consultar datos JSON
SELECT
    nombre,
    especificaciones->>'marca' AS marca,
    (especificaciones->>'ram')::INTEGER AS ram_gb,
    especificaciones->'pantalla'->>'tamaño' AS tamaño_pantalla
FROM productos_tech;

-- Buscar productos de una marca específica
SELECT nombre, especificaciones
FROM productos_tech
WHERE especificaciones @> '{"marca": "Apple"}';

-- Productos con más de 8GB de RAM
SELECT nombre, (especificaciones->>'ram')::INTEGER AS ram
FROM productos_tech
WHERE (especificaciones->>'ram')::INTEGER > 8
ORDER BY ram DESC;

-- Actualizar JSON
UPDATE productos_tech
SET especificaciones = jsonb_set(
        especificaciones,
        '{ram}',
        '32'
                       )
WHERE nombre = 'Laptop Dell XPS';

-- Verificar actualización
SELECT nombre, especificaciones->>'ram' AS ram_actualizada
FROM productos_tech
WHERE nombre = 'Laptop Dell XPS';