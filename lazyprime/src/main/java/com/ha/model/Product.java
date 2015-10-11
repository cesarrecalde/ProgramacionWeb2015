/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ha.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Entity
@XmlRootElement
@Table(name = "PRODUCT")
public class Product implements Serializable {
    /** 242352345 **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, max = 25)
    private String nameProduct;


    @OneToMany(mappedBy = "product")
    private List<CompraDetalle> compraDetalle;

     private long precioUnitario;

    @Min(0)
     private long cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    private Provider product_provider;

    @OneToMany(mappedBy = "product")
    private List<VentaDetalle> ventaDetalles;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public long getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(long precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }


     public Provider getProduct_provider() {
        return product_provider;
    }

    public void setProduct_provider(Provider product_provider_id) {
        this.product_provider = product_provider_id;
    }

    public Product(String nameProduct,  long precioUnitario, long cantidad, Provider product_provider) {
        this.nameProduct = nameProduct;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.product_provider = product_provider;
    }

    public Product() {
    }
}
