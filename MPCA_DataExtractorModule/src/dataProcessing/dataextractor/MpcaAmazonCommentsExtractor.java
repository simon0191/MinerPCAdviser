/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.dataextractor;

import dataProcessing.interfaces.MpcaICommentsExtractor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dataProcessing.model.MpcaCommentModel;
import model.utils.MpcaPolarity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class MpcaAmazonCommentsExtractor implements MpcaICommentsExtractor {
    
    private static MpcaICommentsExtractor extractor = null;
    
    private MpcaAmazonCommentsExtractor() {}
    
    public static MpcaICommentsExtractor getExtractor() {
        if(extractor == null) {
            extractor = new MpcaAmazonCommentsExtractor();
        }
        return extractor;
    }
    
    @Override
    public List<MpcaCommentModel> commentExtractor(Element comments) {
        List<MpcaCommentModel> coms = new ArrayList<>();
        Elements everyComment = comments.select("div[style=margin-left:0.5em;]");
        for (Element singleComment : everyComment) {
            double rate = Double.parseDouble(singleComment.select("div>span>span>span").first().text().substring(0, 3));
            String title = singleComment.select("div>span>b").first().text();
            String eleDate = singleComment.select("div>span>nobr").first().text();
            
            DateFormat fmt = new SimpleDateFormat("MMM dd, yyyy");
            GregorianCalendar datePosted = new GregorianCalendar();
            try {
                Date d = fmt.parse(eleDate);
                datePosted.setTime(d);
            } catch (ParseException ex) {
                Logger.getLogger(MpcaAmazonCommentsExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            String comnt = singleComment.ownText();
            
            String author = singleComment.select("div[style=float:left;] span[style=font-weight: bold;]").first().text();
            
            MpcaCommentModel newComment = new MpcaCommentModel(title, rate, comnt, MpcaPolarity.NEUTRAL, datePosted, author);
            coms.add(newComment);
            //System.out.println(newComment);
            //System.out.println("=======================================");
        }
        return coms;
    }

    @Override
    public String brandExtractor(Element brandEle) {
        Elements eles = brandEle.select("tr");
        for (Element ele : eles) {
            Elements block = ele.select("td");
            Element title = block.first();
            
            if(title.text().toLowerCase().contains("brand")) {
                return block.get(1).text().trim().toUpperCase();
            }
        }
        return null;
    }

    @Override
    public String modelExtractor(Element modelEle) {
        Elements eles = modelEle.select("tr");
        for (Element ele : eles) {
            Elements block = ele.select("td");
            Element title = block.first();
            
            if(title.text().toLowerCase().contains("model")) {
                return block.get(1).text().trim().toUpperCase();
            }
        }
        return null;
    }
    
}
