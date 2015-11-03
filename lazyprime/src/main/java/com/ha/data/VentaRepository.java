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
package com.ha.data;

import com.ha.model.*;
import com.ha.util.InsuficientStockException;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
@TransactionManagement( TransactionManagementType.CONTAINER )
public class VentaRepository {

    @Inject
    private EntityManager em;

    @Inject
    private VentaDetalleRepository ventaDetalleRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private Validator validator;

    @Resource
    private SessionContext context;

    public Venta findById(Long id) {
        return em.find(Venta.class, id);
    }


    public List<Venta> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Venta> criteria = cb.createQuery(Venta.class);
        Root<Venta> ventaRoot = criteria.from(Venta.class);

        if (mode.contains("asc")){
            criteria.select(ventaRoot).orderBy(cb.asc(ventaRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(ventaRoot).orderBy(cb.desc(ventaRoot.get(attribute)));
        }

        List<Venta> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<Venta> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){
        Query q;
        String consulta = "";
        if( searchAttribute.equals("") ){
            consulta = "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";
            if (searchAttribute.equals("id"))
                consulta = "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and cast(v.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("fecha"))
                consulta = "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and to_char(v.fecha,'DD-MM-YY') LIKE " + clave;

            if (searchAttribute.equals("cliente_nombre") ) {
                consulta = "SELECT v.* FROM venta v,client cl WHERE " +
                        "cl.id=v.client_id and cl.name LIKE " + clave;
            }
            if (searchAttribute.equals("cliente_id")) {
                consulta = "SELECT v.* FROM venta v,client cl WHERE " +
                        "cl.id=v.client_id and cast(cl.id as TEXT) LIKE " + clave;
            }
            if (searchAttribute.equals("producto")) {
                consulta = "SELECT v.* FROM venta v,client cl,ventas_det d WHERE " +
                        "cl.id=v.client_id and d.venta_id=v.id and d.nameproduct LIKE " + clave;
            }
            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and cast(v.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and to_char(v.fecha,'DD-MM-YY') LIKE " + clave + " UNION " +
                        "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and cl.name LIKE " + clave + " UNION " +
                        "SELECT v.* FROM venta v,client cl WHERE cl.id=v.client_id and cast(cl.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT v.* FROM venta v,client cl,ventas_det d WHERE cl.id=v.client_id and d.venta_id=v.id and d.nameproduct LIKE " + clave;

            }

        }


        consulta += " ORDER BY " + orderAttribute + " " + order;
        q = this.em.createNativeQuery(consulta,Venta.class);

        q.setMaxResults(5);

        q.setFirstResult(page*5);

        return q.getResultList();
    }

    public List<Venta> findAllOrderedByDate(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Venta> criteria = cb.createQuery(Venta.class);
        Root<Venta> ventaRoot = criteria.from(Venta.class);
        criteria.select(ventaRoot).orderBy(cb.asc(ventaRoot.get("fecha")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    @TransactionAttribute( TransactionAttributeType.REQUIRES_NEW )
    public void register(Venta v, List<VentaDetalle> detalles) throws ConstraintViolationException,InsuficientStockException,ValidationException{

        try {

            validateVenta(v);
            this.em.persist(v);

            for (VentaDetalle detalle : detalles) {

                Product p = detalle.getProduct();

                if (p.getCantidad() - detalle.getCantidad() >= 0) {
                    p.setCantidad(p.getCantidad() - detalle.getCantidad());
                    this.productRepository.merge(p);
                } else {
                    context.setRollbackOnly();
                    throw new InsuficientStockException("Producto con id " + p.getId() + " con stock insuficiente");
                }

                detalle.setNameProduct(p.getNameProduct());

                validateVentaDetalle(detalle);
                this.em.persist(detalle);
            }

        }catch (ConstraintViolationException e){
            this.context.setRollbackOnly();
            throw e;
        } catch (ValidationException e) {
            this.context.setRollbackOnly();
            throw e;
        }
    }

    private void validateVenta(Venta venta) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Venta>> violations = validator.validate(venta);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private void validateVentaDetalle(VentaDetalle detalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<VentaDetalle>> violations = validator.validate(detalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
