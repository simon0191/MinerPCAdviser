package dataProcessing.sentimentAnalysis.test;

import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
    private static final String SEPARATOR = "------------------------------------------------------------------------------------------";

    private Map<String, List<String>> data;
    private MpcaIClassifier classifier;
    private Map<String, Integer> correct;
    private Map<String, Integer> incorrect;
    private Map<String, Double> precision;
    private int totalCorrect;
    private int totalIncorrect;
    private double totalPrecision;
    private int testSize;
    private boolean tested;

    public MpcaClassifierTest(Map<String, List<String>> data, MpcaIClassifier classifier) {
        tested = false;
        this.classifier = classifier;
        this.data = data;
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
    }

    //TODO: lamparear haciendolo con varios hilos
    public synchronized void execute() throws Exception {
        Set<Map.Entry<String, List<String>>> entrySet = data.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String expectedCategory = entry.getKey();
            List<String> comments = entry.getValue();
            for (String comm : comments) {
                String category = classifier.classify(comm);
                if (category.equalsIgnoreCase(expectedCategory)) {
                    correct.put(expectedCategory,
                            correct.get(expectedCategory) + 1);
                    totalCorrect++;
                } else {
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
    
    private static String createSeparator(String str) {
        StringBuilder sb = new StringBuilder();
        int dashes = SEPARATOR.length()-str.length()-2;
        for(int i = 0;i<dashes/2;++i) {
            sb.append('-');    
        }
        sb.append(' ');
        sb.append(str);
        sb.append(' ');
        for(int i = 0;i<dashes/2;++i) {
            sb.append('-');    
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(tested) {
            sb.append(SEPARATOR);
            sb.append("\n");
            sb.append(MpcaClassifierTest.createSeparator("TEST"));
            sb.append("\n");
            sb.append(SEPARATOR);
            sb.append("\n");
            sb.append("Total comments used fot test: "+testSize);
            sb.append("\n");
            for (String category : data.keySet()) {
                sb.append(String.format("Total %s comments: %d\n",category,data.get(category).size()));
                sb.append(String.format("%s correctly classified: %d\n",category,correct.get(category)));
                sb.append(String.format("%s incorrectly classified: %d\n",category,incorrect.get(category)));
                sb.append(String.format("Precision of %s comments: %.2f%c\n",category,precision.get(category)*100.0,'%'));
            }
            sb.append(SEPARATOR);    
            sb.append("\n");
            sb.append(String.format("Total precision: %.2f%c\n",totalPrecision*100.0,'%'));
            sb.append(SEPARATOR);
        }
        else {
            sb.append("NOT YET TRAINED");
        }
        return sb.toString();
    }
}
