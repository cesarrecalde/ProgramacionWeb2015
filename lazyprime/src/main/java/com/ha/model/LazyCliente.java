package com.ha.model;

import com.ha.service.ClienteSorter;

import java.util.*;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


/**
 * Created by isaacveron on 26/10/15.
 */
public class LazyCliente extends LazyDataModel<Client>{
    private List<Client> datasource;


    public LazyCliente(List<Client> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Client getRowData(String rowKey) {
        for(Client client : datasource) {

            String s = Objects.toString(client.getId());
            System.out.print(s);

            if(s.equals(rowKey)) {
                return client;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Client client) {
        return client.getId();
    }

    @Override
    public List<Client> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<Client> data = new ArrayList<Client>();


        //filter
        for(Client client : datasource) {
            boolean match = true;
            boolean ban = false;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        //busqueda por id y nombre
                        if (filterProperty.contains("id") && client.getId()==Integer.valueOf(filters.get("id").toString())){
                            data.add(client);
                            ban = true;
                        }

                        if (filterProperty.contains("name") && client.getName().equals(String.valueOf(filters.get("name").toString()))){
                            data.add(client);
                            ban = true;
                        }

                        String fieldValue = String.valueOf(client.getClass().getField(filterProperty).get(client));
                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                        }
                        else {
                            match = false;
                            break;
                        }

                    } catch(Exception e) {
                        match = false;
                    }
                }
            }

            if(match && !ban) {
                data.add(client);
            }
        }

        //sort
        if(sortField != null) {
            Collections.sort(data, new ClienteSorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }


}
