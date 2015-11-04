INSERT INTO provider(name) VALUES ('Proveedor 1');
INSERT INTO provider(name) VALUES ('Proveedor 2');
INSERT INTO provider(name) VALUES ('Proveedor 3');
INSERT INTO provider(name) VALUES ('Proveedor 4');
INSERT INTO provider(name) VALUES ('Proveedor 5');
INSERT INTO provider(name) VALUES ('Proveedor 6');
INSERT INTO provider(name) VALUES ('Proveedor 7');
INSERT INTO provider(name) VALUES ('Proveedor 8');
INSERT INTO provider(name) VALUES ('Proveedor 9');

INSERT INTO client(name) VALUES ('Cliente 1');
INSERT INTO client(name) VALUES ('Cliente 2');
INSERT INTO client(name) VALUES ('Cliente 3');
INSERT INTO client(name) VALUES ('Cliente 4');
INSERT INTO client(name) VALUES ('Cliente 5');
INSERT INTO client(name) VALUES ('Cliente 6');
INSERT INTO client(name) VALUES ('Cliente 7');
INSERT INTO client(name) VALUES ('Cliente 8');
INSERT INTO client(name) VALUES ('Cliente 9');

INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (100,'Producto 1',3000);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (200,'Producto 2', 12000);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (160,'Producto 3', 12000);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (80,'Producto 4', 12000);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (410,'Producto 5', 100);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (400,'Producto 6', 100);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (500,'Producto 7', 100);
INSERT INTO product(cantidad, nameproduct, preciounitario) VALUES (300,'Producto 8', 100);

INSERT INTO venta(fecha, client_id) VALUES (NOW(),2);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),3);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),4);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),5);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),6);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),7);
INSERT INTO venta(fecha, client_id) VALUES (NOW(),9);

INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (10,1,4,'Producto 4');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (12,1,5,'Producto 5');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (20,1,1,'Producto 1');

INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (4,2,2,'Producto 2');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (16,2,7,'Producto 7');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (40,2,5,'Producto 8');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (12,2,1,'Producto 1');
INSERT INTO ventas_det(cantidad, venta_id, product_id,nameproduct) VALUES (43,2,3,'Producto 3');

INSERT INTO compra(fecha, provider_id) VALUES (NOW(),2);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),3);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),4);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),5);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),6);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),7);
INSERT INTO compra(fecha, provider_id) VALUES (NOW(),9);

INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (10,1,4,'Producto 4');
INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (12,1,5,'Producto 5');
INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (20,1,1,'Producto 1');

INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (10,2,2,'Producto 2');
INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (12,2,3,'Producto 3');
INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (20,2,6,'Producto 6');

INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (12,3,8,'Producto 8');
INSERT INTO compra_det(cantidad, compra_id, product_id,nameproduct) VALUES (20,3,7,'Producto 7');
