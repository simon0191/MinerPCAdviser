/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.test;

import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import dataProcessing.sentimentAnalysis.MpcaITrainableClassifier;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;
import dataProcessing.sentimentAnalysis.utils.MpcaTrainableClassifierFactory;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class MpcaTranierAndTester {
    private Map<String, MpcaIClassifier> classifiersDirectory;
    private MpcaDataSet dataSet;
    private File xml;
    private Element xmlDoc;
    private boolean trained;

    public MpcaTranierAndTester(File xml) throws IOException {
        this.xml = xml;
        xmlDoc = Jsoup.parse(xml, "UTF-8").select("traningandtest").first();
        Elements dataSets = xmlDoc.select("datasets");
        dataSet = MpcaDataSet.createDataSet(dataSets);
        classifiersDirectory = new HashMap<String, MpcaIClassifier>();
        trained = false;
    }
    
    public Map<String, MpcaIClassifier> train() throws ClassNotFoundException, GeneralSecurityException, IOException, Exception {
        Map<String, MpcaIClassifier> classifiersTrained = null;
        if(!trained) {
            Elements classifiers = xmlDoc.select("classifiers");
            classifiersTrained = trainClassifiers(classifiers);
        }
        return classifiersTrained;
    }
    
    public List<MpcaTestResult> test() throws Exception {
        List<MpcaTestResult> results = null;
        if(trained) {
            Elements tests = xmlDoc.select("test");
            results = testClassifiers(tests);
        }
        return results;
    }
    
    public List<MpcaTestResult> trainAndTest() throws ClassNotFoundException, Exception {
        train();
        return test();
    }

    private Map<String, MpcaIClassifier> trainClassifiers(Elements classifiers) throws ClassNotFoundException, GeneralSecurityException, IOException, Exception {
        Elements allClassifiers = classifiers.select("classifier");
        for (Element everyClassifier : allClassifiers) {
            String id = everyClassifier.id();
            String className = everyClassifier.className();
            
            Elements categoriesEles = everyClassifier.select("category");
            String []categories = new String[categoriesEles.size()];
            MpcaDataSet finalDataSet = new MpcaDataSet();
            int i = 0;
            for (Element catEle : categoriesEles) {
                String category = catEle.attr("name");
                categories[i++] = category;
                Elements traningDataSets = catEle.select("dataset");
                List<String> finalComms = new ArrayList<String>();
                for (Element dataSetEle : traningDataSets) {
                    String dataSetId = dataSetEle.attr("ref");
                    finalComms.addAll(dataSet.get(dataSetId));
                }
                finalDataSet.put(category, finalComms);
            }
            MpcaIClassifier classifier = MpcaTrainableClassifierFactory.createClassifierByClassName(className, categories);
            if(classifier instanceof MpcaITrainableClassifier) {
                ((MpcaITrainableClassifier)classifier).train(finalDataSet);    
            }
            classifiersDirectory.put(id, classifier);
        }
        trained = true;
        return classifiersDirectory;
    }

    private List<MpcaTestResult> testClassifiers(Elements tests) throws Exception {
        Elements allTests = tests.select("test");
        List<MpcaTestResult> results = new ArrayList<MpcaTestResult>();
        for (Element test : allTests) {
            MpcaDataSet finalDataSet = new MpcaDataSet();
            String classifierId = test.select("classifier").first().attr("ref");
            Elements expectedCategories = test.select("expectedCategory");
            for (Element expected : expectedCategories) {
                String polarity = expected.attr("name");
                List<String> finalComms = new ArrayList<String>();
                Elements testDataSets = expected.select("dataSet");
                for (Element testDataSet : testDataSets) {
                    String dataSetId = testDataSet.attr("ref");;
                    finalComms.addAll(dataSet.get(dataSetId));
                }
                finalDataSet.put(polarity, finalComms);
            }
            MpcaIClassifier classifier = classifiersDirectory.get(classifierId);
            MpcaClassifierTest testClassifier = classifier.createTest(finalDataSet);
            testClassifier.execute();
            results.add(testClassifier.getResults());
        }
        return results;
    }
}
