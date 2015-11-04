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
import com.ha.model.Product;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

    public List<Product> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){

        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);

        q.setMaxResults(5);

        q.setFirstResult(page*5);

        return q.getResultList();
    }
    public String getCSVFile(String searchAttribute,String searchKey,String orderAttribute,String order) throws Exception{

        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);
        String filePath = "/home/cesar/Escritorio/ProductoCSV.csv";
        q.setMaxResults(5);

        List<Product> list = q.getResultList();

        File file = new File(filePath);
        PrintWriter pw = new PrintWriter( file );

        int i = 0;
        while( !list.isEmpty() ){

            for( Product item : list){
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
            consulta = "SELECT pr.* FROM product pr ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";

            if (searchAttribute.equals("id"))
                consulta = "SELECT pr.* FROM product pr WHERE cast(pr.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("nombre"))
                consulta = "SELECT pr.* FROM product pr WHERE pr.nameproduct LIKE " + clave;

            if( searchAttribute.equals("cantidad") )
                consulta = "SELECT pr.* FROM product pr WHERE cast(pr.cantidad as TEXT) LIKE " + clave;

            if( searchAttribute.equals("precio") )
                consulta = "SELECT pr.* FROM product pr WHERE cast(pr.preciounitario as TEXT) LIKE " + clave;

            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT pr.* FROM product pr WHERE cast(pr.id as TEXT) LIKE " + clave + " UNION " +
                                "SELECT pr.* FROM product pr WHERE pr.nameproduct LIKE " + clave + " UNION " +
                                "SELECT pr.* FROM product pr WHERE cast(pr.cantidad as TEXT) LIKE " + clave + " UNION " +
                                "SELECT pr.* FROM product pr WHERE cast(pr.preciounitario as TEXT) LIKE " + clave;
            }


        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        return this.em.createNativeQuery(consulta,Product.class);
    }
    public void register(Product p){
        this.em.persist( p );
    }

    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public void merge(Product p){ this.em.merge( p );}
}
