package dataProcessing.sentimentAnalysis.utils;

import dataProcessing.sentimentAnalysis.persistence.MpcaIndexPersistence;
import dataProcessing.sentimentAnalysis.test.MpcaTestResult;
import dataProcessing.sentimentAnalysis.test.MpcaTranierAndTester;
import java.io.File;
import java.util.List;
import model.utils.MpcaIConstants;

/**
 *
 * @author simon
 */
public class MpcaTrainingDescriptorParser {
    public static void main(String[] args) throws Exception {
        
        MpcaTranierAndTester trainerAndTester = new MpcaTranierAndTester(new File(MpcaIConstants.CLASSIFIERS_DESCRIPTOR_PATH, "trainingAndTest.xml"));
        //List<MpcaTestResult> trainAndTest = trainerAndTester.trainAndTest();
        System.out.println("Training...");
        trainerAndTester.train();
        System.out.println("Testing...");
        List<MpcaTestResult> trainAndTest = trainerAndTester.test();
        MpcaTestResult betterResult = null;
        for (MpcaTestResult result : trainAndTest) {
            //System.out.println(result);
            if(betterResult == null) {
                betterResult = result;
            }
            if(betterResult.getTotalPrecision() < result.getTotalPrecision()) {
                betterResult = result;
            }
        }
        System.out.println(betterResult);
        MpcaIndexPersistence.persistIndex(betterResult.getClassifier());
    }
}
