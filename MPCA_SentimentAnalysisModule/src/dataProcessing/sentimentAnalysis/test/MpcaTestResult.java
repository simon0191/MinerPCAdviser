/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.test;

import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;
import java.util.Map;
import model.utils.MpcaIConstants;

/**
 *
 * @author Antonio
 */
public class MpcaTestResult {

    private MpcaDataSet data;
    private MpcaIClassifier classifier;
    private Map<String, Integer> correct;
    private Map<String, Integer> incorrect;
    private Map<String, Double> precision;
    private int totalCorrect;
    private int totalIncorrect;
    private double totalPrecision;
    private int testSize;

    public MpcaTestResult(MpcaDataSet data, MpcaIClassifier classifier, Map<String, Integer> correct, Map<String, Integer> incorrect, Map<String, Double> precision, int totalCorrect, int totalIncorrect, double totalPrecision, int testSize) {
        this.data = data;
        this.classifier = classifier;
        this.correct = correct;
        this.incorrect = incorrect;
        this.precision = precision;
        this.totalCorrect = totalCorrect;
        this.totalIncorrect = totalIncorrect;
        this.totalPrecision = totalPrecision;
        this.testSize = testSize;
    }

    public MpcaIClassifier getClassifier() {
        return classifier;
    }

    public Map<String, Integer> getCorrect() {
        return correct;
    }

    public Map<String, Integer> getIncorrect() {
        return incorrect;
    }

    public Map<String, Double> getPrecision() {
        return precision;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public int getTotalIncorrect() {
        return totalIncorrect;
    }

    public double getTotalPrecision() {
        return totalPrecision;
    }

    public int getTestSize() {
        return testSize;
    }

    private static String createSeparator(String str) {
        StringBuilder sb = new StringBuilder();
        int dashes = MpcaIConstants.SEPARATOR.length() - str.length() - 2;
        for (int i = 0; i < dashes / 2; ++i) {
            sb.append('-');
        }
        sb.append(' ');
        sb.append(str);
        sb.append(' ');
        for (int i = 0; i < dashes / 2; ++i) {
            sb.append('-');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MpcaIConstants.SEPARATOR);
        sb.append("\n");
        sb.append(MpcaTestResult.createSeparator("TEST"));
        sb.append("\n");
        sb.append(MpcaIConstants.SEPARATOR);
        sb.append("\n");
        sb.append("Total comments used fot test: ");
        sb.append(testSize);
        sb.append("\n");
        for (String category : data.keySet()) {
            sb.append(String.format("Total %s comments: %d\n", category, data.get(category).size()));
            sb.append(String.format("%s correctly classified: %d\n", category, correct.get(category)));
            sb.append(String.format("%s incorrectly classified: %d\n", category, incorrect.get(category)));
            sb.append(String.format("Precision of %s comments: %.2f%c\n", category, precision.get(category) * 100.0, '%'));
        }
        sb.append(MpcaIConstants.SEPARATOR);
        sb.append("\n");
        sb.append(String.format("Total precision: %.2f%c\n", totalPrecision * 100.0, '%'));
        sb.append(MpcaIConstants.SEPARATOR);
        return sb.toString();
    }
}
