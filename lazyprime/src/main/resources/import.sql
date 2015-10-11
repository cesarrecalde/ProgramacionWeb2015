INSERT INTO provider(name) VALUES ('Proveedor 1')
INSERT INTO provider(name) VALUES ('Proveedor 2')
INSERT INTO provider(name) VALUES ('Proveedor 3')
INSERT INTO provider(name) VALUES ('Proveedor 4')
INSERT INTO provider(name) VALUES ('Proveedor 5')

INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (3,'Producto 1',3000,1);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 2', 12000,2);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 3', 12000,2);
INSERT INTO product(cantidad, nameproduct, preciounitario, product_provider_id) VALUES (4,'Producto 4', 12000,2);

INSERT INTO compras_det(cantidad, product_id) VALUES (3, 1)
INSERT INTO compras_det(cantidad, product_id) VALUES (3, 2)
INSERT INTO compras_det(cantidad, product_id) VALUES (3, 3)


INSERT INTO compra(fecha) VALUES ('2015-10-11')

INSERT INTO compras_det(cantidad, compra_detalle_id, product_id) VALUES (3,1,1)