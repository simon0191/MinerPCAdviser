/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import interfaces.ICommentsExtractor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MPCA_Comment;
import utils.Polarity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class AmazonCommentsExtractor implements ICommentsExtractor {
    
    private static ICommentsExtractor extractor = null;
    
    private AmazonCommentsExtractor() {}
    
    public static ICommentsExtractor getExtractor() {
        if(extractor == null) {
            extractor = new AmazonCommentsExtractor();
        }
        return extractor;
    }
    
    @Override
    public List<MPCA_Comment> commentExtractor(Element comments) {
        List<MPCA_Comment> coms = new ArrayList<>();
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
                Logger.getLogger(AmazonCommentsExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            String comnt = singleComment.ownText();
            
            String author = singleComment.select("div[style=float:left;] span[style=font-weight: bold;]").first().text();
            
            MPCA_Comment newComment = new MPCA_Comment(title, rate, comnt, Polarity.NEUTRAL, datePosted, author);
            coms.add(newComment);
            
            //System.out.println(newComment);
            //System.out.println("=======================================");
        }
        
        return coms;
    }
    
}
