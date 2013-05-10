/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.util.ArrayList;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;

/**
 * Interface defining the tasks associated with retrieving data from given  web
 * sources. 
 * @author StephenJohnRussell
 */
public interface WebScraper {
    
    /**
     * Returns a list of strings containing the content presented at a given URL
     * and it's subdirectories.  url represents the page to start looking from 
     * and dataSource is the domain key word that will stop the search from 
     * leaving the current domain.
     * 
     * Example: getTextFromUrl("http://www.foo.com/index", foo);
     * Example: getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     * 
     * @param url
     * @param dataSource
     * @return 
     */
    public ArrayList<WebText> getTextFromUrl(WebSource url, String dataSource);
    
    /**
     * Returns a list of SyndEntry containing the content presented by a given 
     * RSS feed. SyndEntry is a library defined encapsulation of RSS data.
     * 
     * url represents the page to start looking from 
     * and dataSource is the domain key word that will stop the search from 
     * leaving the current domain.
     * 
     * Example: getTextFromUrl("http://www.foo.com/index", foo);
     * Example: getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     * 
     * @param url
     * @return 
     */
    public ArrayList<RssFeed> getTextFromRss(WebSource url, String dataSource);
    
    /**
     * Returns a list of file's containing the PDF's stored at a given URL
     * and it's subdirectories.  url represents the page to start looking from, 
     * dataSource is the domain key word that will stop the search from 
     * leaving the current domain and keyWords is a list of words which ensure a
     * given file is actually a pdf.
     * 
     * Example: getTextFromUrl("http://www.foo.com/index", foo);
     * Example: getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     * 
     * @param url
     * @param dataSource
     * @param keyWords
     * @return 
     */
    public ArrayList<Pdf> getPDFsFromUrl(WebSource url, String dataSource, 
            ArrayList<String> keyWords);
    
    
}
