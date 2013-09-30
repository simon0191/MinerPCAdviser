package dataProcessing.sentimentAnalysis;


import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import java.util.List;

/**
 * @author simon
 *
 */
public class MpcaLingPipeClassifier implements MpcaIClassifier {

    private DynamicLMClassifier<NGramProcessLM> classifier;
    private boolean isTrained;
    private int trainingSize;
    
    public MpcaLingPipeClassifier(String[] categories) {
        classifier = DynamicLMClassifier.createNGramProcess(categories, 8);
        isTrained = false;
        trainingSize = 0;
    }
    public MpcaLingPipeClassifier(String[] categories, int maxCharNGram) {
        classifier = DynamicLMClassifier.createNGramProcess(categories, maxCharNGram);
    }
    @Override
    public synchronized void train(String category,List<String> reviews) {
        Classification classification = new Classification(category);
        for(String r:reviews) {
            Classified<CharSequence> classified = new Classified<CharSequence>(r, classification);
            classifier.handle(classified);
        }
        trainingSize = reviews.size();
        isTrained = true;
        //TODO: Intentar compilar el clasificador
        /*
        try {
            JointClassifier<CharSequence> compiledClassifier = 
                    (JointClassifier<CharSequence>)AbstractExternalizable.compile(classifier);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MpcaLingPipeClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MpcaLingPipeClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        * */
        
    }

    @Override
    public String classify(String text) {
        Classification classification = classifier.classify(text);
        return classification.bestCategory();
    }    

    @Override
    public String[] getCategories() {
        return this.classifier.categories();
    }

    @Override
    public boolean isTrained() {
        return this.isTrained;
    }

    @Override
    public int trainingSize() {
        return this.trainingSize;
    }
}