/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.JpaController;
import controllers.MpcaCommentJpaController;
import entities.MpcaComment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sentimentAnalysis.MpcaGooglePredictionAPIClassifier;
import sentimentAnalysis.MpcaIClassifier;
import sentimentAnalysis.MpcaLingPipeClassifier;

/**
 *
 * @author simon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException, Exception {
        System.out.println("...");
        MpcaCommentJpaController commentsController = new MpcaCommentJpaController();

        String[] polarities = {"POSITIVE", "NEGATIVE"};
        MpcaIClassifier classifier = new MpcaGooglePredictionAPIClassifier(polarities);
        
        int posTraining = 432;
        int negTraining = 432;
        /*
        System.out.println("Classifying...");
        
        StringBuilder sb = new StringBuilder();
        for (String p : polarities) {
            List<MpcaComment> comments = commentsController.findMpcaCommentByValueAndAddition(p, "polarity");
            posTraining+=(p.equals("POSITIVE")?comments.size():0);
            negTraining+=(p.equals("NEGATIVE")?comments.size():0);
            
            List<String> reviews = new ArrayList<String>();
            for (MpcaComment c : comments) {
                reviews.add(c.getCommentText());
                sb.append('"');
                sb.append(p);
                sb.append("\", \"");
                sb.append(c.getCommentText().replaceAll("\"", "'"));
                sb.append("\"\n");
            }
            classifier.train(p, reviews);
        }
        File f;
        FileWriter fw = null;
        try {
            f = new File("trainingData.csv");
            f.createNewFile();
            fw = new FileWriter(f);
            fw.append(sb);
        
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            fw.close();
        }
        */
        
        int totalTraining = posTraining+negTraining;        
        
        Map<String,List<String>> ranks = new HashMap<String,List<String>>();
        ranks.put("NEGATIVE", Arrays.asList("1.0","2.0"));
        ranks.put("POSITIVE", Arrays.asList("4.0","5.0"));
        
        Double[] correct = {0.0,0.0};
        Double[] incorrect = {0.0,0.0};
        int positivePos = 0;
        int negativePos = 1;
        
        for (String p : polarities) {
            int pos = (p.equals("POSITIVE")?positivePos:negativePos);
            for(String r: ranks.get(p)) {
                List<MpcaComment> comments = commentsController.findMpcaCommentByAdditionAndValue(JpaController.ADDITION_RANK,r, 100, 0);
                //List<MpcaComment> comments = commentsController.findMpcaCommentByValueAndAddition(r, "rank", 100, 0);
                for (MpcaComment c : comments) {
                    
                    if(classifier.classify(c.getCommentText()).equals(p)) {
                        correct[pos]+=1.0;
                    }
                    else {
                        incorrect[pos]+=1.0;
                    }
                }
            }
            
        }
        
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("----------------------------------- TRAINING ---------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total comments used for training: "+totalTraining);
        System.out.println("Total positive comments used for training: "+posTraining);
        System.out.println("Total negative comments used for training: "+negTraining);
        
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------- TEST -----------------------------------------------");
        System.out.println("------------------------------------------------------------------------------------------");
        int totalPos = (incorrect[positivePos].intValue()+correct[positivePos].intValue());
        System.out.println("Total Positive comments: "+totalPos);
        System.out.println("Positive correctly classified: "+correct[positivePos].intValue());
        System.out.println("Positive not correctly classified: "+incorrect[positivePos].intValue());
        System.out.printf("Accuracy of positive comments: %.2f%c\n",correct[positivePos]/totalPos*100.0,'%');
        System.out.println("------------------------------");
        
        int totalNeg = (incorrect[negativePos].intValue()+correct[negativePos].intValue());
        System.out.println("Total Negative comments: "+totalNeg);
        System.out.println("Negative correctly classified: "+correct[negativePos].intValue());
        System.out.println("Negative not correctly classified: "+incorrect[negativePos].intValue());
        System.out.printf("Accuracy of negative comments: %.2f%c\n",correct[negativePos]/totalNeg*100.0,'%');
        
        System.out.println("------------------------------");
        
        int total = totalNeg+totalPos;
        System.out.println("Total comments: "+total);
        System.out.printf("Total accuracy: %.2f%c\n",(correct[positivePos]+correct[negativePos])/total*100.0,'%');
        System.out.println("------------------------------------------------------------------------------------------");
        
        
        
    }
    
    public static void interactiveMain() throws Exception {
         System.out.println("...");
        MpcaCommentJpaController commentsController = new MpcaCommentJpaController();

        String[] polarities = {"POSITIVE", "NEGATIVE"};
        MpcaIClassifier classifier = new MpcaLingPipeClassifier(polarities);
        System.out.println("Classifying...");
        for (String p : polarities) {
            List<MpcaComment> comments = commentsController.findMpcaCommentByAdditionAndValue(JpaController.ADDITION_POLARITY,p, 500, 0);
            List<String> reviews = new ArrayList<String>();
            for (MpcaComment c : comments) {
                reviews.add(c.getCommentText());
            }
            classifier.train(p, reviews);
        }
        System.out.println("Ready to classify:...");
        
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();
        String line;
        while (scanner.hasNextLine()) {
            if (!(line = scanner.nextLine().trim()).equals("**")) {
                sb.append(line);
            } else if (line.equals("++")) {
                break;
            } else {
                String c = classifier.classify(sb.toString());
                System.out.println("---------------------------------");
                System.out.println("Best category: " + c);
                System.out.println("---------------------------------");
                sb = new StringBuffer();
            }

        }
    }
}
