-- ==========================================
-- Archivo: insert_data.sql
-- Descripción: Generación de datos masivos para DB Producto
-- Registros: 10,000+ productos y códigos de barras
-- ==========================================

USE db_producto;

-- ==========================================
-- 1. LIMPIAR TABLAS (OPCIONAL - solo para pruebas)
-- ==========================================
-- DELETE FROM codigo_barras;
-- DELETE FROM producto;
-- ALTER TABLE producto AUTO_INCREMENT = 1;
-- ALTER TABLE codigo_barras AUTO_INCREMENT = 1;

-- ==========================================
-- 2. GENERAR 10,000 PRODUCTOS
-- ==========================================
INSERT INTO producto (nombre, marca, categoria, precio, peso)
SELECT 
    CONCAT('Producto ', numbers.seq),
    ELT(1 + FLOOR(RAND() * 5), 'Samsung', 'LG', 'Sony', 'Philips', 'Panasonic'),
    ELT(1 + FLOOR(RAND() * 6), 'Electrónicos', 'Hogar', 'Ropa', 'Deportes', 'Juguetes', 'Libros'),
    ROUND(10.50 + (RAND() * 989.50), 2),
    ROUND(0.1 + (RAND() * 9.9), 3)
FROM (
    SELECT a.N + b.N * 10 + c.N * 100 + d.N * 1000 AS seq
    FROM 
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a,
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b,
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) c,
    (SELECT 0 AS N UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) d
) numbers
WHERE seq BETWEEN 1 AND 10000;

-- ==========================================
-- 3. GENERAR CÓDIGOS DE BARRAS (RELACIÓN 1:1)
-- ==========================================
INSERT INTO codigo_barras (producto_id, tipo, valor, fecha_asignacion, observaciones)
SELECT 
    p.id,
    ELT(1 + FLOOR(RAND() * 3), 'EAN13', 'EAN8', 'UPC'),
    CASE 
        WHEN FLOOR(RAND() * 3) = 0 THEN CONCAT('1234567', LPAD(p.id, 5, '0'))
        WHEN FLOOR(RAND() * 3) = 1 THEN CONCAT('9876543', LPAD(p.id, 5, '0'))
        ELSE CONCAT('4567891', LPAD(p.id, 5, '0'))
    END,
    DATE_SUB(CURRENT_DATE, INTERVAL FLOOR(RAND() * 365) DAY),
    CASE 
        WHEN RAND() > 0.8 THEN 'Código asignado automáticamente'
        ELSE NULL
    END
FROM producto p
WHERE p.id BETWEEN 1 AND 10000;

-- ==========================================
-- 4. VERIFICACIÓN DE DATOS GENERADOS
-- ==========================================
SELECT 
    'Productos generados:' AS resultado,
    COUNT(*) AS cantidad 
FROM producto
UNION ALL
SELECT 
    'Códigos de barras generados:',
    COUNT(*) 
FROM codigo_barras
UNION ALL
SELECT 
    'Productos sin código de barras:',
    COUNT(*) 
FROM producto p
LEFT JOIN codigo_barras cb ON p.id = cb.producto_id
WHERE cb.id IS NULL;

-- ==========================================
-- 5. CONSULTAS DE PRUEBA PARA MEDICIÓN
-- ==========================================
-- Consulta 1: Productos por categoría (usará índice)
SELECT 'Consulta 1 - Productos por categoría (con índice)' AS prueba;
SELECT categoria, COUNT(*) as total
FROM producto
WHERE categoria = 'Electrónicos'
GROUP BY categoria;

-- Consulta 2: Códigos de barras por tipo
SELECT 'Consulta 2 - Códigos por tipo' AS prueba;
SELECT tipo, COUNT(*) as total
FROM codigo_barras
GROUP BY tipo;

-- Consulta 3: Productos con precio mayor a $500
SELECT 'Consulta 3 - Productos premium' AS prueba;
SELECT COUNT(*) as productos_premium
FROM producto
WHERE precio > 500;

-- ==========================================
-- FIN DEL SCRIPT
-- ==========================================
SELECT 'Datos masproductoidivos generados exitosamente' AS estado;
