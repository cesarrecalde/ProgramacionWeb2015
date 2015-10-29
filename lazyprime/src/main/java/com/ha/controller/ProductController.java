package com.ha.controller;

import com.ha.data.ProductRepository;
import com.ha.model.LazyProducto;
import com.ha.model.Product;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cesar on 22/10/15.
 */
@ViewScoped
@ManagedBean(name="productController")
public class ProductController implements Serializable {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private Logger log;

    private List<Product> productList;

    private static final String prueba = "asfkdgjadgfsdavahfjscgahfsdgjXGc";

    private Integer page;

    private String order;

    private Product newProduct;

    private LazyDataModel<Product> model;

    @PostConstruct
    private void init(){
        this.page = 0;
        this.order = "asc";
        this.newProduct = new Product();
        model = new LazyProducto(productRepository.findAllOrderedByName(0));
    }

    public LazyDataModel<Product> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Product> model) {
        this.model = model;
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
}
