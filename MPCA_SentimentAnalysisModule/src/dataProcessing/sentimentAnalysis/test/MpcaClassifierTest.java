package dataProcessing.sentimentAnalysis.test;

import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.utils.MpcaIConstants;

/**
 *
 * @author SimonXPS
 */
public class MpcaClassifierTest {

    private MpcaDataSet data;
    private MpcaIClassifier classifier;
    private Map<String, Integer> correct;
    private Map<String, Integer> incorrect;
    private Map<String, Double> precision;
    private int totalCorrect;
    private int totalIncorrect;
    private double totalPrecision;
    private int testSize;
    private boolean tested;

    public MpcaClassifierTest(MpcaDataSet data, MpcaIClassifier classifier) {
        this.classifier = classifier;
        this.data = data;
        init();
    }
    
    private void init() {
        this.totalCorrect = 0;
        this.totalIncorrect = 0;
        this.totalPrecision = 0;
        this.testSize = 0;
        correct = new HashMap<String, Integer>();
        incorrect = new HashMap<String, Integer>();
        precision = new HashMap<String, Double>();

        for (String category : data.keySet()) {
            correct.put(category, 0);
            incorrect.put(category, 0);
            testSize+=data.get(category).size();
        }
        tested = false;
    }

    //TODO: lamparear haciendolo con varios hilos
    public synchronized void execute() throws Exception {
        init();
        Set<Map.Entry<String, List<String>>> entrySet = data.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String expectedCategory = entry.getKey();
            List<String> comments = entry.getValue();
            for (String comm : comments) {
                String category = classifier.bestMatch(comm);
                if (category.equalsIgnoreCase(expectedCategory)) {
                    correct.put(expectedCategory,
                            correct.get(expectedCategory) + 1);
                    totalCorrect++;
                } else {
                    System.out.println(comm);
                    incorrect.put(expectedCategory,
                            incorrect.get(expectedCategory) + 1);
                    totalIncorrect++;
                }
            }
        }
        for (Map.Entry<String, Integer> entry : correct.entrySet()) {
            precision.put(entry.getKey(),
                    ((double) entry.getValue())
                    / ((double) data.get(entry.getKey()).size()));
        }
        tested = true;
        totalPrecision = ((double)totalCorrect)/((double)testSize);
    }

    public boolean isTested() {
        return tested;
    }
    
    public MpcaTestResult getResults() {
        MpcaTestResult results = null;
        if(isTested()) {
            results = new MpcaTestResult(data, classifier, correct, incorrect, precision, totalCorrect, totalIncorrect, totalPrecision, testSize);
        }
        return results;
    }
}
