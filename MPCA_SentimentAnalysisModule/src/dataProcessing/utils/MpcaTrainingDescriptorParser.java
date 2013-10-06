package dataProcessing.utils;

import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import dataProcessing.sentimentAnalysis.exceptions.MpcaClassifierNotTrainedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.controllers.JpaController;
import model.controllers.MpcaCommentJpaController;
import model.controllers.MpcaLabelTypeJpaController;
import model.entities.MpcaComment;
import model.entities.MpcaLabelType;
import model.utils.MpcaIConstants;

/**
 *
 * @author simon
 */
public class MpcaTrainingDescriptorParser {

    public static MpcaITrainableClassifier getClassifier(File file) throws
            FileNotFoundException, ClassNotFoundException {
        MpcaITrainableClassifier classifier = parseAndTrain(file);
        return classifier;
    }

    private static MpcaITrainableClassifier parseAndTrain(File file) throws
            FileNotFoundException, ClassNotFoundException {
        Scanner scanner = new Scanner(file);
        //TODO: usar el nombre en algo...
        String name = scanner.nextLine();
        String className = scanner.nextLine();
        int numberOfCategories = scanner.nextInt();

        JpaController jpaController = new JpaController();
        EntityManager em = jpaController.getEntityManager();
        Map<String, List<String>> mapa = new HashMap<String, List<String>>();
        for (int i = 0; i < numberOfCategories; ++i) {
            String label = scanner.next();
            if (!mapa.containsKey(label)) {
                mapa.put(label, new ArrayList<String>());
            }
            int numberOfQueries = scanner.nextInt();
            List<MpcaComment> comments = new ArrayList<MpcaComment>();
            for (int j = 0; j < numberOfQueries; ++j) {
                int maxResults = scanner.nextInt();
                int firstResult = scanner.nextInt();

                String query = scanner.nextLine();
                Query q = em.createQuery(query);
                if (maxResults > 0) {
                    q.setMaxResults(maxResults);
                }
                if (firstResult > 0) {
                    q.setFirstResult(firstResult);
                }
                mapa.get(label).addAll(q.getResultList());
            }

        }
        return trainClassifier(className, mapa);
    }

    private static MpcaITrainableClassifier trainClassifier(String className,
            Map<String, List<String>> mapa) throws ClassNotFoundException {
        MpcaITrainableClassifier classifier =
                MpcaTrainableClassifierFactory.createClassifierByClassName(
                className, mapa.keySet().toArray(new String[]{}));
        classifier.train(mapa);
        return classifier;
    }
    private static final String CLASSIFIERS_DESCRIPTOR_PATH = "data/classifiers";
    private static final BigDecimal POSITIVE_ID = new BigDecimal("1");
    private static final BigDecimal NEGATIVE_ID = new BigDecimal("2");
    private static final MpcaLabelType POSITIVE_LABEL = new MpcaLabelTypeJpaController().findMpcaLabelType(POSITIVE_ID);
    private static final MpcaLabelType NEGATIVE_LABEL = new MpcaLabelTypeJpaController().findMpcaLabelType(NEGATIVE_ID);
    
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, MpcaClassifierNotTrainedException {
        
        
        String[] polarities = {"POSITIVE", "NEGATIVE"};
        List<MpcaComment> comments = new ArrayList<MpcaComment>();
                
        MpcaCommentJpaController commentsController = new MpcaCommentJpaController();
        for (String p : polarities) {
            comments.addAll(commentsController.findMpcaCommentByAdditionAndValue(MpcaIConstants.ADDITION_POLARITY,p));
        }
        
        File fileDescriptor = new File(CLASSIFIERS_DESCRIPTOR_PATH,"files.descriptor" );
        Scanner in = new Scanner(fileDescriptor);
        while(in.hasNext()) {
            String fileName = in.next();
            MpcaITrainableClassifier classifier = getClassifier(fileDescriptor);
            
            
            for(int i = 0;i<10;++i) {
                String testComment = comments.get(i).getCommentText();
                String category = classifier.classify(testComment);
                System.out.println("Test comment: "+testComment);
                System.out.println("Category:"+category);
                /*
                MpcaCommentIndex index = new MpcaCommentIndex();
                
                index.s
                index.setMpcaComment(c);
                classifier.classify(c);
                index.setLabelId( );
                * */
            }
            
        }
    }
}
