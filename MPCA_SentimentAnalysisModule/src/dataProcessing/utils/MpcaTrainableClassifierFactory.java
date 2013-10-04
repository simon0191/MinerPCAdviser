/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.utils;

import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import dataProcessing.sentimentAnalysis.MpcaLingPipeClassifier;

/**
 *
 * @author simon
 */
class MpcaTrainableClassifierFactory {

    public static final String LING_PIPE =
            "dataProcessing.sentimentAnalysis.MpcaLingPipeClassifier";

    public static MpcaITrainableClassifier createClassifierByClassName(
            String className, String[] categories) throws ClassNotFoundException {
        MpcaITrainableClassifier classifier = null;
        switch (className) {
            case MpcaTrainableClassifierFactory.LING_PIPE:
                classifier = new MpcaLingPipeClassifier(categories);
                break;
            default:
                throw new ClassNotFoundException();
        }
        return classifier;
    }
}