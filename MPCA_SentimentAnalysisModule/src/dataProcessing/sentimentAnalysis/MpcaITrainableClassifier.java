/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import dataProcessing.sentimentAnalysis.exceptions.MpcaClassifierNotTrainedException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author simon
 */
public interface MpcaITrainableClassifier extends MpcaIClassifier {
    void train(String category,List<String> reviews);
    void train(Map<String,List<String>> reviews);
    boolean isTrained();
    int trainingSize();
    @Override
    String classify(String text) throws MpcaClassifierNotTrainedException;
}
