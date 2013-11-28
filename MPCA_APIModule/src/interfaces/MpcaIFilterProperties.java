/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Set;
import model.MpcaProperty;
import model.entities.MpcaProduct;

/**
 *
 * @author Antonio
 */
public interface MpcaIFilterProperties {

    Set<MpcaProduct> getProducts();
    Set<String> getProperties();
    MpcaProperty getProperty(String property);
    boolean containsProperty(String key);
    
}
