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
public class MpcaEvaluator implements MpcaIEvaluator {
    
    private MpcaIClassifier classifier;

    @Override
    public List<MpcaComment> evaluate(List<MpcaComment> comments, MpcaIClassifier classifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MpcaComment evaluate(MpcaComment comments, MpcaIClassifier classifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
