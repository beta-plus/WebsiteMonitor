/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;
import org.betaplus.util.ComparatorImpl;
import org.betaplus.util.DataDumperImpl;
import org.betaplus.util.DataRetrieverImpl;
import org.betaplus.util.WebScraperImpl;

/**
 *
 * @author StephenJohnRussell
 */
public class WebsiteMonitor {
    
    private static WebScraperImpl scraper;
    private static ComparatorImpl compare;
    private static DataRetrieverImpl retrieve;
    private static DataDumperImpl dumper;
    private static LinkedList<String> keyWords;
    private static LinkedList<String> fileHashes;
    private static LinkedList<String> webContent;    
    private static LinkedList<String> rssContent;    

    public WebsiteMonitor() {
        scraper = new WebScraperImpl();
        compare = new ComparatorImpl();
        retrieve = new DataRetrieverImpl();
        dumper = new DataDumperImpl();
    }

    public static void main(String args[]) {

        WebsiteMonitor websMon = new WebsiteMonitor();
        LinkedList<WebSource> dataSources = websMon.getDataSources();
        for (WebSource ws : dataSources) {
            initLists(ws);
            LinkedList<File> pdFsFromUrl = scraper.getPDFsFromUrl(ws.getWebPageURL(), ws.getSource(), keyWords);
            for (File pdf : pdFsFromUrl) {
                boolean gotIt = false;
                String newHash = websMon.getHash(pdf);
                for (String hash : fileHashes) {                    
                    if (hash.equalsIgnoreCase(newHash)) {
                        gotIt = true;
                    }
                }
                if (!gotIt || fileHashes.size() == 0) {
                    dumper.dumpPDF(new Pdf(newHash, pdf.getName(), pdf, ws));
                }
            }
            LinkedList<String> textFromUrl = scraper.getTextFromUrl(ws.getWebPageURL(), ws.getSource());
            LinkedList<String> newText = new LinkedList<String>();            
            for (String newWebCont : textFromUrl) {
                boolean gotIt = false;
                for (String webCont : webContent) {                    
                    if (webCont.equalsIgnoreCase(newWebCont)) {
                        gotIt = true;
                    }
                }
                if (!gotIt || webContent.size() == 0) {
                    newText.add(newWebCont);                    
                }
            }
            if (newText.size() > 0) {
                dumper.dumpWebText(new WebText(newText, "Title", ws));
            }
        }
    }
    
    private String getHash(File f) {
        InputStream is = null;
        try {
            is = new FileInputStream(f);            
            return DigestUtils.sha512Hex(is);
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

    private LinkedList<WebSource> getDataSources() {
        LinkedList<WebSource> sources = new LinkedList<WebSource>();
        try {
            ResultSet r = retrieve.getTable("Urls");
            while (r.next()) {
                sources.add(new WebSource(r.getString("Url_Name"), r.getString("Http_Url"), r.getString("Rss_Url"), r.getInt("Url_Id")));
            }
            return sources;
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new LinkedList<WebSource>();
    }
    
    private static void initLists(WebSource webSources) {
        
        keyWords = new LinkedList<String>();
        fileHashes = new LinkedList<String>();
        webContent = new LinkedList<String>();
        rssContent = new LinkedList<String>();
        
        ResultSet r = retrieve.getTableWhere("KeyWords", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                keyWords.add(r.getString("Key_Word"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Pdfs", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                fileHashes.add(r.getString("Pdf_Hash"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Html", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                webContent.add(r.getString("Html_Content"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Rss", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                rssContent.add(r.getString("Rss_Content"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
