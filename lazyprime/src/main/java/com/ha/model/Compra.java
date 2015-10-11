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
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@XmlRootElement
@Table(name = "COMPRA")
public class Compra implements Serializable {
    /** 242352345 **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(mappedBy = "compra_detalle")
    private List<CompraDetalle> compraDetalles;


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

    public Compra(Date fecha, List<CompraDetalle> compraDetalles) {
        this.fecha = fecha;
        this.compraDetalles = compraDetalles;
    }

    public Compra() {
    }
}
