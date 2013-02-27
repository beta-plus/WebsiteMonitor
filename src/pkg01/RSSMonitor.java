package pkg01;

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
        
        SimpleDataSource.init("/Users/Jay/Documents/Documents/University Work/Year 02/Year 02 - Semester 02/NetBeans Projects/Software Hut/RSS Monitor/src/pkg01/database.properties");
        
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
            
            stat.execute("INSERT INTO RSS (title,linktitle,linkpubdate,linklink) VALUES('" + feed.getTitle() + "','" + title + "','" + entry.getPublishedDate() + "','" + entry.getLink() + "')");
        }
    }
}