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

import com.ha.model.Compra;
import com.ha.model.CompraDetalle;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
public class CompraDetalleRepository {

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    public CompraDetalle findById(Long id) {
        return em.find(CompraDetalle.class, id);
    }


    public List<CompraDetalle> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CompraDetalle> criteria = cb.createQuery(CompraDetalle.class);
        Root<CompraDetalle> compraDetalleRoot = criteria.from(CompraDetalle.class);

        if (mode.contains("asc")){
            criteria.select(compraDetalleRoot).orderBy(cb.asc(compraDetalleRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(compraDetalleRoot).orderBy(cb.desc(compraDetalleRoot.get(attribute)));
        }

        List<CompraDetalle> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<CompraDetalle> findAllOrderedById(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CompraDetalle> criteria = cb.createQuery(CompraDetalle.class);
        Root<CompraDetalle> compraDetalleRoot = criteria.from(CompraDetalle.class);
        criteria.select(compraDetalleRoot).where(cb.isNull(compraDetalleRoot.get("compra"))).orderBy(cb.asc(compraDetalleRoot.get("id")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(CompraDetalle c) throws ValidationException{
        validateCompraDetalle( c );
        this.em.persist(c);
    }

    private void validateCompraDetalle(CompraDetalle detalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<CompraDetalle>> violations = validator.validate(detalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }
}
