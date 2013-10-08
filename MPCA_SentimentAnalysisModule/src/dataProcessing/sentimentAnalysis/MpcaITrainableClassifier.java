/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import dataProcessing.sentimentAnalysis.exceptions.MpcaClassifierNotTrainedException;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;
import java.util.List;

/**
 *
 * @author simon
 */
public interface MpcaITrainableClassifier extends MpcaIClassifier {
    void train(String category,List<String> reviews);
    void train(MpcaDataSet reviews);
    boolean isTrained();
    int trainingSize();
    @Override
    String classify(String text) throws MpcaClassifierNotTrainedException;
}
