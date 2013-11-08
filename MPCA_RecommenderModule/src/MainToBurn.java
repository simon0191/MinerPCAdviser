/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;
import model.controllers.MpcaProductJpaController;
import model.entities.MpcaProduct;
import model.entities.MpcaProductAddition;
import model.utils.MpcaIConstants;
import recommender.MpcaRecommendation;
import recommender.MpcaRecommender;

/**
 *
 * @author Antonio
 */
public class MainToBurn {
    public static void main(String[] args) throws Exception {
        MpcaProductJpaController pc = new MpcaProductJpaController();
        List<MpcaProduct> products = pc.findMpcaProductEntities();
        System.out.println(products.size());
        for (MpcaProduct p : products) {
            System.out.println(p.getModel());
            String brand = null;
            String ram = null;
            String hd = null;
            for (MpcaProductAddition pa : p.getMpcaProductAdditionList()) {
                String addType = pa.getMpcaAdditionType().getAddType();
                switch (addType) {
                    case MpcaIConstants.BRAND_TAG:
                        brand =  pa.getValue();
                        break;
                    case MpcaIConstants.RAM_TAG:
                        String[] data = pa.getValue().split(" +");
                        ram =  data[0];
                        break;
                    case MpcaIConstants.HARD_DRIVE_TAG:
                        String[] d = pa.getValue().split(" +");
                        if(d[1].toLowerCase().contains("t")) {
                            d[0] = (Integer.parseInt(d[0]) * 1024) + "";
                        }
                        hd =  d[0];
                        break;
                }
            }
            System.out.println(brand);
            System.out.println(ram);
            System.out.println(hd);
            // Recomendation
            MpcaRecommendation recom = MpcaRecommender.getInstance().doRecommendation(p, 4l);
            System.out.println(recom.getDecision());
            System.out.println(recom.getPriority());
            System.out.println("null");
        }
    }
}
