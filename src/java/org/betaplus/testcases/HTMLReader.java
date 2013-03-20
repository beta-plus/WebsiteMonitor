package org.betaplus.testcases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLReader {

    private URL url;
    private String siteName;
    private File file;
    private FileWriter fw;
    private BufferedWriter bw;
    private Document doc;

    public HTMLReader(String siteName, String url){
        try {
            this.siteName = siteName;
            this.url = new URL(url);
            
            System.out.println("Success: Request Completed");
            
        } catch (MalformedURLException ex) {
            System.out.println("Error: Request Failed" + ex);
        }
    }
    
    public void readHTML() {
        try {
            doc = Jsoup.parse(url, 100000);

            file = new File(siteName + "Parsed.html");
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(doc.toString());
            bw.close();

            System.out.println("Success: Request Completed");

        } catch (IOException ex) {
            System.out.println("Error: Request Failed" + ex);
        }
    }
    
    public void printText() {
        System.out.println(doc.body().text());
    }

    public void removeMarkup() {
        try {
            file = new File(siteName + "MarkupRemoved.txt");
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            bw.write(doc.body().text());
            bw.close();

            System.out.println("Success: Request Completed");

        } catch (IOException ex) {
            System.out.println("Error: Request Failed" + ex);
        }
    }
}
