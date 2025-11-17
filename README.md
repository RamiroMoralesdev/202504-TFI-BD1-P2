# Trabajo Integrador - BD1 y Programación 2
## Integrantes: Morales, Morales, Montes y Rios

Sistema de gestión de productos con códigos de barras, usuarios y roles, implementado en Java con MySQL.

## 📁 Estructura del Proyecto

```
202504-TFI-BD1-P2/
├── DB/                          # Documentación y scripts de base de datos
│   ├── DER/                     # Diagramas Entidad-Relación
│   ├── docs/                    # Documentación de diseño
│   ├── sql/                     # Scripts SQL
│   │   ├── create_tables.sql   # Creación de tablas
│   │   └── insert_data.sql     # Datos iniciales
│   ├── uml/                     # Diagramas UML
│   └── vitacora/                # Bitácora de cambios
│
└── Trabajo Integrador/          # Proyecto Java
    └── src/trabajo/integrador/
        ├── entities/             # Entidades del dominio
        │   ├── Producto.java
        │   ├── CodigoBarras.java
        │   ├── Usuario.java
        │   └── Rol.java
        │
        ├── dao/                  # Capa de acceso a datos
        │   ├── DAO.java         # Interfaz genérica
        │   ├── MysqlProductoDAO.java
        │   ├── MysqlCodigoBarrasDAO.java
        │   ├── MysqlUsuarioDAO.java
        │   ├── MysqlRolDAO.java
        │   └── tipoCodigoBarras.java  # Enum (EAN13, EAN8, UPC)
        │
        ├── config/              # Configuración
        │   └── DatabaseConfiguration.java
        │
        ├── services/             # Servicios transaccionales
        │   ├── ProductoCodigoBarrasTransaccion.java
        │   ├── UsuarioRolTransaccion.java
        │   └── CodigoBarrasTransaccion.java
        │
        ├── AppMenu.java          # Menú interactivo CRUD
        └── TrabajoIntegrador.java  # Clase principal 
        
```

## 🏗️ Arquitectura

### Patrón DAO (Data Access Object)
El proyecto implementa el patrón DAO para separar la lógica de acceso a datos de la lógica de negocio:

- **Interfaz `DAO<T, K>`**: Define operaciones CRUD genéricas
- **Implementaciones MySQL**: Cada entidad tiene su DAO específico

### Entidades Principales

1. **Producto**: Productos con nombre, marca, categoría, precio y peso
2. **CodigoBarras**: Códigos asociados a productos (tipo EAN13, EAN8, UPC)
3. **Usuario**: Usuarios con autenticación y roles
4. **Rol**: Roles del sistema con permisos

### Servicios Transaccionales

El proyecto implementa servicios transaccionales para garantizar la atomicidad de operaciones complejas:

- **`ProductoCodigoBarrasTransaccion`**: Operaciones atómicas entre Producto y CodigoBarras
  - `crearProductoConCodigoBarras()`: Crea producto y código de barras de forma atómica
  - `actualizarProductoConCodigoBarras()`: Actualiza ambas entidades transaccionalmente
  - `eliminarProductoConCodigoBarras()`: Elimina producto y código de barras asociado

- **`UsuarioRolTransaccion`**: Operaciones transaccionales para Usuario
  - `crearUsuarioTransaccional()`: Crea usuario con garantía de atomicidad
  - `actualizarUsuarioTransaccional()`: Actualiza usuario (incluye verificaciones) de forma atómica
  - `eliminarUsuarioTransaccional()`: Elimina usuario transaccionalmente

- **`CodigoBarrasTransaccion`**: Operaciones transaccionales para CodigoBarras
  - `crearCodigoBarrasTransaccional()`: Crea código de barras con transacción
  - `actualizarCodigoBarrasTransaccional()`: Actualiza código de barras (verificación + actualización atómicas)
  - `eliminarCodigoBarrasTransaccional()`: Elimina código de barras transaccionalmente

**¿Cuándo usar transacciones?**
- Operaciones que involucran múltiples tablas (ej: Producto + CodigoBarras)
- Operaciones con verificaciones previas que deben ser atómicas (ej: verificar UNIQUE antes de actualizar)
- Operaciones críticas donde la integridad de datos es esencial

Ver `TRANSACCIONES.md` para más detalles sobre qué entidades requieren transacciones y por qué.


## 🗄️ Menu interactivo CRUD
Al ejecutar TrabajoIntegrador.java se llama a el menu donde para cada entidad tenemos un CRUD. Implementado con Switch-Case 

## 🗄️ Base de Datos

- **Motor**: MySQL
- **Base de datos**: `db_producto`
- **Tablas**:
  - `rol` - Roles del sistema
  - `usuario` - Usuarios (FK a rol)
  - `producto` - Catálogo de productos
  - `codigo_barras` - Códigos de barras (FK a producto, UNIQUE producto_id)


### Configuración de Database para levantar

Editar `DatabaseConfiguration.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/db_producto";
private static final String USER = "root";
private static final String PASSWORD = "";
```

## 🚀 Ejecución

1. **Crear base de datos**: Ejecutar `DB/sql/create_tables.sql`
2. **Configurar conexión**: Ajustar credenciales en `DatabaseConfiguration.java`
3. **Compilar**: Build del proyecto Java
4. **Ejecutar**: Run `TrabajoIntegrador.java`

## 📝 Uso

Descomentar los ejemplos en `TrabajoIntegrador.java` para probar operaciones CRUD

## 🔧 Tecnologías

- **Java** 
- **MySQL**: Base de datos relacional
- **JDBC**: MySQL Connector/J 8.4.0


## 📋 Validaciones Implementadas

- **Restricciones UNIQUE**: Validación de duplicados en `producto_id`, `username`, `email`
- **Soft Delete**: Filtrado de registros eliminados (`eliminado = FALSE`)
- **Foreign Keys**: Integridad referencial entre tablas
- **Transacciones**: Garantía de atomicidad en operaciones complejas (ACID)

## 🔄 Transacciones

El sistema implementa transacciones para garantizar la integridad de los datos en operaciones complejas. Las transacciones aseguran que todas las operaciones se completen exitosamente o se reviertan completamente (rollback) en caso de error.

### Entidades que requieren transacciones:

1. **Producto + CodigoBarras** ⚠️ **CRÍTICO**
   - Relación UNIQUE: un producto solo puede tener un código de barras
   - Las operaciones de creación/actualización deben ser atómicas

2. **Usuario** ⚠️ **IMPORTANTE**
   - Operaciones con múltiples queries (verificaciones + actualización)
   - Previene condiciones de carrera (race conditions)

3. **CodigoBarras** ⚠️ **IMPORTANTE**
   - Verificaciones previas antes de actualizar
   - Previene violaciones de restricciones UNIQUE

