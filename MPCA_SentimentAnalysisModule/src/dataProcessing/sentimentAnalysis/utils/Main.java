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
public class Main {

    public static void main(String[] args) throws Exception {

        MpcaTranierAndTester trainerAndTester = new MpcaTranierAndTester(new File(MpcaIConstants.CLASSIFIERS_DESCRIPTOR_PATH, "trainingAndTest.xml"));
        //List<MpcaTestResult> trainAndTest = trainerAndTester.trainAndTest();
        System.out.println("Training...");
        trainerAndTester.train();
        System.out.println("Testing...");
        List<MpcaTestResult> trainAndTest = trainerAndTester.test();
        MpcaTestResult bestResult = null;
        for (MpcaTestResult result : trainAndTest) {
            //System.out.println(result);
            if (bestResult == null) {
                bestResult = result;
            }
            if (bestResult.getTotalPrecision() < result.getTotalPrecision()) {
                bestResult = result;
            }
        }
        System.out.println(bestResult);
        // TODO: Cambiar Test2 por el nombre del clasificador
        MpcaIndexPersistence.persistIndex(bestResult.getClassifier(), "LingPipe2", true);
    }
}
