/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Set;

/**
 *
 * @author Antonio
 */
public class MpcaProperty {
    private String propertyKey;
    private String propertyName;
    private Set<String> propertyValues;

    public MpcaProperty(String propertyKey, String propertyName, Set<String> propertyValues) {
        this.propertyKey = propertyKey;
        this.propertyName = propertyName;
        this.propertyValues = propertyValues;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Set<String> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(Set<String> propertyValues) {
        this.propertyValues = propertyValues;
    }
    
}
