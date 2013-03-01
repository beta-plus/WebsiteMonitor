package org.betaplus.rsstestcase.pkg01;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

public class RSSMonitor
{
    public static void main(String[] args) throws Exception
    {
        URL feedSource = new URL("http://feeds.bbci.co.uk/news/rss.xml");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        
        System.out.println("Title = " + feed.getTitle());
        System.out.println("Description = " + feed.getDescription() + "\n");
        int rssID = 0;
        
        SimpleDataSource.init("data/database.properties");
                               
        
        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();
        
        Statement stat = conn.createStatement();
                
        for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
        {
            SyndEntry entry = (SyndEntry) i.next();
            System.out.println(entry.getTitle());
            System.out.println(entry.getPublishedDate());
            System.out.println(entry.getLink() + "\n");
            
            String title = entry.getTitle().replace("'", "");
            try {
                stat.execute("INSERT INTO RSS (rssid,title,linktitle,linkpubdate,linklink) VALUES('" + rssID++ + "','" + feed.getTitle() + "','" + title + "','" + entry.getPublishedDate() + "','" + entry.getLink() + "')");
            }
            catch (MySQLIntegrityConstraintViolationException e) {
                System.out.println("Already stored!!");
            }
        }
    }
}