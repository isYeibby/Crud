-- =====================================================
-- ORACLE - EJEMPLO SIMPLE DE TIPOS DE OBJETOS
-- =====================================================

-- Limpiar objetos previos
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE empleados CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE tipo_direccion FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Crear tipo de objeto Dirección
CREATE OR REPLACE TYPE tipo_direccion AS OBJECT (
    calle VARCHAR2(100),
    numero NUMBER,
    ciudad VARCHAR2(50),
    codigo_postal VARCHAR2(10),
    MEMBER FUNCTION direccion_completa RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tipo_direccion AS
    MEMBER FUNCTION direccion_completa RETURN VARCHAR2 IS
BEGIN
RETURN calle || ' ' || numero || ', ' || codigo_postal || ' ' || ciudad;
END;
END;
/

-- Crear tabla con tipo de objeto
CREATE TABLE empleados (
                           id_empleado NUMBER PRIMARY KEY,
                           nombre VARCHAR2(100),
                           direccion tipo_direccion,
                           salario NUMBER(10,2)
);

-- Insertar datos de ejemplo
INSERT INTO empleados VALUES (
                                 1,
                                 'Juan Pérez',
                                 tipo_direccion('Calle Mayor', 25, 'Madrid', '28013'),
                                 3000.00
                             );

INSERT INTO empleados VALUES (
                                 2,
                                 'María García',
                                 tipo_direccion('Gran Vía', 100, 'Barcelona', '08001'),
                                 3500.00
                             );

INSERT INTO empleados VALUES (
                                 3,
                                 'Carlos López',
                                 tipo_direccion('Avenida Principal', 50, 'Valencia', '46001'),
                                 2800.00
                             );

COMMIT;

-- Consultas de ejemplo
SELECT
    id_empleado,
    nombre,
    e.direccion.calle AS calle,
    e.direccion.ciudad AS ciudad,
    e.direccion.direccion_completa() AS direccion_completa,
    salario
FROM empleados e;