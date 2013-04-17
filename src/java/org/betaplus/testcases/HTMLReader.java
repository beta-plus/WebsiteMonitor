package org.betaplus.testcases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
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
    private Comparitor c = new Comparitor();

    public HTMLReader() throws Exception
    {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
    
    public boolean setSource(String url){
        try {
            this.url = new URL(url);
            System.out.println("Success: Request Completed");
            return true;
            
        } catch (MalformedURLException ex) {
            System.out.println("Error: Request Failed" + ex);
            return false;
        }
    }
    
    public void readHTML() {
        try {
            doc = Jsoup.parse(url, 100000);

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
            pageText = doc.body().text();
            pageText = pageText.replaceAll("'", "");

            file1 = new File("NewEntryText.txt");
            fw = new FileWriter(file1.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            bw.write(pageText);
            bw.close();

            System.out.println("Success: Request Completed");

        } catch (IOException ex) {
            System.out.println("Error: Request Failed" + ex);
        }
    }
    
    public void insertDatabase() throws SQLException, IOException
    {
        ResultSet checkUrl = stat.executeQuery("SELECT UrlId,Url FROM urls WHERE url= '" + url + "'");
        
        if (checkUrl.next())
        {
            System.out.println("URL Already Exists...");
            
            ResultSet getUrlId = stat.executeQuery("SELECT UrlId FROM urls WHERE url= '" + url + "'");
            getUrlId.next();
            String urlId = getUrlId.getString("UrlId");
            System.out.println("UrlId = " + urlId);
            
            ResultSet latestEntry = stat.executeQuery("SELECT * FROM html WHERE UrlId= '" + urlId + "'"
                    + "AND DLDate= (select max(DLDate) FROM html WHERE UrlID= '" + urlId + "')");
            latestEntry.next();
            System.out.println("HTML ID = " + latestEntry.getString("HtmlId"));
            String text = latestEntry.getString("text");
            
            file2 = new File("LatestEntryText.txt");
            fw = new FileWriter(file2.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            bw.write(text);
            bw.close();
            
            boolean equal = c.compareChecksums(file1, file2, 0);
            System.out.println("Entries Equal:" + equal);
            
            if(!equal)
            {
                System.out.println("Changes have been made, new enty inserted into html table...");
                stat.execute("INSERT INTO html (text,UrlId)"
                        + "VALUES('" + pageText +"','" + urlId + "')");
            }
            else
            {
                System.out.println("No changes have been made since last scan...");
            }
        }
        else
        {
            System.out.println("New Entry...");
            
            stat.execute("INSERT INTO urls (Url, Type) VALUES('" + url + "','html')");
            
            ResultSet getUrlId = stat.executeQuery("SELECT UrlId FROM urls WHERE url= '" + url + "'");
            getUrlId.next();
            String urlId = getUrlId.getString("UrlId");
            System.out.println("UrlId = " + urlId);
            
            stat.execute("INSERT INTO html (text,UrlId)"
                        + "VALUES('" + pageText +"','" + urlId + "')");
        }
    }
}
