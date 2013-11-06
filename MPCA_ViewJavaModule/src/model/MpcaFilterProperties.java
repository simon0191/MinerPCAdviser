/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import interfaces.MpcaIFilterProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import model.entities.MpcaProduct;

/**
 *
 * @author Antonio
 */
public class MpcaFilterProperties implements MpcaIFilterProperties {
    Map<String, MpcaProperty> properties;
    Set<MpcaProduct> products;

    public MpcaFilterProperties() {
        this.properties = new HashMap<String, MpcaProperty>();
        this.products = new TreeSet<MpcaProduct>();
    }
    
    public void addProperty(MpcaProperty property) {
        properties.put(property.getPropertyKey(), property);
    }
    
    public void addProperty(String key, String name, Set<String> values) {
        properties.put(key, new MpcaProperty(key, name, values));
    }
    
    @Override
    public Set<String> getProperties() {
        return properties.keySet();
    }
    
    @Override
    public MpcaProperty getProperty(String property) {
        return properties.get(property);
    }

    @Override
    public Set<MpcaProduct> getProducts() {
        return products;
    }

    public void setProducts(Set<MpcaProduct> products) {
        this.products = products;
    }
    
    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }
    
}
