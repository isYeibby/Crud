#!/bin/bash

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Ejecutando SQL de Oracle${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Configuración
SQL_DIR="/home/yeibby/Documents/BD_Avanzadas/Crud/sql/oracle"
CONTAINER_NAME="oracle-db"
DB_USER="appuser"
DB_PASS="appuser123"
DB_CONN="//localhost:1521/FREEPDB1"

# Archivos SQL en orden
SQL_FILES=(
    "ejemplo.sql"
    "tipos_basicos.sql"
    "colecciones.sql"
    "tipos_con_herencia.sql"
)

# Copiar archivos SQL al contenedor
echo -e "${BLUE}📦 Copiando archivos SQL al contenedor...${NC}"
docker cp "$SQL_DIR" $CONTAINER_NAME:/tmp/sql_oracle
echo -e "${GREEN}✅ Archivos copiados${NC}\n"

# Ejecutar cada archivo con formato mejorado
for sql_file in "${SQL_FILES[@]}"; do
    echo -e "${YELLOW}📄 Ejecutando: ${sql_file}${NC}"

    docker exec -i $CONTAINER_NAME sqlplus -S $DB_USER/$DB_PASS@$DB_CONN <<EOF > /tmp/oracle_output.txt 2>&1
SET PAGESIZE 0
SET FEEDBACK OFF
SET HEADING OFF
SET ECHO OFF
SET VERIFY OFF
@/tmp/sql_oracle/$sql_file
exit;
EOF

    # Mostrar solo mensajes importantes
    if grep -q "ERROR" /tmp/oracle_output.txt; then
        echo -e "${RED}❌ Error en $sql_file${NC}"
        cat /tmp/oracle_output.txt
    else
        echo -e "${GREEN}✅ $sql_file ejecutado correctamente${NC}"
    fi
    echo ""
done

# Verificar tablas creadas
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Tablas creadas en Oracle:${NC}"
echo -e "${BLUE}========================================${NC}"
docker exec -i $CONTAINER_NAME sqlplus -S $DB_USER/$DB_PASS@$DB_CONN <<EOF
SET PAGESIZE 50
SET LINESIZE 100
COLUMN table_name FORMAT A30
SELECT table_name FROM user_tables ORDER BY table_name;
exit;
EOF

echo -e "\n${BLUE}========================================${NC}"
echo -e "${BLUE}  Tipos de objetos creados:${NC}"
echo -e "${BLUE}========================================${NC}"
docker exec -i $CONTAINER_NAME sqlplus -S $DB_USER/$DB_PASS@$DB_CONN <<EOF
SET PAGESIZE 50
SET LINESIZE 100
COLUMN type_name FORMAT A30
SELECT type_name FROM user_types ORDER BY type_name;
exit;
EOF

# Limpiar archivo temporal
rm -f /tmp/oracle_output.txt

echo -e "\n${GREEN}✅ Oracle configurado correctamente${NC}\n"