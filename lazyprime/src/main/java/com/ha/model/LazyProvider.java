package com.ha.model;

import com.ha.service.ProviderSorter;

import java.util.*;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


/**
 * Created by isaacveron on 26/10/15.
 */
public class LazyProvider extends LazyDataModel<Provider>{
    private List<Provider> datasource;


    public LazyProvider(List<Provider> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Provider getRowData(String rowKey) {
        for(Provider provider : datasource) {

            String s = Objects.toString(provider.getId());
            System.out.print(s);

            if(s.equals(rowKey)) {
                return provider;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Provider provider) {
        return provider.getId();
    }

    @Override
    public List<Provider> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<Provider> data = new ArrayList<Provider>();


        //filter
        for(Provider provider : datasource) {
            boolean match = true;
            boolean ban = false;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        //busqueda por id y nombre
                        if (filterProperty.contains("id") && provider.getId()==Integer.valueOf(filters.get("id").toString())){
                            data.add(provider);
                            ban = true;
                        }

                        if (filterProperty.contains("name") && provider.getName().equals(String.valueOf(filters.get("name").toString()))){
                            data.add(provider);
                            ban = true;
                        }

                        String fieldValue = String.valueOf(provider.getClass().getField(filterProperty).get(provider));
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
                data.add(provider);
            }
        }

        //sort
        if(sortField != null) {
            Collections.sort(data, new ProviderSorter(sortField, sortOrder));
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
