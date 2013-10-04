/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.evaluator;

import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import java.util.List;
import model.entities.MpcaComment;

/**
 *
 * @author simon
 */
public interface MpcaIEvaluator {
    List<MpcaComment> evaluate(List<MpcaComment> comments, MpcaIClassifier classifier);
    MpcaComment evaluate(MpcaComment comments, MpcaIClassifier classifier);
        
}
