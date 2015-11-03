package com.ha.controller;

import com.ha.data.ComprasRepository;
import com.ha.model.Compra;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by cesar on 29/10/15.
 */
@ViewScoped
@ManagedBean( name = "compraController")
public class CompraController {
    @Inject
    private ComprasRepository repository;

    private List<Compra> list;

    private String searchKey;

    private String searchAttribute;

    private String order;

    private String orderAttribute;

    private Integer page;


    @PostConstruct
    public void init(){
        this.page = 0;
        this.searchAttribute = "";
        this.searchKey = "";
        this.orderAttribute = "id";
        this.order = "ASC";
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);

    }

    public void find(){
        this.page = 0;
        System.out.println("****************");
        System.out.println("Pagina: " +this.page);
        System.out.println("OrdenA: "+this.orderAttribute);
        System.out.println("Orden: "+this.order);
        System.out.println("BusquedaA: "+this.searchAttribute);
        System.out.println("Clave: "+this.searchKey);
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
        System.out.println("Resultados: "+this.list.size());

    }

    public void nextPage(){
        this.page ++;
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
        System.out.println("****************");
        System.out.println("Pagina: " +this.page);
        System.out.println("OrdenA: "+this.orderAttribute);
        System.out.println("Orden: "+this.order);
        System.out.println("BusquedaA: "+this.searchAttribute);
        System.out.println("Clave: "+this.searchKey);
    }

    public void previusPage(){
        this.page --;
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
        System.out.println("****************");
        System.out.println("Pagina: " +this.page);
        System.out.println("OrdenA: "+this.orderAttribute);
        System.out.println("Orden: "+this.order);
        System.out.println("BusquedaA: "+this.searchAttribute);
        System.out.println("Clave: "+this.searchKey);
    }

    public boolean isLast(){
        return this.list.size() < 5;
    }

    public List<Compra> getList() {
        return list;
    }

    public void setList(List<Compra> list) {
        this.list = list;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSearchAttribute() {
        return searchAttribute;
    }

    public void setSearchAttribute(String searchAttribute) {
        this.searchAttribute = searchAttribute;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderAttribute() {
        return orderAttribute;
    }

    public void setOrderAttribute(String orderAttribute) {
        this.orderAttribute = orderAttribute;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
