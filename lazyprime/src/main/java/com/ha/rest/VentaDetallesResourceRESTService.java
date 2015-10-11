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
package com.ha.rest;

import com.ha.data.CompraDetalleRepository;
import com.ha.data.VentaDetalleRepository;
import com.ha.model.CompraDetalle;
import com.ha.model.VentaDetalle;
import com.ha.service.CompraDetalleRegistration;
import com.ha.service.CompraDetalleRemove;
import com.ha.service.VentaDetalleRegistration;
import com.ha.service.VentaDetalleRemove;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Logger;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/ventasDetalles")
@RequestScoped
public class VentaDetallesResourceRESTService {
    @Inject
    private Logger log;

    @Inject
    private Validator validator;


    @Inject
    private VentaDetalleRepository repository;

    @Inject
    private VentaDetalleRemove ventaDetalleRemove;

    @Inject
    VentaDetalleRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VentaDetalle> listAllVentaDetalles() {
        return repository.findAllOrderedById();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public VentaDetalle lookupVentaDetalleById(@PathParam("id") long id) {
        VentaDetalle ventaDetalle = repository.findById(id);
        if (ventaDetalle == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return ventaDetalle;
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVentaDetalle(VentaDetalle ventaDetalle) {
        Response.ResponseBuilder builder = null;
        try {
            // Validates member using bean validation

            validateVentaDetalle(ventaDetalle);
            registration.register(ventaDetalle);
            // Create an "ok" response
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeVentaDetalle(@PathParam("id")int id) {
        Response.ResponseBuilder builder = null;

        try {
            ventaDetalleRemove.remove((long)id);
            builder = Response.ok();
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    private void validateVentaDetalle(VentaDetalle ventaDetalle) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<VentaDetalle>> violations = validator.validate(ventaDetalle);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can then be used
     * by clients to show violations.
     *
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }


}
