package com.ha.model;

import com.ha.service.ProductoSorter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.*;

/**
 * Created by isaacveron on 28/10/15.
 */
public class LazyProducto extends LazyDataModel<Product> {


    private List<Product> datasource;


    public LazyProducto(List<Product> datasource) {
        this.datasource = datasource;
    }

    @Override
    public Product getRowData(String rowKey) {
        for(Product product : datasource) {

            String s = Objects.toString(product.getId());
            System.out.print(s);

            if(s.equals(rowKey)) {
                return product;
            }
        }

        return null;
    }

    @Override
    public Object getRowKey(Product product) {
        return product.getId();
    }

    @Override
    public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {

        List<Product> data = new ArrayList<Product>();

        //filter
        for(Product product : datasource) {
            boolean match = true;
            boolean ban = false;

            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);

                        //busqueda por id cantidad nombre y precio
                        if (filterProperty.contains("id") && product.getId()==Integer.valueOf(filters.get("id").toString())){
                            data.add(product);
                            ban = true;
                        }

                        if (filterProperty.contains("cantidad") && product.getCantidad() == Integer.valueOf(filters.get("cantidad").toString())){
                            data.add(product);
                            ban = true;
                        }
                        if (filterProperty.contains("nameProduct") && product.getNameProduct().equals(String.valueOf(filters.get("nameProduct").toString()))){
                            data.add(product);
                            ban = true;
                        }
                        if (filterProperty.contains("precioUnitario") && product.getPrecioUnitario() == Integer.valueOf(filters.get("precioUnitario").toString())){
                            data.add(product);
                            ban = true;
                        }

                        String fieldValue = String.valueOf(product.getClass().getField(filterProperty).get(product));
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
                data.add(product);
            }
        }

        //sort
        if(sortField != null) {
            Collections.sort(data, new ProductoSorter(sortField, sortOrder));
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
