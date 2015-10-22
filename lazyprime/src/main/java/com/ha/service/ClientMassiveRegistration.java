package com.ha.service;

import com.ha.model.Client;
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
 * Created by cesar on 07/10/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClientMassiveRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Validator validator;

    @Resource
    private SessionContext context;

    @Inject
    private Event<Client> clientEvent;


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
                    Client c = parseClient(l);
                    validateClient(c);
                    register(c);

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

    private void validateClient(Client client) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    private Client parseClient(String s) throws Exception,IncorrectFieldException{

        String[] line =  s.split(",", -1) ;
        Client c = new Client();

        try {
            c.setName(line[0]);

        } catch (Exception e) {
            throw new IncorrectFieldException("Campos mal especificados");
        }


        return c;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void register(Client client) throws Exception{

        try {
            em.persist(client);
            clientEvent.fire(client);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }
}
