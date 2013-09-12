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
import model.Polarity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Antonio
 */
public class NeweggCommentsExtractor implements ICommentsExtractor {
    
    private static ICommentsExtractor extractor = null;
    
    private NeweggCommentsExtractor() {}
    
    public static ICommentsExtractor getExtractor() {
        if(extractor == null) {
            extractor = new NeweggCommentsExtractor();
        }
        return extractor;
    }
    
    /**
     * Extrae los comentarios de una página de un computadore de Newegg a partir del tag donde 
     * se encuentran los comentarios como parámetro
     * 
     * @param comments
     *  Se recibe el tag tbody donde se encuentran los comentarios
     * @return Lista de comentarios
     */
    
    @Override
    public List<MPCA_Comment> commentExtractor(Element comments) {
        List<MPCA_Comment> coms = new ArrayList<>();
        Elements everyComment = comments.getElementsByTag("tr");
        for (Element ele : everyComment) {
            Element eleStarsTitle = ele.select("h3").first();
            Element eleTitle = eleStarsTitle.getElementsByTag("img").first();
            String className = eleTitle.className();
            double stars = Double.parseDouble(className.charAt(className.length()-1) + "");
            
            DateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
            GregorianCalendar datePosted = new GregorianCalendar();
            try {
                Date d = fmt.parse(ele.getElementsByTag("li").get(1).text());
                datePosted.setTime(d);
            } catch (ParseException ex) {
                Logger.getLogger(NeweggCommentsExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Elements singleComments = ele.select(".details").first().select("p");
            //Elements singleComments = ele.select("p");
            Elements singleComments = ele.select(".details p");
            //System.out.println(singleComments);
            for (Element e : singleComments) {
                
                String comnt = e.ownText();
                Element select = e.select("em").first();
                //if(!comnt.trim().equals("") && !e.className().equalsIgnoreCase("helpful") && select != null) {
                if(select != null) {
                    String pol = select.text();
                    Polarity p = null;
                    if(pol.equalsIgnoreCase("pros:")) {
                        p = Polarity.POSITIVE;
                    } else if(pol.equalsIgnoreCase("cons:")) {
                        p = Polarity.NEGATIVE;
                    } else {
                        p = Polarity.NEUTRAL;
                    }

                    String author = ele.select("th em").first().text();
                    if(author.equalsIgnoreCase("N/A")) {
                        author = null;
                    }

                    MPCA_Comment newComment = new MPCA_Comment(eleStarsTitle.ownText(), stars, comnt, p, datePosted, author);
                    coms.add(newComment);

    //                System.out.println(newComment);
    //                System.out.println("============================================");
                }
            }
            
        }
        return coms;
    }
    
}
