package com.ha.data;

import com.ha.model.Product;
import com.ha.model.SolicitudCompra;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
/*
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
*/
        return true;
    }
}
