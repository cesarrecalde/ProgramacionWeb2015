package com.ha.controller;

import com.ha.data.*;
import com.ha.model.*;
import com.ha.service.VentaMassiveRegistration;
import com.ha.util.CSVFileReadingException;
import com.ha.util.InsuficientStockException;
import com.ha.util.ProductNotFoundException;
import com.ha.util.ProviderNotFoundException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cesar on 30/10/15.
 */
@ViewScoped
@ManagedBean(name = "ventaRC")
public class VentaRegistrationController {
    @Inject
    private VentaRepository ventaRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private VentaDetalleRepository ventaDetalleRepository;

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private VentaMassiveRegistration ventaMassiveRegistration;

    private Venta newVenta;

    private Long newClientId;

    private List<String> productIds;

    private List<String> productNum;


    @PostConstruct
    private void init(){
        this.newVenta = new Venta();

        this.productIds = new ArrayList<String>();
        this.productNum = new ArrayList<String>();
        this.productIds.add("0");
        this.productNum.add("0");

    }

    public void registerVenta() {

        Client client = new Client();
        while ( true ){
            try{
                client = clientRepository.findById( this.newClientId );
                client.getId();
            }catch (Exception e){
                this.showError( "Cliente con id " + this.newClientId + " no encontrado" );
                break;
            }

            this.newVenta.setClient(client);

            List<VentaDetalle> detalles = new ArrayList<VentaDetalle>();

            for( int i = 0 ; i < this.productIds.size() ; i++){

                Long id;

                try {
                    id = Long.parseLong(this.productIds.get(i) );
                } catch (NumberFormatException e) {
                    this.showError("No se admiten letras en ningun campo");
                    break;
                }

                Product p;

                try {
                    p = productRepository.findById( id );
                    p.getId();
                }catch (Exception e){
                    this.showError( "Producto con id " + id + " no encontrado" );
                    break;
                }

                int cant = 0;

                try {
                    cant = Integer.parseInt(this.productNum.get( i ) );
                    if( cant <= 0){
                        this.showError("No se permiten cantidades menores o iguales a cero");
                        break;
                    }
                } catch (NumberFormatException e) {
                    this.showError("No se admiten letras en ningun campo");
                    break;
                }

                VentaDetalle detalle = new VentaDetalle();

                detalle.setCantidad(cant);
                detalle.setProduct( p );
                detalle.setVenta(this.newVenta);

                detalles.add( detalle );

            }

            try {
                this.ventaRepository.register(this.newVenta, detalles);
                this.newVenta = new Venta();
                this.newClientId = Long.parseLong("0");
                this.productIds = new ArrayList<String>();
                this.productNum = new ArrayList<String>();
                this.productIds.add("0");
                this.productNum.add("0");
                break;
            }catch (ConstraintViolationException e) {
                this.showError( e.getMessage() );
                break;
            }catch (ValidationException e) {
                this.showError( e.getMessage() );
                break;
            }catch( InsuficientStockException e){
                this.showError( e.getMessage() );
                break;
            }
        }

    }

    public void massiveRegistration( FileUploadEvent event ) throws IOException {
        UploadedFile file = event.getFile();
        String fileName = file.getFileName();

        String filePath = "/home/cesar/Escritorio/" + fileName;

        File archivoGuardado = new File( filePath );

        InputStream in = file.getInputstream();
        OutputStream out = new FileOutputStream( archivoGuardado );

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        in.close();
        out.close();

        FileReader fr = new FileReader( archivoGuardado );

        try {
            ventaMassiveRegistration.massiveRegistration( fr );
            fr.close();
        } catch (CSVFileReadingException e) {
            this.showError( e.getMessage() );
        }

    }

    public void showError(String message){
        FacesContext.getCurrentInstance().
                addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error!",
                                "\n" + message
                        )
                );
    }

    public void addProduct(){
        this.productIds.add("0");
        this.productNum.add("0");
    }

    public void removeProduct(){
        this.productIds.remove( this.productIds.size() - 1 );
        this.productNum.remove( this.productNum.size() - 1 );
    }


    public Long getNewClientId() {
        return newClientId;
    }

    public void setNewClientId(Long newClientId) {
        this.newClientId = newClientId;
    }

    public Venta getNewVenta() {
        return newVenta;
    }

    public void setNewVenta(Venta newVenta) {
        this.newVenta = newVenta;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductNum() {
        return productNum;
    }

    public void setProductNum(List<String> productNum) {
        this.productNum = productNum;
    }
}
