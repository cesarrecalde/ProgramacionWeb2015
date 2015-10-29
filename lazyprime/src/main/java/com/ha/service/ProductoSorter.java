package com.ha.service;

import com.ha.model.Product;
import org.primefaces.model.SortOrder;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by isaacveron on 29/10/15.
 */
public class ProductoSorter  implements Comparator<Product> {
    private String sortField;

    private SortOrder sortOrder;

    public ProductoSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Product product1, Product product2) {
        try {
            //Object value1 = Provider.class.getField(this.sortField).get(provider1);
            //Object value2 = Provider.class.getField(this.sortField).get(provider2);
            String s1=null;
            String s2=null;

            if (sortField.equals("id")){
                s1 = Objects.toString(product1.getId());
                s2 = Objects.toString(product2.getId());
            }else if (sortField.equals("cantidad")){
                s1 = Objects.toString(product1.getCantidad());
                s2 = Objects.toString(product2.getCantidad());
            }else if (sortField.equals("nameProduct")){
                s1 = Objects.toString(product1.getNameProduct());
                s2 = Objects.toString(product2.getNameProduct());
            }
            else{
                s1 = Objects.toString(product1.getPrecioUnitario());
                s2 = Objects.toString(product2.getPrecioUnitario());
            }

            int value = s1.compareTo(s2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
