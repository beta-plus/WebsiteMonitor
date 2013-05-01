package org.betaplus.beans;

/*
	Title: RSSData
	Author: Ben Taylor
	Date: May 1, 2013
	Version: 1.0
*/

public class RSSData 
{
    private String rssID;
    private String feedTitle;
    private String feedDes;
    private String linkTitle;
    private String linkDes;
    private String linkPubDate;
    private String linkLink;
    private String dlDate;
    private String urlID;
    private String url;
    private String type;
    
    // Setter Methods ----------------------------------------------------------
    public void setRssID(String rssID)
    { this.rssID = rssID; }
    
    public void setFeedTitle(String feedTitle)
    { this.feedTitle = feedTitle; }
    
    public void setFeedDes(String feedDes)
    { this.feedDes = feedDes; }
    
    public void setLinkTitle(String linkTitle)
    { this.linkTitle = linkTitle; }
    
    public void setLinkDes(String linkDes)
    { this.linkDes = linkDes; }
    
    public void setLinkPubDate(String linkPubDate)
    { this.linkPubDate = linkPubDate; }
    
    public void setLinkLink(String linkLink)
    { this.linkLink = linkLink; }
    
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    public void setUrl(String url)
    { this.url = url; }
    
    public void setType(String type)
    { this.type = type; }
    
    // Getter Methods ----------------------------------------------------------
    public String getRssID()
    { return rssID; }
    
    public String getFeedTitle()
    { return feedTitle; }
    
    public String getFeedDes()
    { return feedDes; }
    
    public String getLinkTitle()
    { return linkTitle; }
    
    public String getLinkDes()
    { return linkDes; }
    
    public String getLinkPubDate()
    { return linkPubDate; }
    
    public String getLinkLink()
    { return linkLink; }
    
    public String getDlDate()
    { return dlDate; }
    
    public String getUrlID()
    { return urlID; }
    
    public String getUrl()
    { return url; }
    
    public String getType()
    { return type; }
}
