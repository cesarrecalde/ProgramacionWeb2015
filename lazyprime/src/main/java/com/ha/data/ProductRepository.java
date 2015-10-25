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

import com.ha.model.Product;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ProductRepository {

    @Inject
    Logger logger ;

    @Inject
    private EntityManager em;

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }


    public List<Product> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> productRoot = criteria.from(Product.class);

         if (mode.contains("asc")){
            criteria.select(productRoot).orderBy(cb.asc(productRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(productRoot).orderBy(cb.desc(productRoot.get(attribute)));
        }

        List<Product> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<Product> findAllOrderedByName(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> productRoot = criteria.from(Product.class);
        criteria.select(productRoot).orderBy(cb.asc(productRoot.get("nameProduct")));
         return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    public List<Product> findLowStockProducts(){
        return em.createNativeQuery("SELECT * FROM product p WHERE p.cantidad<=10",Product.class).getResultList();
    }

    public void register(Product p){
        this.em.persist( p );
    }
}
