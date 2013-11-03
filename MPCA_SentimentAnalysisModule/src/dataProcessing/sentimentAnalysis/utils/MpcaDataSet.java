package dataProcessing.sentimentAnalysis.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.controllers.JpaController;
import model.entities.MpcaComment;
import model.utils.MpcaIConstants;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class MpcaDataSet extends HashMap<String, List<String>> {

    public MpcaDataSet() {
        super();
    }
    
    
    public static MpcaDataSet createDataSet(Elements dataSets) {
        Elements everyDataSet = dataSets.select("dataset");
        MpcaDataSet dataSet = new MpcaDataSet();
        for (Element element : everyDataSet) {
            String id = element.id();
            element = element.select("query").first();
            List<String> commentsS = extractComments(element);
            dataSet.put(id, commentsS);
        }
        return dataSet;
    }
    
    private static List<String> extractComments(Element element) throws NumberFormatException {
        int maxResults = -1;
        int offset = -1;
        if(element.hasAttr(MpcaIConstants.MAX_RESULTS_TAG)) {
            maxResults = Integer.parseInt(element.attr(MpcaIConstants.MAX_RESULTS_TAG));
        }
        if(element.hasAttr(MpcaIConstants.OFFSET_TAG)) {
            offset = Integer.parseInt(element.attr(MpcaIConstants.OFFSET_TAG));
        }
        String query = element.text();
        List<MpcaComment> comments = JpaController.doQuery(query, maxResults, offset);
        List<String> commentsS = new ArrayList<String>();
        for (MpcaComment comm : comments) {
            commentsS.add(comm.getCommentText());
        }
        return commentsS;
    }
    
}
