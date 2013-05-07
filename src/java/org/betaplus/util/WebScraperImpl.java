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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        l.add(removeMarkup(readHTML(url)));
        for (String s : getLinks(readHTML(url), dataSource, new LinkedList<String>())) {
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
     * Example: getTextFromRss("http://www.foo.com/index", foo); 
     * Example: getTextFromRss("http://www.foo.blog.com/index", foo.blog);
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
            LinkedList<String> keyWords) {
        //The list of pdf's.
        LinkedList<File> l = new LinkedList<File>();
        try {
            l = extractPDFLinks(readHTML(url), keyWords, l);
        } catch (Exception ex) {
            Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Check each link for pdf's and retrive.
        int c = 0;
        for (String s : getLinks(readHTML(url), dataSource, new LinkedList<String>())) {
            try {
                LinkedList<File> extractPDFLinks = extractPDFLinks(readHTML(s), keyWords, l);
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
            doc = Jsoup.connect(url).get();
            //Jsoup.parse(new URL(url), 100000);
        } catch (IOException ex) {
            doc = null;
        } catch (IllegalArgumentException ex) {
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
    private LinkedList<String> getLinks(Document doc, String dataSource, LinkedList<String> lf) {
        Elements els;
        try {
            //parse links from body of page
            els = doc.body().select("a[href]");
        } catch (NullPointerException ex) {
            try {
                //If no links check for frames.
                els = doc.getElementsByTag("frame");
                for (Element e : els) {
                    getLinks((Document) e, dataSource, lf);
                }
            } catch (NullPointerException e) {
                els = new Elements();
            }
        }
        lf.add("");
        //get URL of each link
        for (Element e : els) {
            String url = e.absUrl("href");
            //Check list for presence of link and source of link.            
            boolean inList = false;
            for (int i = 0; i < lf.size(); i++) {
                if (lf.get(i).equalsIgnoreCase(url)) {
                    inList = true;
                }
            }
            if (!inList && url.contains(dataSource)) {
                lf.add(url);
                System.out.println(url);
                //if the link is new then it may have more links!
                getLinks(readHTML(url), dataSource, lf);
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
    private LinkedList<File> extractPDFLinks(Document doc, LinkedList<String> keyWords, LinkedList<File> lf) throws Exception {
        //A list of urls used to gether links.
        LinkedList<String> ol = new LinkedList<String>();
        //A list of every link without prejudice.
        Elements els;
        try {
            els = doc.body().select("a[href]");
        } catch (NullPointerException ex) {
            els = new Elements();
        }
        ol.add("");
        boolean getFile = false;
        for (Element e : els) {
            //The String rep of a link found
            String absUrl = e.absUrl("href");
            //Check each keyword for presence in link
            for (String s : keyWords) {
                if (absUrl.contains(s)) {
                    getFile = true;
                    //Make sure link not already used
                    for (String usedURL : ol) {
                        if (usedURL.equalsIgnoreCase(absUrl)) {
                            getFile = false;
                        }
                    }
                }
            }
            boolean addFile = true;
            if (getFile) {
                String nm = absUrl.replace("http://www.lga.org.mt/common/file_provider.aspx?id=", "");
                File f = downloadFile(new URL(absUrl), "data", "document_" + nm);
                if (f != null) {
                    for (File fil : lf) {
                        ComparatorImpl c = new ComparatorImpl();
                        if (c.compareChecksums(fil, f, Comparator.SHA_512)) {
                            addFile = false;
                        }
                    }
                    if (addFile) {
                        lf.add(f);
                        System.out.println(absUrl);
                        System.out.println(lf.getLast().getName());
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
            System.out.println(f.getAbsolutePath());
        } catch (ClassCastException ex) {
            //Not a file found.
            f = null;
        } catch (IOException ex) {
            f = null;
        }
        return f;
    }

    /**
     * Downloads a single file from a given URL.
     *
     * @throws IOException ,MalformedURLException
     */
    public static File downloadFile(URL url, String directory, String name) {
        try {

            //Get a connection to the URL and start up a buffered reader.
            url.openConnection();
            InputStream reader = url.openStream();
            //Setup a buffered file writer to write out what is read from URL.
            FileOutputStream writer = new FileOutputStream(directory + "/" + name + ".pdf");
            byte[] buffer = new byte[153600];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[153600];
            }
            writer.close();
            reader.close();
            return new File(directory + "/" + name + ".pdf");
        } catch (IOException ex) {
            System.out.println("File Not Found...");
            return null;
        }
    }

    /**
     * Remove the markup from a given web page returning only the content.
     *
     * @param doc
     * @return
     */
    private String removeMarkup(Document doc) {
        String pageText;
        try {
            pageText = doc.body().text();
        } catch (NullPointerException ex) {
            pageText = "";
        }
        return pageText;
    }
}
