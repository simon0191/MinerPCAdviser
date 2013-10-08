/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

import dataProcessing.sentimentAnalysis.test.MpcaClassifierTest;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;

/**
 *
 * @author simon
 */
public interface MpcaIClassifier {
    
    String classify(String text) throws Exception;
    String[] getCategories();
    MpcaClassifierTest createTest(MpcaDataSet testData);
}
