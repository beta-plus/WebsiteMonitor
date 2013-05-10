package org.betaplus.cli;

/*
 Title: WebScanner
 Author: Ben Taylor
 Date: Apr 5, 2013
 Version: 1.0
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.betaplus.util.SimpleDataSource;

public class WebScanner {

    private Statement stat;
    private HTMLReader htmlReader;
    private RSSReader rssReader;
    private PDFReader pdfReader;

    public WebScanner() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();

        htmlReader = new HTMLReader();
        rssReader = new RSSReader();
        pdfReader = new PDFReader();
    }

    public void scan() throws Exception {
        System.out.println("\tBeginning Scan...");

        System.out.println("\tScanning HTML...");
        scanHTML();
        System.out.println("\tHTML Scan SuccessFull...");

        System.out.println("\tScanning RSS...");
        scanRSS();
        System.out.println("\tRSS Scan SuccessFull...");

        System.out.println("\tScanning PDF...");
        scanPDF();
        System.out.println("\tPDF Scan SuccessFull...");

        System.out.println("\tEnding Scan...");
    }

    private void scanHTML() throws Exception {
        ResultSet htmlSet = getURL("html");

        while (htmlSet.next()) {
            String url = htmlSet.getString("Url");
            int urlID = htmlSet.getInt("UrlId");

            htmlReader.setSource(url, urlID);
            htmlReader.readHTML();
            htmlReader.extractPDFLinks();
            htmlReader.removeMarkup();
            htmlReader.store();
        }
    }

    private void scanRSS() throws Exception {
        ResultSet rssSet = getURL("rss");

        while (rssSet.next()) {
            String url = rssSet.getString("Url");
            int urlID = rssSet.getInt("UrlId");

            rssReader.setSource(url, urlID);
            rssReader.store();
        }
    }

    private void scanPDF() throws Exception {
        ResultSet pdfSet = getURL("pdf");

        while (pdfSet.next()) {
            String url = pdfSet.getString("Url");
            int urlID = pdfSet.getInt("UrlId");
            System.out.printf("%-100s%s\n", url, urlID);
            pdfReader.setSource(url, urlID);
            pdfReader.store();
        }
    }

    private ResultSet getURL(String type) throws Exception {
        return stat.executeQuery("SELECT * FROM urls WHERE Type= '" + type + "'");
    }
}
