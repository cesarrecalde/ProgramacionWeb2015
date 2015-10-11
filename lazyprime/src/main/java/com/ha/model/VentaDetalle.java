package com.ha.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by fede on 11/10/15.
 */
@Entity
@XmlRootElement
@Table(name = "VENTAS_DET")
public class VentaDetalle {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Venta venta;

    @Min(1)
    private int cantidad;

    @ManyToOne
    private Product product;


    public VentaDetalle() {
    }

    public VentaDetalle(Venta venta, Product product) {
        this.venta = venta;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
