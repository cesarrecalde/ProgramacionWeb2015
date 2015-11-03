package com.ha.controller;


import com.ha.data.ClientRepository;
import com.ha.model.Client;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by isaacveron on 25/10/15.
 *
 */


@ViewScoped
@ManagedBean(name="clienteController")
public class ClienteController {

    @Inject
    private ClientRepository repository;

    private List<Client> list;

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
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
    }

    public void nextPage(){
        this.page ++;
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
    }

    public void previusPage(){
        this.page --;
        this.list = this.repository.findBy(page,searchAttribute,searchKey,orderAttribute,order);
    }

    public boolean isLast(){
        return this.list.size() < 5;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrderAttribute() {
        return orderAttribute;
    }

    public void setOrderAttribute(String orderAttribute) {
        this.orderAttribute = orderAttribute;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSearchAttribute() {
        return searchAttribute;
    }

    public void setSearchAttribute(String searchAttribute) {
        this.searchAttribute = searchAttribute;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public List<Client> getList() {
        return list;
    }

    public void setList(List<Client> list) {
        this.list = list;
    }
}
