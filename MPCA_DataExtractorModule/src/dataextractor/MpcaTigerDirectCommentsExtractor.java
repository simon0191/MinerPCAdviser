/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import interfaces.IMpcaCommentsExtractor;
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
import model.MpcaCommentModel;
import utils.Polarity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class MpcaTigerDirectCommentsExtractor implements IMpcaCommentsExtractor {
    
    private static IMpcaCommentsExtractor extractor = null;
    
    private MpcaTigerDirectCommentsExtractor() {}
    
    public static IMpcaCommentsExtractor getExtractor() {
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
            
            MpcaCommentModel newComment = new MpcaCommentModel(title, rate, comnt, Polarity.NEUTRAL, datePosted, author);
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
