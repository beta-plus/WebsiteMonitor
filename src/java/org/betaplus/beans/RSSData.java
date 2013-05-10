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
    
    /**
     * Set the rss ID
     * @param rssID 
     */
    public void setRssID(String rssID)
    { this.rssID = rssID; }
    
    /**
     * Set the rss feed title
     * @param feedTitle 
     */
    public void setFeedTitle(String feedTitle)
    { this.feedTitle = feedTitle; }
    
    /**
     * Set the rss feed description
     * @param feedDes 
     */
    public void setFeedDes(String feedDes)
    { this.feedDes = feedDes; }
    
    /**
     * Set the rss link title
     * @param linkTitle 
     */
    public void setLinkTitle(String linkTitle)
    { this.linkTitle = linkTitle; }
    
    /**
     * Set the rss link description
     * @param linkDes 
     */
    public void setLinkDes(String linkDes)
    { this.linkDes = linkDes; }
    
    /**
     * Set the rss link published date
     * @param linkPubDate 
     */
    public void setLinkPubDate(String linkPubDate)
    { this.linkPubDate = linkPubDate; }
    
    /**
     * Set the link of the rss link
     * @param linkLink 
     */
    public void setLinkLink(String linkLink)
    { this.linkLink = linkLink; }
    
    /**
     * Set the rss download date
     * @param dlDate 
     */
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    /**
     * Set the url ID
     * @param urlID 
     */
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    /**
     * Set the url
     * @param url 
     */
    public void setUrl(String url)
    { this.url = url; }
    
    /**
     * Get the rss ID
     * @return 
     */
    public String getRssID()
    { return rssID; }
    
    /**
     * Get the rss feed title
     * @return 
     */
    public String getFeedTitle()
    { return feedTitle; }
    
    /**
     * Get the rss feed description
     * @return 
     */
    public String getFeedDes()
    { return feedDes; }
    
    /**
     * Get the rss link title
     * @return 
     */
    public String getLinkTitle()
    { return linkTitle; }
    
    /**
     * Get the rss link description
     * @return 
     */
    public String getLinkDes()
    { return linkDes; }
    
    /**
     * Get the rss link publish date
     * @return 
     */
    public String getLinkPubDate()
    { return linkPubDate; }
    
    /**
     * Get the link of the rss link
     * @return 
     */
    public String getLinkLink()
    { return linkLink; }
    
    /**
     * Get the rss download date
     * @return 
     */
    public String getDlDate()
    { return dlDate; }
    
    /**
     * Get the url ID
     * @return 
     */
    public String getUrlID()
    { return urlID; }
    
    /**
     * Get the url
     * @return 
     */
    public String getUrl()
    { return url; }
}
