package com.ha.service;

import com.ha.model.Product;
import com.ha.model.Provider;
import com.ha.data.ProviderRepository;
import com.ha.util.*;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
/**
 * Created by cesar on 08/10/15.
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProductMassiveRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    @Resource
    private SessionContext context;

    @Inject
    private Event<Product> productEvent;

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
                try {
                    Product p = parseProduct(l);
                    validateProduct(p);
                    register(p);

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

    private void validateProduct(Product product) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private Product parseProduct(String s) throws Exception,IncorrectFieldException, ProviderNotFoundException{
        String[] line =  s.split(",", -1) ;
        Product p = new Product();

        try {

            p.setNameProduct( line[0] );

            p.setPrecioUnitario( Integer.parseInt( line[1] ) );

            p.setCantidad( Integer.parseInt( line[2] ) );

        } catch (Exception e) {
            throw new IncorrectFieldException("Campos mal especificados");
        }

        return p;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(Product product) throws Exception{

        try {
            em.persist(product);
            productEvent.fire(product);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
}
