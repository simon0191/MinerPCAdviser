/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.MpcaCommentJpaController;
import entities.MpcaComment;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import sentimentAnalysis.IClassifier;
import sentimentAnalysis.LingPipeClassifier;

/**
 *
 * @author simon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("...");
        MpcaCommentJpaController commentsController = new MpcaCommentJpaController();

        String[] polarities = {"POSITIVE", "NEGATIVE"};
        IClassifier classifier = new LingPipeClassifier(polarities);
        System.out.println("Classifying...");
        for (String p : polarities) {
            List<MpcaComment> comments = commentsController.findMpcaCommentByValueAndAddition(p, "polarity", 100, 0);
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
