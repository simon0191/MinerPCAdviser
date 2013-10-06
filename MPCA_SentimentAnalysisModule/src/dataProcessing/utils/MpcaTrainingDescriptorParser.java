package dataProcessing.utils;

import dataProcessing.sentimentAnalysis.MpcaClassifierTest;
import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import dataProcessing.sentimentAnalysis.exceptions.MpcaClassifierNotTrainedException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
            FileNotFoundException, ClassNotFoundException, IOException {
        MpcaITrainableClassifier classifier = parseAndTrain(file);
        return classifier;
    }

    private static MpcaITrainableClassifier parseAndTrain(File file) throws
            FileNotFoundException, ClassNotFoundException, IOException {
        //Scanner scanner = new Scanner(file);
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        //TODO: usar el nombre en algo...
        String name = bf.readLine();
        String className = bf.readLine();
        int numberOfCategories = Integer.parseInt(bf.readLine());

        JpaController jpaController = new JpaController();
        EntityManager em = jpaController.getEntityManager();
        Map<String, List<String>> mapa = new HashMap<String, List<String>>();
        for (int i = 0; i < numberOfCategories; ++i) {
            String[] line = bf.readLine().split(" +");
            String label = line[0];
            int numberOfQueries = Integer.parseInt(line[1]);
            if (!mapa.containsKey(label)) {
                mapa.put(label, new ArrayList<String>());
            }

            List<MpcaComment> comments = new ArrayList<MpcaComment>();
            for (int j = 0; j < numberOfQueries; ++j) {
                int maxResults = Integer.parseInt(bf.readLine());
                int firstResult = Integer.parseInt(bf.readLine());

                String query = bf.readLine();
                Query q = em.createQuery(query);
                if (maxResults > 0) {
                    q.setMaxResults(maxResults);
                }
                if (firstResult > 0) {
                    q.setFirstResult(firstResult);
                }
                comments = q.getResultList();
                for (MpcaComment mc : comments) {
                    mapa.get(label).add(mc.getCommentText());
                }
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
    /*
    private static final BigDecimal POSITIVE_ID = new BigDecimal("1");
    private static final BigDecimal NEGATIVE_ID = new BigDecimal("2");
    
    private static final MpcaLabelType POSITIVE_LABEL = new MpcaLabelTypeJpaController().findMpcaLabelType(POSITIVE_ID);
    private static final MpcaLabelType NEGATIVE_LABEL = new MpcaLabelTypeJpaController().findMpcaLabelType(NEGATIVE_ID);
    * */

    public static void main(String[] args) throws Exception {


        String[] polarities = {"POSITIVE", "NEGATIVE"};
        Map<String, List<String>> testData = new HashMap<String, List<String>>();
        for (String p : polarities) {
            testData.put(p, new ArrayList<String>());
        }
        MpcaCommentJpaController commentsController = new MpcaCommentJpaController();
        for (String p : polarities) {
            List<MpcaComment> mpcaComms = commentsController.findMpcaCommentByAdditionAndValue(MpcaIConstants.ADDITION_POLARITY, p);
            for (MpcaComment mc : mpcaComms) {
                if(mc.getPageId().getPageId() == 2) {
                    testData.get(p).add(mc.getCommentText());    
                }
            }
            
        }

        File fileDescriptor = new File(CLASSIFIERS_DESCRIPTOR_PATH, "files.descriptor");
        Scanner in = new Scanner(fileDescriptor);
        System.out.println("Classifying");
        while (in.hasNext()) {
            String fileName = in.next();
            MpcaITrainableClassifier classifier = getClassifier(new File(CLASSIFIERS_DESCRIPTOR_PATH, fileName));
            MpcaClassifierTest test = classifier.createTest(testData);
            test.execute();
            System.out.println(test.toString());
        }
    }
}
