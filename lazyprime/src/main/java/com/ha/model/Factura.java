package com.ha.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity de la Factura
 * Created by german
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "factura.totalRegisters", query = "select count(f.id) from Factura f"),
        @NamedQuery(name = "factura.findById", query = "select f from Factura f where f.id=:id")
})
@Table(name = "factura")
public class Factura implements Serializable{

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Expose
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_factura")
    @SequenceGenerator(name = "seq_factura", sequenceName = "seq_factura")
    private Long id;

    @Basic
    @Column(name = "monto", nullable = true, insertable = true, updatable = true)
    @Expose
    private Long monto;

    @Basic
    @Column(name = "fecha", nullable = false, insertable = true, updatable = true)
    @Expose
    private String fecha;

    public Factura() {
        // Constructor por Defecto
    }

    public String toCSV(){
        String res = "";
        res += this.id + ",";
        res += this.monto + ",";
        res += this.fecha;

        return res;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMonto() {
        return monto;
    }

    public void setMonto(Long monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
