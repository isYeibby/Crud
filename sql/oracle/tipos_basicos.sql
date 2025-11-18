-- =====================================================
-- ORACLE - TIPOS BÁSICOS
-- =====================================================

-- Limpiar
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE productos CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE tipo_producto FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Crear tipo Producto
CREATE OR REPLACE TYPE tipo_producto AS OBJECT (
    codigo VARCHAR2(20),
    nombre VARCHAR2(100),
    precio NUMBER(10,2),
    stock NUMBER,
    MEMBER FUNCTION precio_con_iva RETURN NUMBER,
    MEMBER FUNCTION info_producto RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tipo_producto AS
    MEMBER FUNCTION precio_con_iva RETURN NUMBER IS
BEGIN
RETURN ROUND(precio * 1.21, 2); -- IVA 21%
END;

    MEMBER FUNCTION info_producto RETURN VARCHAR2 IS
BEGIN
RETURN codigo || ' - ' || nombre || ' ($' || precio || ')';
END;
END;
/

-- Crear tabla
CREATE TABLE productos (
                           id NUMBER PRIMARY KEY,
                           producto tipo_producto
);

-- Insertar datos
INSERT INTO productos VALUES (
                                 1,
                                 tipo_producto('P001', 'Laptop Dell', 1200.00, 15)
                             );

INSERT INTO productos VALUES (
                                 2,
                                 tipo_producto('P002', 'Mouse Logitech', 25.50, 100)
                             );

INSERT INTO productos VALUES (
                                 3,
                                 tipo_producto('P003', 'Teclado Mecánico', 85.00, 50)
                             );

COMMIT;

-- Consultar
SELECT
    id,
    p.producto.codigo AS codigo,
    p.producto.nombre AS nombre,
    p.producto.precio AS precio_base,
    p.producto.precio_con_iva() AS precio_iva,
    p.producto.stock AS stock,
    p.producto.info_producto() AS informacion
FROM productos p;