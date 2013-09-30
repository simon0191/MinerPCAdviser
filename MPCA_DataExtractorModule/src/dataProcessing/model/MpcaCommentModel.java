/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.model;

import model.utils.MpcaPolarity;
import java.util.Calendar;
import java.util.GregorianCalendar;
import model.controllers.JpaController;
import model.utils.MpcaIConstants;

/**
 *
 * @author Antonio
 */
public class MpcaCommentModel {
    private double stars;
    private String comment;
    private String title;
    private MpcaPolarity polarity;
    private GregorianCalendar date;
    private String author;
    
    public MpcaCommentModel(String title, double stars, String comment, MpcaPolarity pol, GregorianCalendar date, String author) {
        this.title = normalizeTitle(title);
        this.stars = stars;
        this.comment = comment;
        this.polarity = pol;
        this.date = date;
        this.author = normalizeAuthor(author);
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MpcaPolarity getPolarity() {
        return polarity;
    }

    public void setPolarity(MpcaPolarity pol) {
        this.polarity = pol;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = normalizeAuthor(author);
    }
    
    private static String normalizeAuthor(String authorUsername) {
        if(authorUsername != null) {
            authorUsername = authorUsername.trim();
        }
        if(authorUsername == null || authorUsername.equalsIgnoreCase("na") || authorUsername.equalsIgnoreCase("n/a") || 
                authorUsername.equalsIgnoreCase("") || authorUsername.equalsIgnoreCase(",")) {
            authorUsername = MpcaIConstants.NA_AUTHOR;
        }
        return authorUsername;
    }
    
    private static String normalizeTitle(String title) {
        if(title == null || title.trim().equals("")) {
            title = MpcaIConstants.NO_TITLE;
        }
        return title;
    }
    
    @Override
    public String toString() {
        return "Comment{" + "author=" + author + ", stars=" + stars + ", title=" + title + ", date=" + date.get(Calendar.DAY_OF_MONTH) 
                    + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR) +
                ", polarity=" + polarity + ",\ncomment=" + comment + '}';
    }
    
}
