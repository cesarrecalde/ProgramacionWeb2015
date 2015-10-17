//package com.ha.rest;
//
////import com.ha.service.FacturaPDF;
//import org.jboss.resteasy.specimpl.HttpHeadersImpl;
//import org.jboss.resteasy.util.HttpHeaderNames;
//import javax.inject.Inject;
//import javax.ws.rs.GET;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Response;
//import java.io.FileInputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.logging.Logger;
//
///**
// * Created by cesar on 13/10/15.
// */
//@javax.ws.rs.Path("/reporte")
//public class ReporteResourceREST {
//
//    @Inject
//    private FacturaPDF factura;
//
//    @Inject
//    private Logger log;
//
//    @GET
//    @Produces("application/pdf")
//    public Response generarInforme(){
//        try{
//
//            factura.pdf();
//            FileInputStream fileInputStream = new FileInputStream("/home/cesar/Escritorio/Informe.pdf");
//
//            Response.ResponseBuilder responseBuilder = Response.ok((Object) fileInputStream);
//            responseBuilder.type("application/pdf");
//            responseBuilder.header("Content-Disposition", "filename=Informe.pdf");
//            return responseBuilder.build();
//
//        }catch (Exception e){
//            return Response.serverError().build();
//        }
//    }
//}
