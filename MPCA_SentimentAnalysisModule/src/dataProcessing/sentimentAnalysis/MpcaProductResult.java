/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import java.util.Map;
import java.util.Set;
import model.entities.MpcaIndexType;
import model.entities.MpcaProduct;

/**
 *
 * @author Antonio
 */
public class MpcaProductResult {
    private MpcaProduct product;
    private MpcaIndexType index;
    private Map<String, Double> classificationsIndex;

    public MpcaProductResult(MpcaProduct product, MpcaIndexType index, Map<String, Double> classificationsIndex) {
        this.product = product;
        this.index = index;
        this.classificationsIndex = classificationsIndex;
    }

    public MpcaProduct getProduct() {
        return product;
    }

    public MpcaIndexType getIndex() {
        return index;
    }
    
    public Set<String> getClassifications() {
        return classificationsIndex.keySet();
    }
    
    public Double getClassificationValue(String classification) {
        return classificationsIndex.get(classification);
    }

    @Override
    public String toString() {
        String text = "Product: " + product.getModel() + ", Index: " + index.getIndexName() + "\n";
        for (Map.Entry<String, Double> entry : classificationsIndex.entrySet()) {
            text += "Classification: " + entry.getKey() + ", Probability: " + entry.getValue();
        }
        return text;
    }
    
}
