/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentAnalysis;

import java.util.List;

/**
 *
 * @author simon
 */
public interface IClassifier {
    void train(String category,List<String> reviews);
    String classify(String text) throws Exception;
}
