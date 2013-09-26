/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon
 */
public class MpcaPage {
    private URL mainPageURL;
    private URL commentsPageURL;

    public MpcaPage(URL mainPageURL) {
        this.mainPageURL = this.commentsPageURL = mainPageURL;
    }

    public MpcaPage(URL mainPageURL, URL commentsPageURL) {
        this.mainPageURL = mainPageURL;
        this.commentsPageURL = commentsPageURL;
    }
    
    public MpcaPage(String mainPageURL) throws MalformedURLException {
        this.mainPageURL = this.commentsPageURL = new URL(mainPageURL);
    }

    public MpcaPage(String mainPageURL, String commentsPageURL) throws MalformedURLException {
        this.mainPageURL = new URL(mainPageURL);
        this.commentsPageURL = new URL(commentsPageURL);
    }

    public URL getMainPageURL() {
        return mainPageURL;
    }

    public void setMainPageURL(String mainPageURL) throws MalformedURLException {
        this.mainPageURL = new URL(mainPageURL);
    }

    public void setMainPageURL(URL mainPageURL) {
        this.mainPageURL = mainPageURL;
    }

    public void setCommentsPageURL(URL commentsPageURL) {
        this.commentsPageURL = commentsPageURL;
    }

    public URL getCommentsPageURL() {
        return commentsPageURL;
    }

    public void setCommentsPageURL(String commentsPageURL) throws MalformedURLException {
        this.commentsPageURL = new URL(commentsPageURL);
    }
    
}
