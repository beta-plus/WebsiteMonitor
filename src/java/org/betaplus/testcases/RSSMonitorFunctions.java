package org.betaplus.testcases;

/*
 * Author: James Finney
 * Title: RSS Monitor Functions
 * Created: 01/02/2013
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import org.betaplus.util.SimpleDataSource;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;

public class RSSMonitorFunctions
{
    final String url;
    final URL feedSource;
    final SyndFeedInput input;
    final SyndFeed feed;
    final Statement stat;
    
    public RSSMonitorFunctions(String inputUrl) throws Exception
    {
        url = inputUrl;
        feedSource = new URL(url);
        input = new SyndFeedInput();
        feed = input.build(new XmlReader(feedSource));
        
        SimpleDataSource.init("data/database.properties");
        
        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();
        
        stat = conn.createStatement();
    }
    
    public void displayFeed()
    {
        System.out.println("Title = " + feed.getTitle());
        System.out.println("Description = " + feed.getDescription() + "\n");
        
        for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
        {
            SyndEntry entry = (SyndEntry) i.next();
            System.out.println(entry.getTitle());
            System.out.println(entry.getDescription().getValue());
            System.out.println(entry.getPublishedDate());
            System.out.println(entry.getLink() + "\n");         
        }
    }
    
    public void insertDatabase() throws SQLException
    {
        ResultSet checkUrl = stat.executeQuery("SELECT UrlId,Url FROM urls WHERE url= '" + url + "'");
        
        if (checkUrl.next())
        {
            int changesMade = 0;
            int newInserts = 0;
            System.out.println("Already inserted once! hahaha.");
            
            ResultSet getUrlId = stat.executeQuery("SELECT UrlId FROM urls WHERE url= '" + url + "'");
            getUrlId.next();
            String urlId = getUrlId.getString("UrlId");
            System.out.println("UrlId = " + urlId);
            
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
            {
                SyndEntry entry = (SyndEntry) i.next();    

                String linkTitle = entry.getTitle().replace("'", "");
                String linkDes = entry.getDescription().getValue().replace("'", "");
                Date linkPubDate = entry.getPublishedDate();
                String linkLink = entry.getLink().replace("'", "");
                
                ResultSet checkFeeds = stat.executeQuery("SELECT LinkPubDate FROM rss WHERE LinkTitle= '" + linkTitle + "'");
                if (checkFeeds.next())
                {
                    String oldDate = checkFeeds.getString("LinkPubDate");
                    String newDate = linkPubDate.toString();
                    
                    System.out.println("Link Title = " + linkTitle);
                    System.out.println("Old Date = " + oldDate);
                    System.out.println("New Date = " + newDate);
                    
                    if (!newDate.equals(oldDate))
                    {
                        stat.execute("UPDATE rss SET LinkDes='" + linkDes
                                + "',LinkPubDate='" + linkPubDate + "',LinkLink='"
                                + linkLink + "'WHERE LinkTitle='" + linkTitle + "'");
                        changesMade += 1;
                        System.out.println("New!\n");
                    } else
                    {
                        System.out.println("Not new!");
                    }
                } else
                {
                    stat.execute("INSERT INTO rss (FeedTitle,FeedDes,LinkTitle,LinkDes,LinkPubDate,LinkLink,UrlId)"
                        + "VALUES('" + feed.getTitle() + "','" + feed.getDescription() + "','"
                        + linkTitle + "','" + linkDes + "','" + linkPubDate + "','"
                        + linkLink+ "','" + urlId + "')");
                    newInserts += 1;
                }
            }
            
            System.out.println("Changes made = " + changesMade);
            System.out.println("New inserts made = " + newInserts);
        } else
        {
            stat.execute("INSERT INTO urls (Url, Type) VALUES('" + url + "','rss')");
            
            ResultSet getUrlId = stat.executeQuery("SELECT UrlId FROM urls WHERE url= '" + url + "'");
            getUrlId.next();
            String urlId = getUrlId.getString("UrlId");
            System.out.println("UrlId = " + urlId);
            
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();)
            {
                SyndEntry entry = (SyndEntry) i.next();    

                String linkTitle = entry.getTitle().replace("'", "");
                String linkDes = entry.getDescription().getValue().replace("'", "");
                Date linkPubDate = entry.getPublishedDate();
                String linkLink = entry.getLink().replace("'", "");

                stat.execute("INSERT INTO rss (FeedTitle,FeedDes,LinkTitle,LinkDes,LinkPubDate,LinkLink,UrlId)"
                        + "VALUES('" + feed.getTitle() + "','" + feed.getDescription() + "','"
                        + linkTitle + "','" + linkDes + "','" + linkPubDate + "','"
                        + linkLink+ "','" + urlId + "')");
            }
        }
    }
}