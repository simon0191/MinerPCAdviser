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

    public MpcaTranierAndTester(File xml) {
        this.xml = xml;
        classifiersDirectory = new HashMap<String, MpcaIClassifier>();
    }
    
    public List<MpcaTestResult> trainAndTest() throws IOException, ClassNotFoundException, Exception {
        Element xmlDoc = Jsoup.parse(xml, "UTF-8").select("traningandtest").first();
        Elements dataSets = xmlDoc.select("datasets");
        dataSet = MpcaDataSet.createDataSet(dataSets);
        Elements classifiers = xmlDoc.select("classifiers");
        trainClassifiers(classifiers);
        Elements tests = xmlDoc.select("test");
        return testClassifiers(tests);
    }

    private void trainClassifiers(Elements classifiers) throws ClassNotFoundException {
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
            MpcaITrainableClassifier classifier = MpcaTrainableClassifierFactory.createClassifierByClassName(className, categories);
            classifier.train(finalDataSet);
            classifiersDirectory.put(id, classifier);
        }
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
