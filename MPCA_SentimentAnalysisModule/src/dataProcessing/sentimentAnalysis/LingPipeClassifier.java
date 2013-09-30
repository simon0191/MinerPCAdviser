package dataProcessing.sentimentAnalysis;


import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.JointClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.AbstractExternalizable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //TODO: Intentar compilar el clasificador
        /*
        try {
            JointClassifier<CharSequence> compiledClassifier = 
                    (JointClassifier<CharSequence>)AbstractExternalizable.compile(classifier);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LingPipeClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LingPipeClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        * */
        
    }

    @Override
    public String classify(String text) {
        Classification classification = classifier.classify(text);
        return classification.bestCategory();
    }    
}