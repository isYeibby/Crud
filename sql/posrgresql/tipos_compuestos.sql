-- =====================================================
-- POSTGRESQL - TIPOS COMPUESTOS
-- =====================================================

-- Limpiar
DROP TABLE IF EXISTS productos CASCADE;
DROP TYPE IF EXISTS info_producto CASCADE;
DROP FUNCTION IF EXISTS precio_con_iva(NUMERIC);

-- Crear tipo compuesto
CREATE TYPE info_producto AS (
    codigo VARCHAR(20),
    nombre VARCHAR(100),
    precio NUMERIC(10,2),
    stock INTEGER
    );

-- Crear tabla
CREATE TABLE productos (
                           id SERIAL PRIMARY KEY,
                           producto info_producto,
                           categoria VARCHAR(50),
                           activo BOOLEAN DEFAULT true
);

-- Función para calcular precio con IVA
CREATE OR REPLACE FUNCTION precio_con_iva(precio_base NUMERIC)
RETURNS NUMERIC AS $$
BEGIN
RETURN ROUND(precio_base * 1.21, 2); -- IVA 21%
END;
$$ LANGUAGE plpgsql;

-- Insertar datos
INSERT INTO productos (producto, categoria) VALUES
                                                (ROW('P001', 'Laptop Dell', 1200.00, 15)::info_producto, 'Computadoras'),
                                                (ROW('P002', 'Mouse Logitech', 25.50, 100)::info_producto, 'Accesorios'),
                                                (ROW('P003', 'Teclado Mecánico', 85.00, 50)::info_producto, 'Accesorios');

-- Consultar
SELECT
    id,
    (producto).codigo AS codigo,
    (producto).nombre AS nombre,
    (producto).precio AS precio_base,
    precio_con_iva((producto).precio) AS precio_iva,
    (producto).stock AS stock,
    categoria
FROM productos
ORDER BY (producto).precio DESC;