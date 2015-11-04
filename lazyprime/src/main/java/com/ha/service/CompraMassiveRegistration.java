package com.ha.service;

import com.ha.data.ProviderRepository;
import com.ha.model.Compra;
import com.ha.model.CompraDetalle;
import com.ha.model.Product;
import com.ha.model.Provider;
import com.ha.util.*;
import com.ha.data.ProductRepository;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

/**
 * Created by cesar on 07/10/15.
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CompraMassiveRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    @Resource
    private SessionContext context;

    @Inject
    private Event<Compra> clientEvent;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProviderRepository providerRepository;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void massiveRegistration( FileReader fr) throws CSVFileReadingException, IOException{

        boolean rollback = false;
        int i = 1;
        BufferedReader br = new BufferedReader(fr);
        HashMap<String,String> exceptions = new HashMap<String,String>();
        exceptions.clear();
        String l;

        try {
            while ( ( l = br.readLine() ) != null ){
                System.out.println(l);
                try {
                    Compra c = parseCompra(l);
                    registerCompra(c);

                } catch (IncorrectFieldException e) {
                    exceptions.put( "line" + i,  e.getMessage() );
                    rollback = true;

                } catch (ProviderNotFoundException e) {
                    exceptions.put( "line" + i,  e.getMessage() );
                    rollback = true;
                } catch (ConstraintViolationException e) {
                    exceptions.put( "line" + i,  e.getConstraintViolations().toString() );
                    rollback = true;
                } catch (Exception e) {
                    exceptions.put( "line" + i,  e.getMessage() );
                    rollback = true;
                }
                i++;
            }
        } catch (IOException e) {
            throw new IOException();
        }

        if( rollback ){
            context.setRollbackOnly();
            throw new CSVFileReadingException( exceptions.toString() );
        }

    }

    private void validateCompra(Compra compra) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Compra>> violations = validator.validate(compra);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private void validateCompraDetalle(CompraDetalle detalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<CompraDetalle>> violations = validator.validate(detalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private Compra parseCompra(String s) throws Exception,IncorrectFieldException, ProductNotFoundException, ConstraintViolationException{

        String[] line =  s.split(",", 3) ;
        Compra compra = new Compra();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            compra.setFecha( formatter.parse(line[0]) );

        } catch (Exception e) {
            throw new IncorrectFieldException("Fecha mal especificada");
        }

        try {

            Provider provider = providerRepository.findById( Long.parseLong( line[1]) );
            compra.setProvider( provider );
        } catch (Exception e) {
            throw new ProviderNotFoundException("Proveedor no encontrado");
        }

        ArrayList<CompraDetalle> detallesList = null;

        detallesList = parseDetalles( line[2] );


        compra.setCompraDetalles( detallesList );

        return compra;
    }

    private ArrayList<CompraDetalle> parseDetalles( String s) throws ProductNotFoundException,IncorrectFieldException{

        ArrayList<CompraDetalle> lista = new ArrayList<CompraDetalle>();

        String[] line = s.split(",",-1);

        for( String detalle : line){
            String[] prop = detalle.split("-",-1);

            Long productId = null;
            int cantidad;
            Product product;
            try {
                productId = Long.parseLong( prop[0] );
                cantidad = Integer.parseInt( prop[1] );
            }catch ( Exception e){
                throw new IncorrectFieldException("Detalle de compra mal especificado");
            }

            try {
                product = productRepository.findById(productId);
                product.getId();
            }catch( Exception e){
                throw new ProductNotFoundException("Producto con id: " + productId + "no encontrado");
            }

            CompraDetalle det = new CompraDetalle();
            det.setCantidad( cantidad );
            det.setProduct( product );

            lista.add( det );

        }

        if( lista.isEmpty() ){
            throw  new IncorrectFieldException("La lista de detalles no puede estar vacia");
        }
        else {
            return lista;
        }
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerCompra(Compra compra) throws ConstraintViolationException,Exception{

        validateCompra( compra );
        em.persist( compra );

        for( CompraDetalle detalle : compra.getCompraDetalles() ){

            Product product = detalle.getProduct();
            product.setCantidad( product.getCantidad() + detalle.getCantidad() );
            em.merge( product );

            detalle.setCompra( compra );
            detalle.setNameProduct( product.getNameProduct() );
            validateCompraDetalle(detalle);
            em.persist( detalle );

        }


    }

}
