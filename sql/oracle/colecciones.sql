-- =====================================================
-- ORACLE - COLECCIONES (NESTED TABLES)
-- =====================================================

-- Limpiar
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE personas CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE lista_telefonos FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TYPE tipo_telefono FORCE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Definir tipo Teléfono
CREATE OR REPLACE TYPE tipo_telefono AS OBJECT (
    tipo VARCHAR2(20),
    numero VARCHAR2(15)
);
/

-- Crear colección (Nested Table)
CREATE OR REPLACE TYPE lista_telefonos AS TABLE OF tipo_telefono;
/

-- Crear tabla con colección
CREATE TABLE personas (
                          id NUMBER PRIMARY KEY,
                          nombre VARCHAR2(100),
                          email VARCHAR2(100),
                          telefonos lista_telefonos
) NESTED TABLE telefonos STORE AS tabla_telefonos;

-- Insertar datos con múltiples teléfonos
INSERT INTO personas VALUES (
                                1,
                                'Pedro López',
                                'pedro@example.com',
                                lista_telefonos(
                                        tipo_telefono('Móvil', '612345678'),
                                        tipo_telefono('Trabajo', '915551234'),
                                        tipo_telefono('Casa', '918887766')
                                )
                            );

INSERT INTO personas VALUES (
                                2,
                                'María Fernández',
                                'maria@example.com',
                                lista_telefonos(
                                        tipo_telefono('Móvil', '623456789'),
                                        tipo_telefono('Trabajo', '914442233')
                                )
                            );

INSERT INTO personas VALUES (
                                3,
                                'José Martínez',
                                'jose@example.com',
                                lista_telefonos(
                                        tipo_telefono('Móvil', '634567890')
                                )
                            );

COMMIT;

-- Consultar la colección
SELECT
    p.id,
    p.nombre,
    p.email,
    t.tipo AS tipo_telefono,
    t.numero
FROM personas p, TABLE(p.telefonos) t
ORDER BY p.nombre, t.tipo;

-- Contar teléfonos por persona
SELECT
    p.nombre,
    p.email,
    (SELECT COUNT(*) FROM TABLE(p.telefonos)) AS cantidad_telefonos
FROM personas p;