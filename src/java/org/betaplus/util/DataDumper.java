/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import org.betaplus.datatypes.KeyWord;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.User;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;

/**
 * Interface defining addition of data to database.
 * 
 * @author StephenJohnRussell
 */
public interface DataDumper {
    
    /**
     * Store a new PDF from a given URL in the database.
     * @param pdf
     * @return 
     */
    public boolean dumpPDF(Pdf pdf);
    
    /**
     * Store the text from an RSS feed in the database.
     * @param rss
     * @param url
     * @return 
     */
    public boolean dumpRSS(RssFeed rss);
    
    /**
     * Store the text from a URL in the database.
     * @param webText
     * @return 
     */
    public boolean dumpWebText(WebText webText);
    
    /**
     * Store a User in the database.
     * @param user
     * @return 
     */
    public boolean dumpUser(User user);
    
    /**
     * Store a Keyword for a given WebSource.
     * @param keyWord
     * @return 
     */
    public boolean dumpKeyWord(KeyWord keyWord);
    
    /**
     * Store the details of a given WebSource in the database.
     * @param ws
     * @return 
     */
    public boolean dumpWebSource(WebSource ws);
}
