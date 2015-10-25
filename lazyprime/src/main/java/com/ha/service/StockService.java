package com.ha.service;

import com.ha.data.ProductRepository;
import com.ha.data.SolicitudCompraRepository;
import com.ha.model.Product;
import com.ha.model.SolicitudCompra;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cesar on 11/10/15.
 */
@Stateless
public class StockService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private SolicitudCompraRepository solicitudCompraRepository;

    @Schedule(minute = "*/3",hour = "*", persistent = false)
    public void something(){

        System.out.println("************************************");

        List<Product> products = productRepository.findLowStockProducts();
        for( Product p : products){

            System.out.println("Producto " + p.getNameProduct() + " con bajo stock");

            if( !solicitudCompraRepository.existSolicitudForProduct( p ) ){
                SolicitudCompra solicitud = new SolicitudCompra( p );
                em.persist( solicitud );
                System.out.println("Solicitud de compra agregada");
            }else {
                System.out.println("Ya existe solicitud de compra");
            }
        }

    }
}
