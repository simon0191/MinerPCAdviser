package sentimentAnalysis;


import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import java.util.List;

/**
 * @author simon
 *
 */
public class LingPipeClassifier implements IClassifier {

    private DynamicLMClassifier<NGramProcessLM> classifier;

    public LingPipeClassifier(String[] categories) {
        classifier = DynamicLMClassifier.createNGramProcess(categories, 8);
    }
    public LingPipeClassifier(String[] categories, int maxCharNGram) {
        classifier = DynamicLMClassifier.createNGramProcess(categories, maxCharNGram);
    }
    @Override
    public synchronized void train(String category,List<String> reviews) {
        Classification classification = new Classification(category);
        for(String r:reviews) {
            Classified<CharSequence> classified = new Classified<CharSequence>(r, classification);
            classifier.handle(classified);
        }
    }

    @Override
    public String classify(String text) {
        Classification classification = classifier.classify(text);
        return classification.bestCategory();
    }    
}