package com.ha.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * Created by fede on 11/10/15.
 */
@Entity
@XmlRootElement
@Table(name = "COMPRA")
public class Compra {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(mappedBy = "compra", fetch = FetchType.EAGER)
    private List<CompraDetalle> compraDetalles;

    @ManyToOne
    private Provider provider;

    public Compra() {
    }

    public Compra(Date fecha, List<CompraDetalle> compraDetalles, Provider provider) {
        this.fecha = fecha;
        this.compraDetalles = compraDetalles;
        this.provider = provider;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<CompraDetalle> getCompraDetalles() {
        return compraDetalles;
    }

    public void setCompraDetalles(List<CompraDetalle> compraDetalles) {
        this.compraDetalles = compraDetalles;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
