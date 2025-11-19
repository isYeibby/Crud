#!/bin/bash

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Ejecutando SQL de PostgreSQL${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Configuración
SQL_DIR="/home/yeibby/Documents/BD_Avanzadas/Crud/sql/postgresql"
CONTAINER_NAME="postgres-db"
DB_USER="admin"
DB_NAME="testdb"

# Archivos SQL en orden
SQL_FILES=(
    "ejemplo.sql"
    "tipos_compuestos.sql"
    "arrays_json.sql"
    "herencia_tablas.sql"
)

# Ejecutar cada archivo
for sql_file in "${SQL_FILES[@]}"; do
    echo -e "${YELLOW}📄 Ejecutando: ${sql_file}${NC}"

    docker exec -i $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME < "$SQL_DIR/$sql_file" 2>&1

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ $sql_file ejecutado correctamente${NC}\n"
    else
        echo -e "${RED}❌ Error en $sql_file${NC}\n"
    fi
done

# Verificar tablas creadas
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Tablas creadas en PostgreSQL:${NC}"
echo -e "${BLUE}========================================${NC}"
docker exec -it $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME -c "\dt"

echo -e "\n${GREEN}✅ PostgreSQL configurado correctamente${NC}\n"