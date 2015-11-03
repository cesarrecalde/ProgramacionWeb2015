package com.ha.controller;

import com.ha.data.CompraDetalleRepository;
import com.ha.data.ComprasRepository;
import com.ha.data.ProductRepository;
import com.ha.data.ProviderRepository;
import com.ha.model.Compra;
import com.ha.model.CompraDetalle;
import com.ha.model.Product;
import com.ha.model.Provider;
import com.ha.service.CompraMassiveRegistration;
import com.ha.util.CSVFileReadingException;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ValidationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cesar on 26/10/15.
 */
@ViewScoped
@ManagedBean(name = "compraRC")
public class CompraRegistrationController {

    @Inject
    private ComprasRepository comprasRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private CompraDetalleRepository compraDetalleRepository;

    @Inject
    private ProviderRepository providerRepository;

    @Inject
    private CompraMassiveRegistration compraMassiveRegistration;

    private Compra newCompra;

    private Long newProviderId;

    private List<String> productIds;

    private List<String> productNum;


    @PostConstruct
    private void init(){
        this.newCompra = new Compra();

        this.productIds = new ArrayList<String>();
        this.productNum = new ArrayList<String>();
        this.productIds.add("0");
        this.productNum.add("0");

    }

    public void registerCompra(){

        Provider provider = new Provider();

        while ( true ){
            try{
                provider = providerRepository.findById( this.newProviderId );
                provider.getId();
            }catch (Exception e){
                this.showError( "Proveedor con id " + this.newProviderId + " no encontrado" );
                break;
            }

            this.newCompra.setProvider( provider );

            List<CompraDetalle> detalles = new ArrayList<CompraDetalle>();

            for( int i = 0 ; i < this.productIds.size() ; i++){

                Long id;
                try {
                    id = Long.parseLong(this.productIds.get(i) );
                } catch (NumberFormatException e) {
                    this.showError("No se admiten letras en ningun campo");
                    break;
                }

                Product p = new Product();

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

                CompraDetalle detalle = new CompraDetalle();

                detalle.setCantidad(cant);
                detalle.setProduct( p );
                detalle.setCompra( this.newCompra );

                detalles.add( detalle );

            }

            try {
                this.comprasRepository.register( this.newCompra, detalles );
                this.newCompra = new Compra();
                this.newProviderId = Long.parseLong("0");
                this.productIds = new ArrayList<String>();
                this.productNum = new ArrayList<String>();
                this.productIds.add("0");
                this.productNum.add("0");
                break;
            } catch (ValidationException e) {
                this.showError( e.getMessage() );
                break;
            }
        }

    }

    public void massiveRegistration( FileUploadEvent event ) {
        UploadedFile file = event.getFile();
        String fileName = file.getFileName();

        String filePath = "/home/cesar/Escritorio/" + fileName;

        try {
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


            compraMassiveRegistration.massiveRegistration( fr );
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CSVFileReadingException e) {
            this.showError(e.getMessage());
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

    public Compra getNewCompra() {
        return newCompra;
    }
    public void setNewCompra(Compra newCompra) {
        this.newCompra = newCompra;
    }
    public ComprasRepository getComprasRepository() {
        return comprasRepository;
    }
    public void setComprasRepository(ComprasRepository comprasRepository) {
        this.comprasRepository = comprasRepository;
    }
    public CompraMassiveRegistration getCompraMassiveRegistration() {
        return compraMassiveRegistration;
    }
    public void setCompraMassiveRegistration(CompraMassiveRegistration compraMassiveRegistration) {
        this.compraMassiveRegistration = compraMassiveRegistration;
    }
    public Long getNewProviderId() {
        return newProviderId;
    }
    public void setNewProviderId(Long newProviderId) {
        this.newProviderId = newProviderId;
    }
    public List<String> getProductIds() {
        return productIds;
    }
    public void setProductIds(List<Long> productIds) {
        productIds = productIds;
    }
    public List<String> getProductNum() {
        return productNum;
    }
    public void setProductNum(List<Integer> productNum) {
        productNum = productNum;
    }


}
