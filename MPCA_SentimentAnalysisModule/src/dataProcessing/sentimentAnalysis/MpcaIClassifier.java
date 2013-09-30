/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import java.util.List;

/**
 *
 * @author simon
 */
public interface MpcaIClassifier {
    void train(String category,List<String> reviews);
    String classify(String text) throws Exception;
    String[] getCategories();
    boolean isTrained();
    int trainingSize();
}
