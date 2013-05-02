/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class implementing webScraper interface for WebsiteMonitor.
 *
 * @author StephenJohnRussell
 */
public class WebScraperImpl implements WebScraper {

    /**
     * Returns a list of strings containing the content presented at a given URL
     * and it's subdirectories. url represents the page to start looking from
     * and dataSource is the domain key word that will stop the search from
     * leaving the current domain.
     *
     * Example: getTextFromUrl("http://www.foo.com/index", foo); Example:
     * getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     *
     * @param url
     * @param dataSource
     * @return
     */
    @Override
    public LinkedList<String> getTextFromUrl(String url, String dataSource) {
        //The list of page content
        LinkedList<String> l = new LinkedList<String>();
        /**
         * Simply grab the text here and store it. It's unlikely that duplicate
         * pages will exist named differently, so no need to check for identical
         * match in large list.
         */
        for (String s : getLinks(readHTML(url), dataSource)) {
            l.add(removeMarkup(readHTML(s)));
        }
        return l;
    }

    /**
     * Returns a list of strings containing the content presented by a given RSS
     * feed. url represents the page to start looking from and dataSource is the
     * domain key word that will stop the search from leaving the current
     * domain.
     *
     * Example: getTextFromUrl("http://www.foo.com/index", foo); Example:
     * getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     *
     * @param url
     * @return
     */
    @Override
    public LinkedList<SyndEntry> getTextFromRss(String url, String dataSource) {
        LinkedList<SyndEntry> l = new LinkedList<SyndEntry>();
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(url)));
            l = (LinkedList) feed.getEntries();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FeedException ex) {
            Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    /**
     * Returns a list of file's containing the PDF's stored at a given URL and
     * it's subdirectories. url represents the page to start looking from and
     * dataSource is the domain key word that will stop the search from leaving
     * the current domain.
     *
     * Example: getTextFromUrl("http://www.foo.com/index", foo); Example:
     * getTextFromUrl("http://www.foo.blog.com/index", foo.blog);
     *
     * @param url
     * @return
     */
    @Override
    public LinkedList<File> getPDFsFromUrl(String url, String dataSource,
            String[] keyWords) {
        //The list of pdf's.
        LinkedList<File> l = new LinkedList<File>();
        //Check each link for pdf and retrive.
        for (String s : getLinks(readHTML(url), dataSource)) {
            try {
                for (File f : extractPDFLinks(readHTML(s), keyWords)) {
                    l.add(f);
                }
            } catch (Exception ex) {
                Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }

    /**
     * Returns a Document containing content parsed by Jsoup library.
     *
     * @param url
     * @return
     */
    private Document readHTML(String url) {
        //The Document to return
        Document doc;
        try {
            //Make String into URL & parse.
            doc = Jsoup.parse(new URL(url), 100000);
        } catch (IOException ex) {
            doc = null;
        }
        return doc;
    }

    /**
     * Returns a list of links generated by following each link found until link
     * does not contain the key word dataSource.
     *
     * @param doc
     * @return
     */
    private LinkedList<String> getLinks(Document doc, String dataSource) {
        //The list of links
        LinkedList<String> lf = new LinkedList<String>();
        //parse links from body of page
        Elements els = doc.body().select("a[href]");
        //get URL of each link
        for (Element e : els) {
            String url = e.absUrl("href");
            //Check list for presence of link and source of link.
            for (String s : lf) {
                if (!s.equalsIgnoreCase(url) && s.contains("dataSource")) {
                    lf.add(url);
                    //if the link is new then it may have more links!
                    getLinks(readHTML(url), dataSource);
                }
            }
        }
        return lf;
    }

    /**
     * Return a LinkedList of File's from a given document.
     *
     * @param doc
     * @param keyWords
     * @return
     * @throws Exception
     */
    private LinkedList<File> extractPDFLinks(Document doc, String[] keyWords) throws Exception {
        //The list of Files to return
        LinkedList<File> lf = new LinkedList<File>();
        //A list of urls used to gether links.
        LinkedList<String> ol = new LinkedList<String>();
        //A list of every link without prejudice.
        Elements els = doc.body().select("a[href]");
        for (Element e : els) {
            //The String rep of a link found
            String absUrl = e.absUrl("href");
            //Check each keyword for presence in link
            for (String s : keyWords) {
                if (absUrl.contains(s)) {
                    //Make sure link not already used
                    for (String usedURL : ol) {
                        if (!usedURL.equalsIgnoreCase(s)) {
                            lf.add(downloadFile(new URL(absUrl)));
                        }
                    }
                }
            }

        }
        return lf;
    }

    /**
     * Downloads a single file from a given URL.
     *
     * @throws IOException ,MalformedURLException
     */
    private File downloadFile(URL url) {
        //The file to return.
        File f;
        try {
            //Get a connection to the URL.
            url.openConnection();
            //Get content of connection
            f = (File) url.getContent();
        } catch (ClassCastException ex) {
            //Not a file found.
            f = null;
        } catch (IOException ex) {
            f = null;
        }
        return f;
    }

    /**
     * Remove the markup from a given web page returning only the content.
     *
     * @param doc
     * @return
     */
    private String removeMarkup(Document doc) {
        String pageText = doc.body().text();
        return pageText.replaceAll("'", "");
    }
}