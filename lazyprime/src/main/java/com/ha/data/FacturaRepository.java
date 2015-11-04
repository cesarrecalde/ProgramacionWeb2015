package com.ha.data;

import com.ha.model.Client;
import com.ha.model.Factura;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by cesar on 04/11/15.
 */

@Stateless
public class FacturaRepository {

    @Inject
    private EntityManager em;

    public Factura findById(Long id) {
        return em.find(Factura.class, id);
    }

    public List<Factura> findAllOrderedBy(int position, String mode, String attribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> criteria = cb.createQuery(Factura.class);
        Root<Factura> facturaRoot = criteria.from(Factura.class);

        if (mode.contains("asc")){
            criteria.select(facturaRoot).orderBy(cb.asc(facturaRoot.get(attribute)));
        }else if(mode.contains("desc")){
            criteria.select(facturaRoot).orderBy(cb.desc(facturaRoot.get(attribute)));
        }

        List<Factura> p = em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
        return p;
    }

    public List<Factura> findAllOrderedByName(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factura> criteria = cb.createQuery(Factura.class);
        Root<Factura> facturaRoot = criteria.from(Factura.class);
        criteria.select(facturaRoot).orderBy(cb.asc(facturaRoot.get("name")));
        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    public List<Factura> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){
        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);

        q.setMaxResults(5);
        q.setFirstResult(page*5);

        return q.getResultList();
    }

    public String getCSVFile(String searchAttribute,String searchKey,String orderAttribute,String order) throws Exception{

        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);
        String filePath = "/home/cesar/Escritorio/FacturaCSV.csv";
        q.setMaxResults(5);

        List<Factura> list = q.getResultList();

        File file = new File(filePath);
        PrintWriter pw = new PrintWriter( file );

        int i = 0;
        while( !list.isEmpty() ){

            for( Factura item : list){
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
            consulta = "SELECT cl.* FROM client cl ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";

            if (searchAttribute.equals("id"))
                consulta = "SELECT f.* FROM factura f WHERE cast(f.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("monto"))
                consulta = "SELECT f.* FROM factura f WHERE cast(f.monto AS TEXT) LIKE " + clave;

            if (searchAttribute.equals("fecha"))
                consulta = "SELECT f.* FROM factura f WHERE to_char(f.fecha,'DD-MM-YY') LIKE " + clave;

            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT f.* FROM factura f WHERE cast(f.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT f.* FROM factura f WHERE cast(f.monto AS TEXT) LIKE " + clave + " UNION " +
                        "SELECT f.* FROM factura f WHERE to_char(f.fecha,'DD-MM-YY') LIKE " + clave;

            }

        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        return this.em.createNativeQuery(consulta,Factura.class);
    }

    public void register(Factura f){
        this.em.persist( f );
    }
}
