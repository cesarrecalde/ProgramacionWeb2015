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
import com.ha.model.Provider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import org.primefaces.model.SortOrder;
import java.util.Map;


@Stateless
public class ProviderRepository {

    @Inject
    private EntityManager em;

    private List<Provider> providers;

    public Provider findById(Long id) {
        return em.find(Provider.class, id);
    }


    public List<Provider> findAllOrderedByName(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provider> criteria = cb.createQuery(Provider.class);
        Root<Provider> providerRoot = criteria.from(Provider.class);
        criteria.select(providerRoot).orderBy(cb.asc(providerRoot.get("name")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }


    public List<Provider> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provider> criteria = cb.createQuery(Provider.class);
        Root<Provider> providerRoot = criteria.from(Provider.class);

        if (mode.contains("asc")){
            criteria.select(providerRoot).orderBy(cb.asc(providerRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(providerRoot).orderBy(cb.desc(providerRoot.get(attribute)));
        }

        List<Provider> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public Provider findAllByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provider> criteria = cb.createQuery(Provider.class);
        Root<Provider> providerRoot = criteria.from(Provider.class);
        criteria.select(providerRoot).where(em.getCriteriaBuilder().equal(providerRoot.get("name"),name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Provider> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){

        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);

        q.setMaxResults(5);

        q.setFirstResult(page*5);

        return q.getResultList();
    }
    public String getCSVFile(String searchAttribute,String searchKey,String orderAttribute,String order) throws Exception{

        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);
        String filePath = "/home/cesar/Escritorio/ProveedorCSV.csv";
        q.setMaxResults(5);

        List<Provider> list = q.getResultList();

        File file = new File(filePath);
        PrintWriter pw = new PrintWriter( file );

        int i = 0;
        while( !list.isEmpty() ){

            for( Provider item : list){
                pw.println( item.toCSV() );
            }

            i++;
            q.setFirstResult(i*5);
            list = q.getResultList();
        }

        pw.close();
        return filePath;
    }
    public Query createQuery(String searchAttribute,String searchKey,String orderAttribute,String order){
        String consulta = "";
        if( searchAttribute.equals("") ){
            consulta = "SELECT pr.* FROM provider pr ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";

            if (searchAttribute.equals("id"))
                consulta = "SELECT pr.* FROM provider pr WHERE cast(pr.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("nombre"))
                consulta = "SELECT pr.* FROM provider pr WHERE pr.name LIKE " + clave;

            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT pr.* FROM provider pr WHERE pr.name LIKE " + clave + " UNION " +
                                "SELECT pr.* FROM provider pr WHERE cast(pr.id as TEXT) LIKE " + clave;
            }

        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        return this.em.createNativeQuery(consulta,Provider.class);
    }

    public void register(Provider p){

        this.em.persist( p );
    }

}
