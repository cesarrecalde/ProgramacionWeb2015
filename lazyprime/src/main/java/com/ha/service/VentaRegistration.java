/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ha.service;

import com.ha.model.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class VentaRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Venta> ventaEvent;

    @Inject
    private Event<VentaDetalle> ventaDetalleEvent;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(Venta venta) throws Exception {
        venta.setFecha(new java.util.Date());
        em.persist(venta);
        //Esta parte actualizara los objetos que se encuentran en venta detalle
        VentaDetalle cp;
        for (VentaDetalle pedido: venta.getVentaDetalles()){
            cp = em.find(VentaDetalle.class, pedido.getId());
            cp.setVenta(venta);
            em.persist(cp);

            //Una vez que se guara de manera exitosa la compra se debe reducir el numero de cantidad en el producto comprado
            reducirCantidadDeProducto(cp.getCantidad(),cp.getProduct());

            cp = new VentaDetalle();
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void reducirCantidadDeProducto(int cantidad, Product producto) throws Exception{
        Product p = em.find(Product.class, producto.getId());
        p.setCantidad(p.getCantidad() - cantidad);

        if (p.getCantidad() < 0){
            throw new Exception("Cantidad no disponible.");
        }
        em.persist(p);
    }
}