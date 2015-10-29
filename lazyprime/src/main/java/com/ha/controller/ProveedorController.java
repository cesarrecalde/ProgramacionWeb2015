package com.ha.controller;
import com.ha.data.ProviderRepository;
import com.ha.model.LazyProvider;
import com.ha.model.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Created by isaacveron on 25/10/15.
 */

@ViewScoped
@ManagedBean(name="proveedorController")
public class ProveedorController implements Serializable{

    @Inject
    private ProviderRepository providerRepository;

    private LazyDataModel<Provider> model;

    private Provider newProvider;

    @PostConstruct
    public void init() {
        this.newProvider = new Provider();
        model = new LazyProvider(providerRepository.findAllOrderedByName(0));
    }

    public LazyDataModel<Provider> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Provider> model) {
        this.model = model;
    }

    public Provider getNewProvider(){
        return this.newProvider;
    }

    public void setNewProvider(Provider provider){
        this.newProvider = provider;
    }

    public void registerProvider(){
        this.providerRepository.register( this.newProvider );
        this.newProvider = new Provider();
    }
}
