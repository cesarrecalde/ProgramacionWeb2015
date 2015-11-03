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

import com.ha.model.VentaDetalle;

import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
@TransactionManagement(TransactionManagementType.CONTAINER )
public class VentaDetalleRepository {

    @Inject
    private EntityManager em;

    public VentaDetalle findById(Long id) {
        return em.find(VentaDetalle.class, id);
    }

    private Validator validator;

    public List<VentaDetalle> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VentaDetalle> criteria = cb.createQuery(VentaDetalle.class);
        Root<VentaDetalle> ventaDetalleRoot = criteria.from(VentaDetalle.class);

        if (mode.contains("asc")){
            criteria.select(ventaDetalleRoot).orderBy(cb.asc(ventaDetalleRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(ventaDetalleRoot).orderBy(cb.desc(ventaDetalleRoot.get(attribute)));
        }

        List<VentaDetalle> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<VentaDetalle> findAllOrderedById(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VentaDetalle> criteria = cb.createQuery(VentaDetalle.class);
        Root<VentaDetalle> ventaDetalleRoot = criteria.from(VentaDetalle.class);
        criteria.select(ventaDetalleRoot).where(cb.isNull(ventaDetalleRoot.get("venta"))).orderBy(cb.asc(ventaDetalleRoot.get("id")));
         return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public void register(VentaDetalle vd) throws ValidationException{
        this.validateVentaDetalle( vd );
        this.em.persist( vd );
    }

    private void validateVentaDetalle(VentaDetalle detalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<VentaDetalle>> violations = validator.validate(detalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
}
