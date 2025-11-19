#!/bin/bash

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

clear

echo -e "${YELLOW}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║  CONFIGURACIÓN DE BASES DE DATOS - BD AVANZADAS            ║${NC}"
echo -e "${YELLOW}║  Oracle + PostgreSQL                                       ║${NC}"
echo -e "${YELLOW}║                                                            ║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════════════════════════╝${NC}\n"

# Verificar que Docker esté corriendo
if ! docker ps > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker no está corriendo${NC}"
    exit 1
fi

echo -e "${BLUE}📋 Contenedores detectados:${NC}"
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | grep -E "postgres-db|oracle-db"
echo ""

# Ejecutar PostgreSQL
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}🐘 POSTGRESQL${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"

bash setup-postgres.sh

# Ejecutar Oracle
echo -e "\n${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}🔴 ORACLE${NC}"
echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"

bash setup-oracle.sh

# Resumen final
echo -e "\n${YELLOW}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${YELLOW}║  ✅ CONFIGURACIÓN COMPLETADA                               ║${NC}"
echo -e "${YELLOW}╚════════════════════════════════════════════════════════════╝${NC}\n"

echo -e "${GREEN}Ahora puedes ejecutar las aplicaciones Java:${NC}\n"
echo -e "  ${BLUE}🔴 Oracle:${NC}"
echo -e "     cd crud"
echo -e "     mvn exec:java -Dexec.mainClass=\"com.crud.app.oracle.MenuOracleApp\"\n"
echo -e "  ${BLUE}🐘 PostgreSQL:${NC}"
echo -e "     cd crud"
echo -e "     mvn exec:java -Dexec.mainClass=\"com.crud.app.postgresql.MenuPostgresApp\"\n"