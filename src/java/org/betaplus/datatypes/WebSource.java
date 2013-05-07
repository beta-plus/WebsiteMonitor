/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

import java.io.File;
import java.util.LinkedList;

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
    private LinkedList<String> rssText;
    private LinkedList<File> pdfDocs;
    private LinkedList<String> webText;
    private LinkedList<String> keyWords;

    public WebSource(String source, String webPageURL, String rssURL, LinkedList<String> keyWords) {
        this.source = source;
        this.webPageURL = webPageURL;
        this.rssURL = rssURL;
        this.keyWords = keyWords;
    }

    

    public LinkedList<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(LinkedList<String> keyWords) {
        this.keyWords = keyWords;
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

    public LinkedList<String> getRssText() {
        return rssText;
    }

    public void setRssText(LinkedList<String> rssText) {
        this.rssText = rssText;
    }

    public LinkedList<File> getPdfDocs() {
        return pdfDocs;
    }

    public void setPdfDocs(LinkedList<File> pdfDocs) {
        this.pdfDocs = pdfDocs;
    }

    public LinkedList<String> getWebText() {
        return webText;
    }

    public void setWebText(LinkedList<String> webText) {
        this.webText = webText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
}
