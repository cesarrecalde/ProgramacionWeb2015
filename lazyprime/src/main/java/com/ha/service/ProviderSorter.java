package com.ha.service;

import com.ha.model.Provider;
import java.util.Comparator;
import org.primefaces.model.SortOrder;
import java.util.Objects;


/**
 * Created by isaacveron on 26/10/15.
 */
public class ProviderSorter implements Comparator<Provider> {

        private String sortField;

        private SortOrder sortOrder;

        public ProviderSorter(String sortField, SortOrder sortOrder) {
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(Provider provider1, Provider provider2) {
            try {
                //Object value1 = Provider.class.getField(this.sortField).get(provider1);
                //Object value2 = Provider.class.getField(this.sortField).get(provider2);
                String s1=null;
                String s2=null;

                if (sortField.equals("id")){
                    s1 = Objects.toString(provider1.getId());
                    s2 = Objects.toString(provider2.getId());
                }else{
                    s1 = Objects.toString(provider1.getName());
                    s2 = Objects.toString(provider2.getName());
                }

                int value = s1.compareTo(s2);

                return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
            }
            catch(Exception e) {
                throw new RuntimeException();
            }
        }
}
