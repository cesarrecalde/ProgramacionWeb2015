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
import com.ha.util.ProductNotFoundException;
import com.ha.util.ProviderNotFoundException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cesar on 26/10/15.
 */
@ViewScoped
@ManagedBean
public class CompraController {

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

    private List<Compra> compraList;

    private Compra newCompra;

    private Long newProviderId;

    private List<Long> productIds;

    private List<Integer> productNum;

    private Long newId;

    private Integer newNum;

    @PostConstruct
    private void init(){
        this.newCompra = new Compra();
        this.compraList = comprasRepository.findAllOrderedById();
        this.productIds = new ArrayList<Long>();
        this.productNum = new ArrayList<Integer>();

    }

    public void registerCompra() throws ProductNotFoundException,ProviderNotFoundException{

        List<CompraDetalle> detalles = new ArrayList<CompraDetalle>();

        this.productIds.add( this.newId );
        this.productNum.add( this.newNum );

        for( int i = 0 ; i < this.productIds.size() ; i++){
            Long id = this.productIds.get(i);
            Product p;
            try {
                p = productRepository.findById( id );
            }catch (Exception e){
                throw new ProductNotFoundException("Producto con id " + id + " no encontrado");
            }

            int cant = this.productNum.get( i );

            CompraDetalle detalle = new CompraDetalle();

            detalle.setCantidad(cant);
            detalle.setProduct( p );

            this.compraDetalleRepository.register( detalle );
            detalles.add( detalle );
        }

        Provider provider;
        try{
            provider = providerRepository.findById( this.newProviderId );

        }catch (Exception e){
            throw new ProductNotFoundException("Proveedor con id " + this.newProviderId + " no encontrado");
        }

        this.newCompra.setProvider( provider );
        this.newCompra.setCompraDetalles( detalles );

        this.comprasRepository.register( newCompra );
        this.newCompra = new Compra();
    }

    public void massiveRegistration( FileUploadEvent event ) throws CSVFileReadingException,IOException {
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
            compraMassiveRegistration.massiveRegistration( fr );
            fr.close();
        } catch (CSVFileReadingException e) {

            FacesContext.getCurrentInstance().
                    addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Error!",
                                    "\n" + e.getMessage()
                            )
                    );
        }
    }

    public void addProduct(){
        this.productIds.add(this.newId);
        this.productNum.add(this.newNum);
        this.newId = null;
        this.newNum = 0;

    }

    public void removeProduct(){
        this.newId = this.productIds.get( this.productIds.size() - 1 );
        this.newNum = this.productNum.get( this.productNum.size() - 1 );
        this.productIds.remove(this.productIds.size() - 1);
        this.productNum.remove( this.productNum.size() - 1 );
    }

    public Compra getNewCompra() {
        return newCompra;
    }
    public void setNewCompra(Compra newCompra) {
        this.newCompra = newCompra;
    }
    public List<Compra> getCompraList() {
        return compraList;
    }
    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
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
    public List<Long> getProductIds() {
        return productIds;
    }
    public void setProductIds(List<Long> productIds) {
        productIds = productIds;
    }
    public List<Integer> getProductNum() {
        return productNum;
    }
    public void setProductNum(List<Integer> productNum) {
        productNum = productNum;
    }
    public Long getNewId() {
        return newId;
    }
    public void setNewId(Long newId) {
        this.newId = newId;
    }
    public Integer getNewNum() {
        return newNum;
    }
    public void setNewNum(Integer newNum) {
        this.newNum = newNum;
    }

}
