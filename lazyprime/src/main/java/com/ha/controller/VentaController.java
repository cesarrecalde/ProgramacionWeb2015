package com.ha.controller;

import com.ha.data.VentaRepository;
import com.ha.model.Venta;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by cesar on 01/11/15.
 */
@ViewScoped
@ManagedBean(name = "ventaController")
public class VentaController {

    @Inject
    private VentaRepository repository;

    private List<Venta> list;

    private String searchKey;

    private String searchAttribute;

    private String order;

    private String orderAttribute;

    private Integer page;

    private StreamedContent file;

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


    public void downloadCSV() throws Exception{

        String filePath = this.repository.getCSVFile(searchAttribute,searchKey,orderAttribute,order);

        File file = new File(filePath);

        InputStream input = new FileInputStream(file);

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        this.file = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());

    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
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
    public List<Venta> getList() {
        return list;
    }
    public void setList(List<Venta> ventaList) {
        this.list = ventaList;
    }
    public String getOrderAttribute() {
        return orderAttribute;
    }
    public void setOrderAttribute(String orderAttribute) {
        this.orderAttribute = orderAttribute;
    }


}
