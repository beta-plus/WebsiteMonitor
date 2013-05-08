/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

/**
 * Class defining data held on a given web source.
 * 
 * @author StephenJohnRussell  
 * @version 0.1
 */
public class WebSource {
    
    private String source;
    private String webPageURL;
    private String rssURL;
    private int sourceID;

    public WebSource(String source, String webPageURL, String rssURL, int sourceID) {
        this.source = source;
        this.webPageURL = webPageURL;
        this.rssURL = rssURL;
        this.sourceID = sourceID;
    }

    public String getWebPageURL() {
        return webPageURL;
    }

    public void setWebPageURL(String webPageURL) {
        this.webPageURL = webPageURL;
    }

    public String getRssURL() {
        return rssURL;
    }

    public void setRssURL(String rssURL) {
        this.rssURL = rssURL;
    }
    
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
    }
    
}
