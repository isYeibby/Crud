## **Conexión interna a las bases**

#### PostgreSQL

```
docker exec -it postgres-db psql -U admin -d testdb
```
#### Oracle

```
docker exec -it oracle-db sqlplus appuser/appuser123@//localhost:1521/FREEPDB1
```




## **Conexión externa a las bases**

### **PostgreSQL**

**Datos de conexión:**
- **Host:** `localhost`
- **Puerto:** `5432`
- **Base de datos:** `testdb`
- **Usuario:** `admin`
- **Contraseña:** `admin123`

**Cadena JDBC:**
```
jdbc:postgresql://localhost:5432/testdb
```

### **Oracle Database (container gvenzl/oracle-free)**

**Datos de conexión:**
- **Host:** `localhost`
- **Puerto:** `1521`
- **Service Name:** `FREEPDB1`
- **Usuario:** `appuser`
- **Contraseña:** `appuser123`
