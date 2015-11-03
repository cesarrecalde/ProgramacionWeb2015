package com.ha.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by fede on 11/10/15.
 */
@Entity
@XmlRootElement
@Table(name = "COMPRA_DET")
public class CompraDetalle {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Compra compra;

    @Min(1)
    private int cantidad;

    @ManyToOne
    private Product product;

    private String nameProduct;

    public CompraDetalle() {
    }

    public CompraDetalle(int cantidad, Product product) {
        this.cantidad = cantidad;
        this.product = product;
        this.nameProduct = product.getNameProduct();
    }

    public CompraDetalle(Compra compra, int cantidad, Product product) {
        this.compra = compra;
        this.cantidad = cantidad;
        this.product = product;
        this.nameProduct = product.getNameProduct();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
}
