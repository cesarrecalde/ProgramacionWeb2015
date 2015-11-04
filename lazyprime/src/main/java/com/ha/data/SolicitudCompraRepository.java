package com.ha.data;

import com.ha.model.Client;
import com.ha.model.Product;
import com.ha.model.SolicitudCompra;
import com.ha.model.SolicitudCompra_;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by cesar on 12/10/15.
 */
@Stateless
public class SolicitudCompraRepository {

    @Inject
    private EntityManager em;

    public SolicitudCompra findById(Long id) {
        return em.find(SolicitudCompra.class, id);
    }

    public List<SolicitudCompra> findAllOrderedById(int position) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SolicitudCompra> criteria = cb.createQuery(SolicitudCompra.class);
        Root<SolicitudCompra> solicitudCompraRoot = criteria.from(SolicitudCompra.class);
        criteria.select(solicitudCompraRoot).orderBy(cb.asc(solicitudCompraRoot.get("id")));

        return em.createQuery(criteria).setFirstResult(position).setMaxResults(5).getResultList();
    }

    public boolean existSolicitudForProduct( Product p){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SolicitudCompra> criteria = cb.createQuery(SolicitudCompra.class);
        Root<SolicitudCompra> solicitudCompraRoot = criteria.from(SolicitudCompra.class);
        Predicate condition = cb.equal( solicitudCompraRoot.get(SolicitudCompra_.product), p);

        TypedQuery<SolicitudCompra> tq = em.createQuery(criteria.where(condition));

        List<SolicitudCompra> lista = tq.getResultList();

        if( lista.isEmpty() ){
            return false;
        }else{
            return true;
        }
    }

    public List<SolicitudCompra> findBy(int page,String searchAttribute,String searchKey,String orderAttribute,String order){
        Query q = createQuery(searchAttribute,searchKey,orderAttribute,order);

        q.setMaxResults(5);
        q.setFirstResult(page*5);

        return q.getResultList();
    }

    public Query createQuery(String searchAttribute,String searchKey,String orderAttribute,String order){

        String consulta = "";
        if( searchAttribute.equals("") ){
            consulta = "SELECT s.* FROM solicitudes s ";
        }
        else {
            consulta = "";
            String clave = "\'%" + searchKey + "%\' ";

            if (searchAttribute.equals("id"))
                consulta = "SELECT s.* FROM solicitudes s WHERE cast(s.id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("producto_id"))
                consulta = "SELECT s.* FROM solicitudes s WHERE cast(s.product_id as TEXT) LIKE " + clave;

            if (searchAttribute.equals("producto_nombre"))
                consulta = "SELECT s.* FROM solicitudes s WHERE s.nameProduct LIKE " + clave;

            if (searchAttribute.equals("all")) {
                consulta =
                        "SELECT s.* FROM solicitudes s WHERE cast(s.id as TEXT) LIKE " + clave + " UNION " +
                        "SELECT s.* FROM solicitudes s WHERE cast(s.product_id as TEXT) " + clave + " UNION " +
                        "SELECT s.* FROM solicitudes s WHERE s.nameProduct LIKE " + clave;
            }

        }

        consulta += " ORDER BY " + orderAttribute + " " + order;
        return this.em.createNativeQuery(consulta,SolicitudCompra.class);
    }


}
