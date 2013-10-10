/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Antonio
 */
public class MpcaClassification extends HashMap<String, Double> {
    public Set<String> getCategories() {
        return keySet();
    }
}
