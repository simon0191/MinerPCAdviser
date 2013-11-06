/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.MpcaFilterFeature;
import java.util.Set;
import model.MpcaFilterProperties;
import interfaces.MpcaIFilterProperties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TreeSet;
import model.MpcaProperty;
import model.entities.MpcaProduct;

/**
 *
 * @author Antonio
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        MpcaIFilterProperties filterProperties = MpcaFilterFeature.getFilterProperties(true);
        MpcaProperty filterSelected;
        MpcaFilterProperties filteredProperties = new MpcaFilterProperties();
        while((filterSelected = printFilter(filterProperties)) != null) {
            filteredProperties.addProperty(filterSelected);
            filterProperties = MpcaFilterFeature.getFilterProperties(filteredProperties, true);
        }
        
    }

    private static MpcaProperty printFilter(MpcaIFilterProperties filterPropeties) throws Exception {
        Set<String> properties = filterPropeties.getProperties();
        int k = 0;
        for (String property : properties) {
            MpcaProperty p = filterPropeties.getProperty(property);
            System.out.println((++k) + ". Name = " + p.getPropertyName());
            Set<String> values = p.getPropertyValues();
            int i = 0;
            for (String v : values) {
                System.out.println("\t" + (++i) + ". " + v);
            }
            System.out.println("===================================");
        }
        System.out.println("Products:");
        for (MpcaProduct p : filterPropeties.getProducts()) {
            System.out.println(p.getModel());
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println("Choose your property X and value Y: \"X Y\"");
        
        
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String []numbers = bf.readLine().split(" +");
        int property = Integer.parseInt(numbers[0]);
        int value = Integer.parseInt(numbers[1]);
        int i = 0;
        MpcaProperty propertySelected = null;
        for (String p : properties) {
            if(i == property-1) {
                int j = 0;
                MpcaProperty prop = filterPropeties.getProperty(p);
                for (String v : prop.getPropertyValues()) {
                    if(j == value-1) {
                        //propertySelected = new Pair<String, Pair<String, Set<String>>>(p, filterPropeties.getProperty(p));
                        Set<String> newValue = new TreeSet<String>();
                        newValue.add(v);
                        propertySelected = new MpcaProperty(p, prop.getPropertyName(), newValue);
                        break;
                    }
                    j++;
                }
                break;
            }
            i++;
        }
        return propertySelected;
    }
}
