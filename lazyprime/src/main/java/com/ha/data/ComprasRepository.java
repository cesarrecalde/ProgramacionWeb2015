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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class ComprasRepository {

    @Inject
    private EntityManager em;

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
}
