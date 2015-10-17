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

import com.google.gson.Gson;
import com.ha.model.Compra;
import com.ha.model.CompraDetalle;
import com.ha.model.Product;
import com.ha.model.Provider;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CompraRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Compra> compraEvent;

    @Inject
    private Event<CompraDetalle> compraDetalleEvent;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(Compra compra) throws Exception {
//        compra.setFecha(new java.util.Date());
//        em.persist(compra);
//        //Esta parte actualizara los objetos que se encuentran en compra detalle
//        CompraDetalle cp;
//        for (CompraDetalle pedido: compra.getCompraDetalles()){
//            cp = em.find(CompraDetalle.class, pedido.getId());
////            cp.setCompra_detalle(compra);
//            em.persist(cp);
//
//            //Una vez que se guara de manera exitosa la compra se debe reducir el numero de cantidad en el producto comprado
//            reducirCantidadDeProducto(cp.getCantidad(),cp.getProduct());
//
//            cp = new CompraDetalle();
//        }


        for (CompraDetalle detalle : compra.getCompraDetalles()) {
            reducirCantidadDeProducto(detalle.getCantidad(), detalle.getProduct());
        }


        Gson gson = new Gson();
        Provider provider = gson.fromJson(compra.getProvider().getName(), Provider.class);
        compra.setProvider(provider);
        compra.setFecha(new java.util.Date());
        em.persist(compra);

        for (CompraDetalle detalle: compra.getCompraDetalles()){
            CompraDetalle compraDetalle = em.find(CompraDetalle.class, detalle.getId());
            compraDetalle.setCompra(compra);
            em.persist(compraDetalle);
        }


    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void reducirCantidadDeProducto(int cantidad, Product producto){
        Product p = em.find(Product.class, producto.getId());
        p.setCantidad(p.getCantidad() + cantidad);
        em.persist(p);
    }
}
