/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.utils;

import dataProcessing.sentimentAnalysis.MpcaGooglePredictionAPIClassifier;
import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import dataProcessing.sentimentAnalysis.MpcaLingPipeClassifier;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author simon
 */
public class MpcaTrainableClassifierFactory {

    public static final String LING_PIPE = "dataProcessing.sentimentAnalysis.MpcaLingPipeClassifier";
    public static final String GOOGLE_HOSTED = "dataProcessing.sentimentAnalysis.MpcaGooglePredictionAPIClassifier";

    public static MpcaIClassifier createClassifierByClassName(
            String className, String[] categories) throws ClassNotFoundException, GeneralSecurityException, IOException, Exception {
        MpcaIClassifier classifier = null;
        switch (className) {
            case MpcaTrainableClassifierFactory.LING_PIPE:
                classifier = new MpcaLingPipeClassifier(categories);
                break;
            case MpcaTrainableClassifierFactory.GOOGLE_HOSTED:
                classifier = new MpcaGooglePredictionAPIClassifier();
                break;
            default:
                throw new ClassNotFoundException();
        }
        return classifier;
    }
}