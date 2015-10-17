//package com.ha.service;
//
///**
// * Created by isaacveron on 9/10/15.
// */
////
////import net.sf.jasperreports.engine.*;
////import net.sf.jasperreports.view.JasperViewer;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class FacturaPDF {
//
//    static Connection conn = null;
//    public void pdf() throws Exception{
//        // Cargamos el driver JDBC
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Postgresql JDBC Driver not found.");
//            System.exit(1);
//        }
//        //Para iniciar el Logger.
//        //inicializaLogger();
//        try {
//            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EjbJaxRSDS", "postgres", "postgres");
//            conn.setAutoCommit(false);
//        } catch (SQLException e) {
//            System.out.println("Error de conexión: " + e.getMessage());
//            System.exit(4);
//        }
//
//        try {
//            Map parameters = new HashMap();
//            parameters.put("nro", "nombre");
//
//            JasperReport report = JasperCompileManager.compileReport(
//                    "/home/cesar/Escritorio/clases/Programacion_web/electiva2web/lazyprime/src/main/resources/Detalles.jrxml");
//            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
//            // Exporta el informe a PDF
//            JasperExportManager.exportReportToPdfFile(print,
//                    "/home/cesar/Escritorio/Informe.pdf");
//            //Para visualizar el pdf directamente desde java
//
//            //JasperViewer.viewReport(print, false);
//        } catch (Exception e) {
//            throw new Exception(e);
//        } finally {
//
//            try {
//                if (conn != null) {
//                    conn.rollback();
//                    System.out.println("ROLLBACK EJECUTADO");
//                    conn.close();
//                }
//            } catch (Exception e) {
//                throw new Exception(e);
//            }
//        }
//    }
//}
