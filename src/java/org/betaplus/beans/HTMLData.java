package org.betaplus.beans;

/*
	Title: HTMLData
	Author: Ben Taylor
	Date: May 1, 2013
	Version: 1.0
*/

public class HTMLData 
{
    private String htmlID;
    private String content;
    private String dlDate;
    private String htmlName;
    private String htmlUrl;
    private String urlID;
    
    /**
     * Set the html ID
     * @param htmlID 
     */
    public void setHtmlID(String htmlID)
    { this.htmlID = htmlID; }
    
    /**
     * Set the html content
     * @param content 
     */
    public void setContent(String content)
    { this.content = content; }
    
    /**
     * Set the html download date
     * @param dlDate 
     */
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    /**
     * Set the html name
     * @param htmlName 
     */
    public void setHtmlName(String htmlName)
    { this.htmlName = htmlName; }
    
    /**
     * Set the html url
     * @param htmlUrl 
     */
    public void setHtmlUrl(String htmlUrl)
    { this.htmlUrl = htmlUrl; }
    
    /**
     * Set the url ID
     * @param urlID 
     */
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    /**
     * Get the html ID
     * @return htmlID
     */
    public String getHtmlID()
    { return htmlID; }
    
    /**
     * Get the html content
     * @return content
     */
    public String getContent()
    { return content; }
    
    /**
     * Get the html download date
     * @return dlDate
     */
    public String getDlDate()
    { return dlDate; }
    
    /**
     * Get the html name
     * @return htmlName
     */
    public String getHtmlName()
    { return htmlName; }
    
    /**
     * Get the html url
     * @return htmlUrl
     */
    public String getHtmlUrl()
    { return htmlUrl; }
    
    /**
     * Get the url ID
     * @return urlID
     */
    public String getUrlID()
    { return urlID; }
}
