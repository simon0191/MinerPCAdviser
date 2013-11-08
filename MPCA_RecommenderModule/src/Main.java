
import java.util.List;
import model.controllers.MpcaProductJpaController;
import model.entities.MpcaProduct;
import model.utils.MpcaPolarity;
import model.utils.Range;
import recommender.MpcaRecommendation;
import recommender.MpcaRecommender;





/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SimonXPS
 */
public class Main {
    public static void main(String[] args) throws Exception {
        List<MpcaProduct> products = new MpcaProductJpaController().findMpcaProductEntities();
        MpcaRecommender recommender = MpcaRecommender.getInstance();
        System.out.println("============================================");
        for (MpcaProduct p : products) {
            MpcaRecommendation recommendation = recommender.doRecommendation(p, 4l);
            System.out.println("Id: " + p.getProductId());
            System.out.println("Model: " + p.getModel());
            System.out.println("- Recommendation:");
            String prefix = "\t";
            System.out.println(prefix + recommendation.getDecision());
            prefix += "\t";
            for (MpcaPolarity polarity : recommendation.getPolarities()) {
                System.out.println("\t" + "Polarity: " + polarity);
                Range range = recommendation.getRange(polarity);
                System.out.println(prefix + "Min: " + range.getMin() + ", Max: " + range.getMax());
            }
            System.out.println("========================================");
        }
    }
    
}
