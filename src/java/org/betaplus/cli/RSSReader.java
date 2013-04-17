package org.betaplus.cli;

/*
 Title: RSSReader
 Author: Ben Taylor
 Date: Apr 7, 2013
 Version: 1.0
 */

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import org.betaplus.testcases.SimpleDataSource;

public class RSSReader {

    private int urlID;
    private URL feedSource;
    private SyndFeedInput input;
    private SyndFeed feed;
    private Statement stat;

    public RSSReader() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    public void setSource(String url, int urlID) throws Exception {
        try {
            this.feedSource = new URL(url);
            this.urlID = urlID;
            input = new SyndFeedInput();
            feed = input.build(new XmlReader(feedSource));
        } catch (MalformedURLException ex) {
        }
    }

    public void store() throws Exception {
        for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
            SyndEntry entry = (SyndEntry) i.next();

            String linkTitle = entry.getTitle().replace("'", "");
            String linkDes = entry.getDescription().getValue().replace("'", "");
            Date linkPubDate = entry.getPublishedDate();
            String linkLink = entry.getLink().replace("'", "");

            ResultSet checkFeeds = stat.executeQuery("SELECT LinkPubDate FROM rss WHERE LinkTitle= '" + linkTitle + "'");

            if (checkFeeds.next()) {
                String oldDate = checkFeeds.getString("LinkPubDate");
                String newDate = linkPubDate.toString();

                if (!newDate.equals(oldDate)) {
                    stat.execute("UPDATE rss SET LinkDes='" + linkDes
                            + "',LinkPubDate='" + linkPubDate + "',LinkLink='"
                            + linkLink + "'WHERE LinkTitle='" + linkTitle + "'");
                }
            } else {
                stat.execute("INSERT INTO rss (FeedTitle,FeedDes,LinkTitle,LinkDes,LinkPubDate,LinkLink,UrlId)"
                        + "VALUES('" + feed.getTitle() + "','" + feed.getDescription() + "','"
                        + linkTitle + "','" + linkDes + "','" + linkPubDate + "','"
                        + linkLink + "','" + urlID + "')");
            }
        }
    }
}
