package dataProcessing.sentimentAnalysis.utils;

import dataProcessing.sentimentAnalysis.test.MpcaClassifierTest;
import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author simon
 */
public class MpcaTrainingDescriptorParser {

    public static MpcaITrainableClassifier getClassifier(File file) throws
            FileNotFoundException, ClassNotFoundException, IOException {
        return parseAndTrain(file);
    }

    private static MpcaITrainableClassifier parseAndTrain(File file) throws
            FileNotFoundException, ClassNotFoundException, IOException {
        String className;
        MpcaDataSet dataSet;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            className = bf.readLine();
            String dataSetFileName = bf.readLine();
            dataSet = MpcaDataSet.createDataSet(new File(CLASSIFIERS_DESCRIPTOR_PATH,dataSetFileName));
        }
        return trainClassifier(className, dataSet);
    }

    private static MpcaITrainableClassifier trainClassifier(String className,
            MpcaDataSet dataSet) throws ClassNotFoundException {
        MpcaITrainableClassifier classifier =
                MpcaTrainableClassifierFactory.createClassifierByClassName(
                className, dataSet.keySet().toArray(new String[]{}));
        classifier.train(dataSet);
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
        
        /*String[] polarities = {"POSITIVE", "NEGATIVE"};
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
            
        }*/
        
        File testDescriptor = new File(CLASSIFIERS_DESCRIPTOR_PATH, "files.descriptor");
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(testDescriptor)));
        String classifierClass = bf.readLine();
        File dataSetFile = new File(bf.readLine());
        MpcaDataSet testData = MpcaDataSet.createDataSet(dataSetFile);

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
