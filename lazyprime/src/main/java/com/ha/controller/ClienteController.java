package com.ha.controller;


import com.ha.data.ClientRepository;
import com.ha.model.Client;
import com.ha.model.LazyCliente;
import com.ha.model.Client;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by isaacveron on 25/10/15.
 */


@ViewScoped
@ManagedBean(name="clienteController")
public class ClienteController {
    @Inject
    private ClientRepository clienteRepository;

    @Inject
    private Logger log;

    private LazyDataModel<Client> model;

    private List<Client> clienteList;

    private Client newCliente;

    @PostConstruct
    public void init() {
        this.newCliente = new Client();
        model = new LazyCliente(clienteRepository.findAllOrderedByName(0));
    }

    public LazyDataModel<Client> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Client> model) {
        this.model = model;
    }


    public List<Client> getClienteList(){
        return this.clienteList;
    }
    public void setClienteList(List<Client> lista){
        this.clienteList = lista;
    }
    public Client getNewCliente(){
        return this.newCliente;
    }

    public void setNewCliente(Client cliente){
        this.newCliente = cliente;
    }

    public void registerCliente(){
        this.clienteRepository.register(this.newCliente);
        this.newCliente = new Client();
    }
}
