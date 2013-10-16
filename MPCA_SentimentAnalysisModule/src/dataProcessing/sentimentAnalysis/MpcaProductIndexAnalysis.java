/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.controllers.MpcaCommentIndexJpaController;
import model.controllers.MpcaProductJpaController;
import model.entities.MpcaCommentIndex;
import model.entities.MpcaIndexType;
import model.entities.MpcaProduct;

/**
 *
 * @author Antonio
 */
public class MpcaProductIndexAnalysis {
    
    public static List<MpcaProductResult> calculateAllProductsIndex(MpcaIndexType index) {
        MpcaProductJpaController pc = new MpcaProductJpaController();
        List<MpcaProduct> products = pc.findMpcaProductEntities();
        List<MpcaProductResult> results = new ArrayList<MpcaProductResult>();
        for (MpcaProduct product : products) {
            System.out.println("Calculating Product...");
            results.add(calculateProductIndex(product, index));
        }
        return results;
    }
    
    public static MpcaProductResult calculateProductIndex(MpcaProduct product, MpcaIndexType index) {
        MpcaCommentIndexJpaController cic = new MpcaCommentIndexJpaController();
        System.out.println("Extracting Comments...");
        List<MpcaCommentIndex> commentIndex = cic.findMpcaCommentIndexByProductAndIndexType(product, index);
        System.out.println("Calculating Comments...");
        Map<String, Double> classifications = new HashMap<String, Double>();
        Map<String, Integer> classCount = new HashMap<String, Integer>();
        for (MpcaCommentIndex ci : commentIndex) {
            String classification = ci.getLabelId().getLabelName();
            if(!classifications.containsKey(classification)) {
                classifications.put(classification, 0.0);
                classCount.put(classification, 0);
            }
            classifications.put(classification, classifications.get(classification) + ci.getIndexValue().doubleValue());
            classCount.put(classification, classCount.get(classification) + 1);
        }
        for (String finalClass : classifications.keySet()) {
            double tot = classifications.get(finalClass);
            double count = classCount.get(finalClass);
            classifications.put(finalClass, tot / count);
        }
        return new MpcaProductResult(product, classifications);
    }
}
