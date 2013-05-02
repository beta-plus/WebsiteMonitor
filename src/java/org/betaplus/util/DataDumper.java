/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;

/**
 * Interface defining addition of data to database.
 * 
 * @author StephenJohnRussell
 */
public interface DataDumper {
    
    /**
     * Store a new PDF from a given URL in the database.
     * @param pdf
     * @param url
     * @return 
     */
    public boolean dumpPDF(File pdf, String url);
    
    /**
     * Store the text from an RSS feed in the database.
     * @param rss
     * @param url
     * @return 
     */
    public boolean dumpRSS(String rss, String url);
    
    /**
     * Store the text from a URL in the database.
     * @param webText
     * @param url
     * @return 
     */
    public boolean dumpWebText(String webText, String url);
    
    
    
}
