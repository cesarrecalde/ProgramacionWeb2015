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

import com.ha.model.Client;
import com.ha.model.Compra;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class ClientRepository {

    @Inject
    private EntityManager em;

    public Client findById(Long id) {
        return em.find(Client.class, id);
    }

    public List<Client> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> criteria = cb.createQuery(Client.class);
        Root<Client> clientRoot = criteria.from(Client.class);

        if (mode.contains("asc")){
            criteria.select(clientRoot).orderBy(cb.asc(clientRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(clientRoot).orderBy(cb.desc(clientRoot.get(attribute)));
        }

        List<Client> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<Client> findAllOrderedByName(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> criteria = cb.createQuery(Client.class);
        Root<Client> client = criteria.from(Client.class);
        criteria.select(client).orderBy(cb.asc(client.get("name")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    public List<Client> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){
        Query q;
        String consulta = "";
        if( searchAttribute.equals("") ){
            consulta = "SELECT cl.* FROM client cl ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";

            if (searchAttribute.equals("id"))
                consulta = "SELECT cl.* FROM client cl WHERE cast(cl.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("nombre"))
                consulta = "SELECT cl.* FROM client cl WHERE cl.name LIKE " + clave;

            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT cl.* FROM client cl WHERE cl.name LIKE " + clave + " UNION " +
                        "SELECT cl.* FROM client cl WHERE cast(cl.id as TEXT) LIKE " + clave;
            }

        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        q = this.em.createNativeQuery(consulta,Client.class);

        q.setMaxResults(5);

        q.setFirstResult(page*5);

        return q.getResultList();
    }

    public void register(Client c){
        this.em.persist( c );
    }
}
