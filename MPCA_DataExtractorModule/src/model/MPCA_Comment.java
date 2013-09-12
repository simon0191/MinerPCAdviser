/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Antonio
 */
public class MPCA_Comment {
    private double stars;
    private String comment;
    private String title;
    private Polarity polarity;
    private GregorianCalendar date;
    private String author;
    
    public MPCA_Comment(String title, double stars, String comment, Polarity pol, GregorianCalendar date, String author) {
        this.title = title;
        this.stars = stars;
        this.comment = comment;
        this.polarity = pol;
        this.date = date;
        this.author = author;
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

    public Polarity getPolarity() {
        return polarity;
    }

    public void setPolarity(Polarity pol) {
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
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "Comment{" + "author=" + author + ", stars=" + stars + ", title=" + title + ", date=" + date.get(Calendar.DAY_OF_MONTH) 
                    + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR) +
                ", polarity=" + polarity + ",\ncomment=" + comment + '}';
    }
    
}
