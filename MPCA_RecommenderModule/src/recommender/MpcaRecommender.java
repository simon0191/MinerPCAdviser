/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommender;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.controllers.MpcaIndexTypeJpaController;
import model.controllers.MpcaProductIndexJpaController;
import model.entities.MpcaIndexType;
import model.entities.MpcaProduct;
import model.entities.MpcaProductIndex;
import model.utils.MpcaIConstants;
import model.utils.MpcaPolarity;
import model.utils.Range;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class MpcaRecommender {
    
    private static MpcaRecommender recommender = null;
    
    private List<MpcaRecommendation> parameters;
    
    private MpcaRecommender() throws IOException {
        parameters = extractRecommendationsInXml();
    }
    
    public static MpcaRecommender getInstance() throws IOException {
        if(recommender == null) {
            recommender = new MpcaRecommender();
        }
        return recommender;
    }
    
    public MpcaRecommendation doRecommendation(MpcaProduct product, long indexId) throws IOException {
        MpcaIndexType index = new MpcaIndexTypeJpaController().findMpcaIndexType(indexId);
        MpcaRecommendation recommendation = null;
        if(index != null) {
            recommendation = doRecommendation(product, index);
        }
        return recommendation;
    }
    public MpcaRecommendation doRecommendation(MpcaProduct product, MpcaIndexType index) throws IOException {
        MpcaRecommendation finalRecomm = null;
        Collections.sort(parameters);
        MpcaProductIndexJpaController pic = new MpcaProductIndexJpaController();
        List<MpcaProductIndex> indexes = pic.findProductIndexByProductAndIndex(product, index);
        for (MpcaRecommendation parameter : parameters) {
            if(matches(indexes, parameter)) {
                finalRecomm = parameter;
                break;
            }
        }
        
        return finalRecomm;
    }

    private List<MpcaRecommendation> extractRecommendationsInXml() throws IOException {
        List<MpcaRecommendation> recommendations = new ArrayList<MpcaRecommendation>();
        Document doc = Jsoup.parse(new File(MpcaIConstants.RECOMMENDER_DESCRIPTOR_PATH, MpcaIConstants.RECOMMENDER_DESCRIPTOR_FILE), "UTF-8");
        Elements recommendationsElements = doc.select("recommendation");
        for (Element recommendation : recommendationsElements) {
            String decision = recommendation.attr("decision");
            int priority = Integer.parseInt(recommendation.attr("priority"));
            Elements polarities = recommendation.select("polarity");
            MpcaRecommendation recom = new MpcaRecommendation(decision, priority);
            for (Element polarity : polarities) {
                String polarityStr = polarity.attr("polarity");
                MpcaPolarity po = MpcaPolarity.getPolarity(polarityStr);
                
                String minValueStr = polarity.attr("min");
                String maxValueStr = polarity.attr("max");
                double minValue;
                double maxValue;
                if(minValueStr.trim().equals("")) {
                    minValue = 0d;
                } else {
                    minValue = Double.parseDouble(minValueStr);
                }
                
                if(maxValueStr.trim().equals("")) {
                    maxValue = 1d;
                } else {
                    maxValue = Double.parseDouble(maxValueStr);
                }
                recom.addPolarity(po, new Range(minValue, maxValue));
            }
            recommendations.add(recom);
        }
        return recommendations;
    }

    private boolean matches(List<MpcaProductIndex> indexes, MpcaRecommendation parameter) {
        for (MpcaPolarity polarity : parameter.getPolarities()) {
            for (MpcaProductIndex label : indexes) {
                if(label.getLabelId().getLabelName().equals(polarity.toString())) {
                    Range range = parameter.getRange(polarity);
                    BigDecimal value = label.getIndexValue();
                    if(value.compareTo(new BigDecimal(range.getMin())) < 0 || value.compareTo(new BigDecimal(range.getMax())) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
