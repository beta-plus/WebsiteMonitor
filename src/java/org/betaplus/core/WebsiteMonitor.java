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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.betaplus.datatypes.Notification;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.User;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;
import org.betaplus.util.ComparatorImpl;
import org.betaplus.util.DataDumperImpl;
import org.betaplus.util.DataRetrieverImpl;
import org.betaplus.util.NotifierImpl;
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
    private static NotifierImpl notify;
    private static ArrayList<String> keyWords;
    private static ArrayList<String> fileHashes;
    private static ArrayList<String> webContent;
    private static ArrayList<String> rssContent;
    private static LinkedList<User> users;

    public WebsiteMonitor() {
        scraper = new WebScraperImpl();
        compare = new ComparatorImpl();
        retrieve = new DataRetrieverImpl();
        dumper = new DataDumperImpl();
        notify = new NotifierImpl();
    }

    public static void main(String args[]) {

        WebsiteMonitor websMon = new WebsiteMonitor();
        LinkedList<WebSource> dataSources = websMon.getDataSources();
        for (WebSource ws : dataSources) {
            initLists(ws);
            System.out.println("Scanning : " + ws.getSource());
            System.out.println("RSS");
            ArrayList<RssFeed> textFromRss = scraper.getTextFromRss(ws, ws.getSource());
            for (RssFeed se : textFromRss) {
                boolean gotIt = false;
                for (String s : rssContent) {
                    if (s.equalsIgnoreCase(se.getLinkDes())) {
                        gotIt = true;
                    }
                }
                if (!gotIt || rssContent.isEmpty()) {
                    System.out.println("Dumping *+=-");
                    rssContent.add(se.getLinkDes());
                    dumper.dumpRSS(se);
                    notify.addNotification(new Notification(se.getLinkTitle(), se.getLinkLink()));
                }

            }

            System.out.println("WebText");
            ArrayList<WebText> textFromUrl = scraper.getTextFromUrl(ws, ws.getSource());
            for (WebText newWebCont : textFromUrl) {
                Collections.sort(webContent);
                int indexOf = Collections.binarySearch(webContent, newWebCont.getPageContent());
                if (indexOf == -1 || webContent.isEmpty()) {
                    System.out.println("Dumping *+=-");
                    webContent.add(newWebCont.getPageContent());
                    dumper.dumpWebText(newWebCont);
                    notify.addNotification(new Notification(newWebCont.getPageTitle(), newWebCont.getPageUrl()));
                }
            }

            System.out.println("PDFs");
            ArrayList<Pdf> pdFsFromUrl = scraper.getPDFsFromUrl(ws, ws.getSource(), keyWords);
            for (Pdf pdf : pdFsFromUrl) {
                boolean gotIt = false;
                String newHash = WebsiteMonitor.getHash(pdf.getPdfFile());
                Collections.sort(fileHashes);
                int indexOf = Collections.binarySearch(fileHashes, newHash);
                if (indexOf == -1 || fileHashes.isEmpty()) {
                    System.out.println("Dumping *+=-");
                    dumper.dumpPDF(pdf);
                    notify.addNotification(new Notification(pdf.getPdfName(), pdf.getPdfUrl()));
                }
            }
            notify.sendNotification(users);
            notify.clearNotifications();
        }
        
        File file = new File("data");        
        String[] myFiles;      
            if(file.isDirectory()){  
                myFiles = file.list();  
                for (int i=0; i<myFiles.length; i++) {  
                    File myFile = new File(file, myFiles[i]);   
                    if (myFile.getName().endsWith(".pdf")) {
                        myFile.delete();  
                    }                    
                }  
             }  
    }

    private static String getHash(File f) {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return DigestUtils.md5Hex(is);


        } catch (FileNotFoundException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);

            return "";
        } catch (IOException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);

            return "";
        } finally {
            try {
                is.close();


            } catch (IOException ex) {
                Logger.getLogger(WebsiteMonitor.class
                        .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return new LinkedList<WebSource>();
    }

    private static void initLists(WebSource webSources) {

        keyWords = new ArrayList<String>();
        fileHashes = new ArrayList<String>();
        webContent = new ArrayList<String>();
        rssContent = new ArrayList<String>();
        users = new LinkedList<User>();

        ResultSet r = retrieve.getTableWhere("KeyWords", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                keyWords.add(r.getString("Key_Word"));


            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Pdfs", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                fileHashes.add(r.getString("Pdf_Hash"));


            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Html", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                webContent.add(r.getString("Html_Content"));


            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        r = retrieve.getTableWhere("Rss", "Url_Id", "" + webSources.getSourceID());
        try {
            while (r.next()) {
                rssContent.add(r.getString("Rss_Content"));


            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet table = retrieve.getTable("Users");
        try {
            while (table.next()) {
                users.add(new User(table.getString("User_Name"), table.getString("User_Email")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WebsiteMonitor.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
