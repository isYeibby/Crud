-- =====================================================
-- POSTGRESQL - EJEMPLO SIMPLE DE TIPOS COMPUESTOS
-- =====================================================

-- Limpiar objetos previos
DROP TABLE IF EXISTS empleados CASCADE;
DROP TYPE IF EXISTS direccion CASCADE;
DROP FUNCTION IF EXISTS direccion_completa(direccion);

-- Crear tipo compuesto Dirección
CREATE TYPE direccion AS (
    calle VARCHAR(100),
    numero INTEGER,
    ciudad VARCHAR(50),
    codigo_postal VARCHAR(10)
    );

-- Crear tabla usando el tipo
CREATE TABLE empleados (
                           id_empleado SERIAL PRIMARY KEY,
                           nombre VARCHAR(100),
                           direccion direccion,
                           salario NUMERIC(10,2)
);

-- Insertar datos
INSERT INTO empleados (nombre, direccion, salario) VALUES
                                                       ('Ana Martínez', ROW('Avenida Libertad', 42, 'Barcelona', '08001')::direccion, 3500.00),
                                                       ('Carlos Ruiz', ROW('Gran Vía', 100, 'Madrid', '28013')::direccion, 3200.00),
                                                       ('Laura Sánchez', ROW('Calle Mayor', 25, 'Madrid', '28015')::direccion, 3800.00);

-- Crear función para formatear dirección
CREATE OR REPLACE FUNCTION direccion_completa(dir direccion)
RETURNS TEXT AS $$
BEGIN
RETURN dir.calle || ' ' || dir.numero || ', ' ||
       dir.codigo_postal || ' ' || dir.ciudad;
END;
$$ LANGUAGE plpgsql;

-- Consultas de ejemplo
SELECT
    id_empleado,
    nombre,
    (direccion).calle AS calle,
    (direccion).ciudad AS ciudad,
    direccion_completa(direccion) AS direccion_completa,
    salario
FROM empleados
ORDER BY nombre;