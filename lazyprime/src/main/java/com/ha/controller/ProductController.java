package com.ha.controller;

import com.ha.data.ProductRepository;
import com.ha.model.Product;
import com.ha.service.ProductMassiveRegistration;
import com.ha.util.CSVFileReadingException;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cesar on 22/10/15.
 */
@ViewScoped
@ManagedBean(name="productController")
public class ProductController {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMassiveRegistration productMassiveRegistration;

    @Inject
    private Logger log;

    private List<Product> productList;

    private UploadedFile file;

    private Product newProduct;

    private String errorMessage;

    private Integer page;

    private String order;



    @PostConstruct
    private void init(){
        this.page = 0;
        this.order = "asc";
        this.newProduct = new Product();
        this.productList = productRepository.findAllOrderedByName( page );
        this.file = null;
    }

    public List<Product> getProductList(){
        return this.productList;
    }
    public void setProductList(List<Product> lista){
        this.productList = lista;
    }
    public Product getNewProduct(){
        return this.newProduct;
    }

    public void setNewProduct(Product product){
        this.newProduct = product;
    }

    public void registerProduct(){
        this.productRepository.register( this.newProduct );
        this.newProduct = new Product();
    }

    public void massiveRegistration(FileUploadEvent event) throws CSVFileReadingException,IOException {
        this.errorMessage = "";
        String fileName = this.file.getFileName();

        System.out.println( fileName );
        String filePath = "/home/cesar/Escritorio/" + fileName;

        File archivoGuardado = new File( filePath );

        InputStream in = this.file.getInputstream();
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
            productMassiveRegistration.massiveRegistration( fr );
        } catch (CSVFileReadingException e) {
            this.errorMessage = e.getMessage();
            throw e;
        }
    }
}
