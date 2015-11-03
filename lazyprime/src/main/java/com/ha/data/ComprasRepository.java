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
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class ComprasRepository {

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    @Inject
    private CompraDetalleRepository compraDetalleRepository;

    @Inject
    private ProductRepository productRepository;

    @Resource
    private SessionContext context;

    public Compra findById(Long id) {
        return em.find(Compra.class, id);
    }

    public List<Compra> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Compra> criteria = cb.createQuery(Compra.class);
        Root<Compra> compraRoot  = criteria.from(Compra.class);

        if (mode.contains("asc")){
            criteria.select(compraRoot).orderBy(cb.asc(compraRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(compraRoot).orderBy(cb.desc(compraRoot.get(attribute)));
        }

        List<Compra> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<Compra> findAllOrderedByDate(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Compra> criteria = cb.createQuery(Compra.class);
        Root<Compra> compraRoot = criteria.from(Compra.class);
        criteria.select(compraRoot).orderBy(cb.asc(compraRoot.get("fecha")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    public List<Compra> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){
        Query q;
        String consulta = "";
        if( searchAttribute.equals("") ){
            consulta = "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";
            if (searchAttribute.equals("id"))
                consulta = "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and cast(co.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("fecha"))
                consulta = "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and to_char(co.fecha,'DD-MM-YY') LIKE " + clave;

            if (searchAttribute.equals("proveedor_nombre") ) {
                consulta = "SELECT co.* FROM compra co,provider pr WHERE " +
                        "pr.id=co.provider_id and pr.name LIKE " + clave;
            }
            if (searchAttribute.equals("proveedor_id")) {
                consulta = "SELECT co.* FROM compra co,provider pr WHERE " +
                        "pr.id=co.provider_id and cast(pr.id as TEXT) LIKE " + clave;
            }
            if (searchAttribute.equals("producto")) {
                consulta = "SELECT co.* FROM compra co,provider pr,compra_det d WHERE " +
                        "pr.id=co.provider_id and d.compra_id=co.id and d.nameproduct LIKE " + clave;
            }
            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and cast(co.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and to_char(co.fecha,'DD-MM-YY') LIKE " + clave + " UNION " +
                        "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and pr.name LIKE " + clave + " UNION " +
                        "SELECT co.* FROM compra co,provider pr WHERE pr.id=co.provider_id and cast(pr.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT co.* FROM compra co,provider pr,compra_det d WHERE pr.id=co.provider_id and d.compra_id=co.id and d.nameproduct LIKE " + clave;

            }

        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        q = this.em.createNativeQuery(consulta,Compra.class);

        q.setMaxResults(5);

        q.setFirstResult(page*5);

        return q.getResultList();
    }

    @TransactionAttribute( TransactionAttributeType.REQUIRES_NEW )
    public void register(Compra c, List<CompraDetalle> detalles) throws ValidationException{

        try {

            validateCompra( c );
            this.em.persist(c);

            for ( CompraDetalle detalle : detalles ){

                Product p = detalle.getProduct();

                p.setCantidad( p.getCantidad() + detalle.getCantidad() );

                this.productRepository.merge( p );

                detalle.setNameProduct( p.getNameProduct() );
                this.compraDetalleRepository.register( detalle );
            }


        } catch (ValidationException e) {
            this.context.setRollbackOnly();
            throw e;
        }
    }

    private void validateCompra(Compra compra) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Compra>> violations = validator.validate(compra);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
}
