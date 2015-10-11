INSERT INTO provider(name) VALUES ('Proveedor 1')
INSERT INTO provider(name) VALUES ('Proveedor 2')
INSERT INTO provider(name) VALUES ('Proveedor 3')
INSERT INTO provider(name) VALUES ('Proveedor 4')
INSERT INTO provider(name) VALUES ('Proveedor 5')
INSERT INTO provider(name) VALUES ('Proveedor 6')
INSERT INTO provider(name) VALUES ('Proveedor 7')

INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (3,'Producto 1',3000,1);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 2', 12000,2);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 3', 12000,2);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 4', 12000,2);

INSERT INTO compras_det(cantidad, product_id) VALUES (3, 1)
INSERT INTO compras_det(cantidad, product_id) VALUES (3, 2)
INSERT INTO compras_det(cantidad, product_id) VALUES (3, 3)


INSERT INTO compra(fecha) VALUES ('2015-10-11')

INSERT INTO compras_det(cantidad, compra_detalle_id, product_id) VALUES (3,1,1)

INSERT INTO client(name) VALUES ('Cliente 1');
INSERT INTO client(name) VALUES ('Cliente 2');
INSERT INTO client(name) VALUES ('Cliente 3');
INSERT INTO client(name) VALUES ('Cliente 4');
INSERT INTO client(name) VALUES ('Cliente 5');

INSERT INTO ventas_det(cantidad, product_id) VALUES (2,1)
INSERT INTO ventas_det(cantidad, product_id) VALUES (3,2)

INSERT INTO venta(fecha, client_id) VALUES ('2015-10-11',1)


INSERT INTO ventas_det(cantidad, product_id,venta_id) VALUES (3,3,1)