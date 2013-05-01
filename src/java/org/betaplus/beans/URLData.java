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
    private String url;
    private String type;
    
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    public void setUrl(String url)
    { this.url = url; }
    
    public void setType(String type)
    { this.type = type; }
    
    public String getUrlID()
    { return urlID; }
    
    public String getUrl()
    { return url; }
    
    public String getType()
    { return type; }
}
