package org.betaplus.cli;

/*
 Title: HTMLReader
 Author: Ben Taylor
 Date: Apr 6, 2013
 Version: 1.0
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.betaplus.testcases.SimpleDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLReader {

    private Statement stat;
    private URL url;
    private File file1, file2;
    private FileWriter fw;
    private BufferedWriter bw;
    private Document doc;
    private String pageText;
    private int urlID;

    public HTMLReader() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    public void setSource(String url, int urlID) {
        try {
            this.url = new URL(url);
            this.urlID = urlID;
        } catch (MalformedURLException ex) {
        }
    }

    public void readHTML() {
        try {
            doc = Jsoup.parse(url, 100000);
        } catch (IOException ex) {
        }
    }

    public void extractPDFLinks() throws Exception {
        Elements els = doc.body().select("a[href]");
        for (Element e : els) {
            String absUrl = e.absUrl("href");
            if (absUrl.contains("file") || absUrl.contains(".pdf")) {
                storePDF(absUrl);
            }
        }
    }

    public void removeMarkup() {
        try {
            pageText = doc.body().text();
            pageText = pageText.replaceAll("'", "");

            file1 = new File("NewEntryText.txt");
            fw = new FileWriter(file1.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(pageText);
            bw.close();
        } catch (IOException ex) {
        }
    }

    public void store() throws Exception {
        ResultSet latestEntry = stat.executeQuery("SELECT * FROM html WHERE UrlId= '" + urlID + "'"
                + "AND DLDate= (select max(DLDate) FROM html WHERE UrlID= '" + urlID + "')");

        if (latestEntry.next()) {
            String text = latestEntry.getString("text");

            file2 = new File("LatestEntryText.txt");
            fw = new FileWriter(file2.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();

            boolean equal = Comparitor.compareChecksums(file1, file2, 0);

            if (!equal) {
                stat.execute("INSERT INTO html (text,UrlId)"
                        + "VALUES('" + pageText + "','" + urlID + "')");
            }
        } else {
            stat.execute("INSERT INTO html (text,UrlId)"
                    + "VALUES('" + pageText + "','" + urlID + "')");
        }
    }

    private void storePDF(String pdfLink) throws Exception {
        ResultSet checkUrl = stat.executeQuery("SELECT UrlId,Url FROM urls WHERE url= '" + pdfLink + "'");

        if (!checkUrl.next()) {
            stat.execute("INSERT INTO urls (Url, Type) VALUES('" + pdfLink + "','pdf')");
        }
    }
}
