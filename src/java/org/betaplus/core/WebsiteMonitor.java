/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.core;

import java.io.File;
import java.util.LinkedList;
import org.betaplus.datatypes.WebSource;
import org.betaplus.util.Comparator;
import org.betaplus.util.ComparatorImpl;
import org.betaplus.util.WebScraperImpl;

/**
 *
 * @author StephenJohnRussell
 */
public class WebsiteMonitor {

    public static void main(String args[]) {

        LinkedList<String> keyWords = new LinkedList<String>();
        keyWords.add("file_provider");
        keyWords.add("pdf");
        String[] urls = {"http://www.lga.org.mt/lga/content.aspx?id=92272", "http://www.gov.im/gambling/regulatory.xml", "http://regulations.porezna-uprava.hr/"};
        String[] sources = {"http://www.lga.org.mt/lga/content.", "gov.im", "-uprava"};

        LinkedList<WebSource> dataSources = new LinkedList<WebSource>();

        WebScraperImpl scraper = new WebScraperImpl();
        ComparatorImpl compare = new ComparatorImpl();

        for (int i = 0; i < 1; i++) {
            dataSources.add(new WebSource(sources[i], urls[i], null, keyWords));
        }

        for (WebSource ws : dataSources) {
            ws.setPdfDocs(scraper.getPDFsFromUrl(ws.getWebPageURL(), ws.getSource(), keyWords));
            ws.setWebText(scraper.getTextFromUrl(ws.getWebPageURL(), ws.getSource()));
        }

        for (WebSource ws : dataSources) {
            for (File f : scraper.getPDFsFromUrl(ws.getWebPageURL(), ws.getSource(), keyWords)) {
                boolean inList = false;
                for (File g : ws.getPdfDocs()) {
                    if (compare.compareChecksums(f, g, Comparator.MD5)) {
                        inList = true;
                    }
                }
                if (!inList) {
                    ws.getPdfDocs().add(f);
                    System.out.println("File NOT found in list. ADDED!");
                } else {
                    System.out.println("File found in list. NOT ADDED!");
                }
            }
            String newText = "";
            for (String s : scraper.getTextFromUrl(ws.getWebPageURL(), ws.getSource())) {
                newText = newText + s + "\n";
            }
            String oldText = "";
            for (String s : ws.getWebText()) {
                oldText = oldText + s + "\n";
            }
            int count = 0;
            for (String u : compare.diffText(oldText, newText)) {
                count++;
                if (u.contains("Diff(INSERT,")) {
                    ws.getWebText().add(u);
                    System.out.println("Text NOT found in list. ADDED!");
                }
            }
            if (count == 0) {
                System.out.println("Text found in list. NOT ADDED!");
            }
        }
        
        for (WebSource w : dataSources) {
            System.out.println("*********************************************");
            System.out.println("Source: " + w.getSource() 
                    + "\nURL: " + w.getWebPageURL());
            System.out.println("*********************************************");
            for (File f : w.getPdfDocs()) {
                System.out.println("");
                System.out.println(f.getPath());
            }
            System.out.println("*********************************************");
            for (String s : w.getWebText()) {
                System.out.println(s);
            }
            System.out.println("*********************************************");
        }
    }
    
}
