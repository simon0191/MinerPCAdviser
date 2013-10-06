/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis;

/**
 *
 * @author simon
 */
public interface MpcaIClassifier {
    
    String classify(String text) throws Exception;
    String[] getCategories();
    
}
