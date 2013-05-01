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
    private String text;
    private String dlDate;
    private String urlID;
    private String url;
    private String type;
    
    // Setter Methods ----------------------------------------------------------
    public void setHtmlID(String htmlID)
    { this.htmlID = htmlID; }
    
    public void setText(String text)
    { this.text = text; }
    
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    public void setUrl(String url)
    { this.url = url; }
    
    public void setType(String type)
    { this.type = type; }
    
    // Getter Methods ----------------------------------------------------------
    public String getHtmlID()
    { return htmlID; }
    
    public String getText()
    { return text; }
    
    public String getDlDate()
    { return dlDate; }
    
    public String getUrlID()
    { return urlID; }
    
    public String getUrl()
    { return url; }
    
    public String getType()
    { return type; }
}
