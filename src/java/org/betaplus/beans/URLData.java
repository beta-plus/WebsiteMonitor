package org.betaplus.beans;

/*
	Title: URLData
	Author: Ben Taylor
	Date: May 1, 2013
	Version: 1.0
*/

public class URLData 
{
    private String urlID;
    private String rssUrl;
    private String httpUrl;
    private String urlName;
    
    /**
     * Set the url ID
     * @param urlID 
     */
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    /**
     * Set the rss url
     * @param rssUrl 
     */
    public void setRssUrl(String rssUrl)
    { this.rssUrl = rssUrl; }
    
    /**
     * Set the http url
     * @param httpUrl 
     */
    public void setHttpUrl(String httpUrl)
    { this.httpUrl = httpUrl; }
    
    /**
     * Set the url name
     * @param urlName 
     */
    public void setUrlName(String urlName)
    { this.urlName = urlName; }
    
    /**
     * Get the url ID
     * @return 
     */
    public String getUrlID()
    { return urlID; }
    
    /**
     * Get the rss url
     * @return 
     */
    public String getRssUrl()
    { return rssUrl; }
    
    /**
     * Get the http url
     * @return 
     */
    public String getHttpUrl()
    { return httpUrl; }
    
    /**
     * Get the url name
     * @return 
     */
    public String getUrlName()
    { return urlName; }
}
