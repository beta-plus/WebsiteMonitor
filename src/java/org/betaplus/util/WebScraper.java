/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import com.sun.syndication.feed.synd.SyndEntry;
import java.io.File;
import java.util.LinkedList;

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
    public LinkedList<String> getTextFromUrl(String url, String dataSource);
    
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
    public LinkedList<SyndEntry> getTextFromRss(String url, String dataSource);
    
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
    public LinkedList<File> getPDFsFromUrl(String url, String dataSource, 
            String[] keyWords);
    
    
}
