package com.ha.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by cesar on 12/10/15.
 */
@Entity
@XmlRootElement
@Table(name = "solicitudes")
public class SolicitudCompra implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    public SolicitudCompra( Product product) {
        this.product = product;
    }

    public SolicitudCompra(){

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


}
