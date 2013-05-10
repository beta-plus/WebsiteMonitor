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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.betaplus.core.WebsiteMonitor;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;
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
    public ArrayList<WebText> getTextFromUrl(WebSource url, String dataSource) {
        //The list of page content
        ArrayList<WebText> l = new ArrayList<WebText>();
        /**
         * Simply grab the text here and store it. It's unlikely that duplicate
         * pages will exist named differently, so no need to check for identical
         * match in large list.
         */
        WebText wt = removeMarkup(readHTML(url.getWebPageURL()), url.getWebPageURL(), url);
        if (wt != null) {
            l.add(wt);
        }
        for (String s : getLinks(readHTML(url.getWebPageURL()), dataSource, new ArrayList<String>(), new ArrayList<String>())) {
            wt = removeMarkup(readHTML(s), s, url);
            if (wt != null) {
                l.add(wt);
            }
        }
        return l;
    }

    /**
     * Returns a list of strings containing the content presented by a given RSS
     * feed. url represents the page to start looking from and dataSource is the
     * domain key word that will stop the search from leaving the current
     * domain.
     *
     * Example: getTextFromRss("http://www.foo.com/index", foo); Example:
     * getTextFromRss("http://www.foo.blog.com/index", foo.blog);
     *
     * @param url
     * @param dataSource
     * @return
     */
    @Override
    public ArrayList<RssFeed> getTextFromRss(WebSource url, String dataSource) {
        ArrayList<RssFeed> l = new ArrayList<RssFeed>();
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(url.getRssURL())));
            for (Object s : feed.getEntries()) {
                SyndEntry se = (SyndEntry) s;
                String linkTitle = se.getTitle().replace("'", "");
                String linkDes = se.getDescription().getValue().replace("'", "");
                Date linkPubDate = se.getPublishedDate();
                String linkLink = se.getLink().replace("'", "");
                l.add(new RssFeed(linkTitle, linkDes, linkTitle, linkDes, linkDes, linkLink, url));
            }

        } catch (MalformedURLException ex) {
            //Logger.getLogger(WebScraperImpl.class.getName()).log(Level.INFO, null, ex);
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
    public ArrayList<Pdf> getPDFsFromUrl(WebSource url, String dataSource,
            ArrayList<String> keyWords) {
        //The list of pdf's.
        ArrayList<Pdf> l = new ArrayList<Pdf>();
        try {
            l = extractPDFLinks(readHTML(url.getWebPageURL()), keyWords, l, url);
        } catch (Exception ex) {
            Logger.getLogger(WebScraperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Check each link for pdf's and retrive.
        int c = 0;
        for (String s : getLinks(readHTML(url.getWebPageURL()), dataSource, new ArrayList<String>(), new ArrayList<String>())) {
            try {
                ArrayList<Pdf> extractPDFLinks = extractPDFLinks(readHTML(s), keyWords, l, url);
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
    private ArrayList<String> getLinks(Document doc, String dataSource, ArrayList<String> lf, ArrayList<String> frameList) {
        Elements els;
        try {
            //parse links from body of page
            els = doc.body().select("a[href]");
        } catch (NullPointerException ex) {
            try {
                els = doc.body().select("onclick");
            } catch (NullPointerException exc) {
                try {
                    //If no links check for frames.
                    els = doc.getElementsByTag("frame");
                    for (Element e : els) {
                        String s;
                        if (!e.attr("src").contains(dataSource)) {
                            s = dataSource + e.attr("src");
                        } else {
                            s = e.attr("src");
                        }

                        if (frameList.isEmpty() || !frameList.contains(s)) {
                            frameList.add(s);
                            getLinks(readHTML(s), dataSource, lf, frameList);
                        }

                    }
                } catch (NullPointerException excp) {
                    els = new Elements();
                }
            }

        }
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
            if ((!inList && url.contains(dataSource)) || lf.isEmpty()) {
                lf.add(url);

                //if the link is new then it may have more links!
                getLinks(readHTML(url), dataSource, lf, frameList);
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
    private ArrayList<Pdf> extractPDFLinks(Document doc, ArrayList<String> keyWords, ArrayList<Pdf> lf, WebSource ws) throws Exception {
        //A list of urls used to gether links.
        ArrayList<String> ol = new ArrayList<String>();
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
                String nm = absUrl.replace(ws.getSource(), "");
                Pdf f = downloadFile(new URL(absUrl), "data", "document_" + nm, ws);
                if (f != null) {
                    for (Pdf fil : lf) {
                        ComparatorImpl c = new ComparatorImpl();
                        if (c.compareChecksums(fil.getPdfFile(), f.getPdfFile(), Comparator.SHA_512)) {
                            addFile = false;
                        }
                    }
                    if (addFile) {
                        lf.add(f);
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
     * Downloads a single file from a given URL.
     *
     * @param url
     * @param directory
     * @param name
     * @return
     */
    public static Pdf downloadFile(URL url, String directory, String name, WebSource ws) {
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
            File f = new File(directory + "/" + name + ".pdf");
            return new Pdf(getHash(f), f.getName(), url.toString(), f, ws);
        } catch (IOException ex) {

            return null;
        }
    }

    /**
     * Remove the markup from a given web page returning only the content.
     *
     * @param doc
     * @return
     */
    private WebText removeMarkup(Document doc, String webLink, WebSource url) {
        String pageText;
        String title;
        try {
            pageText = doc.body().text();
            try {
                title = doc.title();
                if (title.isEmpty()) {
                    title = "No Title";
                }
                return new WebText(pageText, title, webLink, url);
            } catch (NullPointerException ex) {
                return new WebText(pageText, webLink, webLink, url);
            }
        } catch (NullPointerException ex) {
            return null;
        }


    }

    private static String getHash(File f) {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return DigestUtils.md5Hex(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (IOException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
