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
import java.util.Locale;
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
public class MpcaTigerDirectCommentsExtractor implements MpcaICommentsExtractor {
    
    private static MpcaICommentsExtractor extractor = null;
    
    private MpcaTigerDirectCommentsExtractor() {}
    
    public static MpcaICommentsExtractor getExtractor() {
        if(extractor == null) {
            extractor = new MpcaTigerDirectCommentsExtractor();
        }
        return extractor;
    }

    @Override
    public List<MpcaCommentModel> commentExtractor(Element comments) {
        List<MpcaCommentModel> coms = new ArrayList<>();
        
        //Elements everyComment = comments.select("div#customerReviews");
        Elements everyComment = comments.select("div#customerReviews>div#customerReviews");
        for (Element singleCom : everyComment) {
            Element ss = singleCom.select("div.review>div.leftCol").first();
            Element rr = ss.select("dl.itemReview>dd>div.itemRating>strong").first();
            double rate = Double.parseDouble(rr.text());
            
            DateFormat fmt = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            GregorianCalendar datePosted = new GregorianCalendar();
            try {
                Date d = fmt.parse(ss.select("dl.reviewer>dd").get(1).text());
                datePosted.setTime(d);
            } catch (ParseException ex) {
                Logger.getLogger(MpcaTigerDirectCommentsExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            String title = singleCom.select("div.rightCol h6").first().text();
            String comnt = singleCom.select("div.rightCol p").first().text();
            
            String author = singleCom.select("dl.reviewer>dd").first().text();
            if(author.trim().equalsIgnoreCase("")) {
                author = null;
            }
            
            MpcaCommentModel newComment = new MpcaCommentModel(title, rate, comnt, MpcaPolarity.NEUTRAL, datePosted, author);
            coms.add(newComment);
            
            
            System.out.println(newComment);
            System.out.println("============================================");
        }
        return coms;
    }

    @Override
    public String brandExtractor(Element brandEle) {
        return brandEle.text().trim().toUpperCase();
    }

    @Override
    public String modelExtractor(Element modelEle) {
        String modelLine = modelEle.text();
        String modelTag = "model#:";
        int indexOf = modelLine.toLowerCase().indexOf(modelTag);
        return modelLine.substring(indexOf+modelTag.length()+1).trim().toUpperCase();
    }
    
}
