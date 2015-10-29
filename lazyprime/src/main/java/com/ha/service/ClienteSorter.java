package com.ha.service;

import com.ha.model.Provider;
import org.primefaces.model.SortOrder;

import java.util.Comparator;
import java.util.Objects;

import com.ha.model.Client;


/**
 * Created by isaacveron on 26/10/15.
 */
public class ClienteSorter implements Comparator<Client> {

    private String sortField;

    private SortOrder sortOrder;

    public ClienteSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Client client1, Client client2) {
        try {
            //Object value1 = Provider.class.getField(this.sortField).get(provider1);
            //Object value2 = Provider.class.getField(this.sortField).get(provider2);
            String s1=null;
            String s2=null;

            if (sortField.equals("id")){
                s1 = Objects.toString(client1.getId());
                s2 = Objects.toString(client2.getId());
            }else{
                s1 = Objects.toString(client1.getName());
                s2 = Objects.toString(client2.getName());
            }

            int value = s1.compareTo(s2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
