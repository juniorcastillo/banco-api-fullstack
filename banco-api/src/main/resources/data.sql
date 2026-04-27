INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, password, estado) VALUES
('Jose Lema', 'Masculino', 30, '1234567890', 'Otavalo sn y principal', '098254785', '1234', true),
('Marianela Montalvo', 'Femenino', 28, '1234567891', 'Amazonas y NNUU', '097548965', '5678', true),
('Juan Osorio', 'Masculino', 32, '1234567892', '13 junio y Equinoccial', '098874587', '1245', true);

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id) VALUES
('478758', 'Ahorro', 2000, 1425, true, 1),
('225487', 'Corriente', 100, 700, true, 2),
('495878', 'Ahorros', 0, 150, true, 3),
('496825', 'Ahorros', 540, 0, true, 2),
('585545', 'Corriente', 1000, 1000, true, 1);

INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id) VALUES
(CURRENT_DATE, 'DEBITO', -575, 1425, 1),
(CURRENT_DATE, 'CREDITO', 600, 700, 2),
(CURRENT_DATE, 'CREDITO', 150, 150, 3),
(CURRENT_DATE, 'DEBITO', -540, 0, 4);