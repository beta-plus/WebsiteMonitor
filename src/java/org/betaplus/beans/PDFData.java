package org.betaplus.beans;

/*
	Title: PDFData
	Author: Ben Taylor
	Date: Apr 30, 2013
	Version: 1.0
*/

public class PDFData 
{
    private String pdfID;
    private String locationDir;
    private String dlDate;
    private String urlID;
    private String url;
    private String type;
    private String pdfName;
    
    // Setter Methods ----------------------------------------------------------
    public void setPdfID(String pdfID)
    { this.pdfID = pdfID; }
    
    public void setLocationDir(String locationDir)
    { this.locationDir = locationDir; }
    
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    public void setUrl(String url)
    { this.url = url; }
    
    public void setType(String type)
    { this.type = type; }
    
    public void setPdfName(String pdfName)
    { this.pdfName = pdfName; }
    
    // Getter Methods ----------------------------------------------------------
    public String getPdfID()
    { return pdfID; }
    
    public String getLocationDir()
    { return locationDir; }
    
    public String getDlDate()
    { return dlDate; }
    
    public String getUrlID()
    { return urlID; }
    
    public String getUrl()
    { return url; }
    
    public String getType()
    { return type; }
    
    public String getPdfName()
    { return pdfName; }
}
