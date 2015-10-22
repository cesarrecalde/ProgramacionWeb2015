package com.ha.service;

import com.ha.data.ClientRepository;
import com.ha.model.*;
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
 * Created by cesar on 11/10/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VentaMassiveRegistration {
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
    private ClientRepository clientRepository;

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
                    Venta v = parseVenta(l);
                    registerVenta(v);

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

    private void validateVenta(Venta venta) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Venta>> violations = validator.validate(venta);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private void validateVentaDetalle(VentaDetalle detalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<VentaDetalle>> violations = validator.validate(detalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private Venta parseVenta(String s) throws Exception,ClientNotFoundException,IncorrectFieldException, ProductNotFoundException, ConstraintViolationException{

        String[] line =  s.split(",", 3) ;
        Venta venta = new Venta();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            venta.setFecha( formatter.parse(line[0]) );

        } catch (Exception e) {
            throw new IncorrectFieldException("Fecha mal especificada");
        }

        try{
            Client cliente = clientRepository.findById( Long.parseLong( line[1] ) );
            venta.setClient( cliente );
        }catch ( Exception e){
            throw new ClientNotFoundException("Cliente con id: " + line[1] + " no encontrado");
        }

        ArrayList<VentaDetalle> detallesList = null;
        try {
            detallesList = parseDetalles( line[2] );
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (IncorrectFieldException e) {
            throw e;
        }

        venta.setVentaDetalles(detallesList);

        return venta;
    }

    private ArrayList<VentaDetalle> parseDetalles( String s) throws ProductNotFoundException,IncorrectFieldException{

        ArrayList<VentaDetalle> lista = new ArrayList<VentaDetalle>();

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
            }catch( Exception e){
                throw new ProductNotFoundException("Producto con id: " + productId + "no encontrado");
            }


            VentaDetalle det = new VentaDetalle();
            det.setCantidad( cantidad );
            det.setProduct( product );

            lista.add( det );

        }

        if( lista.isEmpty() ){
            throw  new IncorrectFieldException("La lista de detalles no piede estar vacia");
        }
        else {
            return lista;
        }
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void registerVenta(Venta venta) throws ConstraintViolationException,InsuficientStockException,Exception{

        for( VentaDetalle detalle : venta.getVentaDetalles() ){
            try {
                validateVentaDetalle(detalle);
                em.persist( detalle );

                Product product = detalle.getProduct();
                if( product.getCantidad() - detalle.getCantidad() >= 0 ){
                    product.setCantidad( product.getCantidad() - detalle.getCantidad() );
                    em.merge( product );
                }else{
                    throw new InsuficientStockException("Stock del producto id: " + product.getId() + " no es suficiente");
                }


            }catch ( ConstraintViolationException e ){
                throw e;
            }catch (Exception e){
                throw new Exception(e);
            }

        }
        try {
            validateVenta(venta);
            em.persist(venta);
        }catch ( ConstraintViolationException e){
            throw e;
        }catch (Exception e){
            throw new Exception(e);
        }

    }
}
