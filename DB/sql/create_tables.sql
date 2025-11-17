-- ==========================================
-- Base de datos: db_producto
-- ==========================================
CREATE DATABASE IF NOT EXISTS db_producto;
USE db_producto;

-- ==========================================
-- Tabla: rol
-- ==========================================
CREATE TABLE rol (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion VARCHAR(255)
);

-- ==========================================
-- Tabla: usuario
-- ==========================================
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN DEFAULT FALSE,
    username VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rol_id) REFERENCES rol(id)
);

-- ==========================================
-- Tabla: producto
-- ==========================================
CREATE TABLE producto (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN DEFAULT FALSE,
    nombre VARCHAR(120) NOT NULL,
    marca VARCHAR(80),
    categoria VARCHAR(80),
    precio DECIMAL(10,2) NOT NULL CHECK (precio > 0),
    peso DECIMAL(10,3),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- Tabla: codigo_barras
-- ==========================================
CREATE TABLE codigo_barras (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN DEFAULT FALSE,
    producto_id BIGINT UNIQUE NOT NULL,
    tipo ENUM('EAN13', 'EAN8', 'UPC') NOT NULL,
    valor VARCHAR(20) UNIQUE NOT NULL,
    fecha_asignacion DATE NOT NULL,
    observaciones VARCHAR(255),
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
);

-- ==========================================
-- Índices para mejorar performance
-- ==========================================
CREATE INDEX idx_producto_categoria ON producto(categoria);
CREATE INDEX idx_producto_precio ON producto(precio);
CREATE INDEX idx_codigo_barras_valor ON codigo_barras(valor);
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_rol_id ON usuario(rol_id);