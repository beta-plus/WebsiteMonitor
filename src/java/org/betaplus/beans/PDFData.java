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
    private String pdfHash;
    private String dlDate;
    private String pdfUrl;
    private String urlID;
    private String pdfName;
    
    /**
     * Set the pdf ID
     * @param pdfID 
     */
    public void setPdfID(String pdfID)
    { this.pdfID = pdfID; }
    
    /**
     * Set the pdf hash code
     * @param pdfHash 
     */
    public void setPdfHash(String pdfHash)
    { this.pdfHash = pdfHash; }
    
    /**
     * Set the pdf download date
     * @param dlDate 
     */
    public void setDlDate(String dlDate)
    { this.dlDate = dlDate; }
    
    /**
     * Set the pdf url
     * @param pdfUrl 
     */
    public void setPdfUrl(String pdfUrl)
    { this.pdfUrl = pdfUrl; }
    
    /**
     * Set the pdf name
     * @param pdfName 
     */
    public void setPdfName(String pdfName)
    { this.pdfName = pdfName; }
    
    /**
     * Set the urdl ID
     * @param urlID 
     */
    public void setUrlID(String urlID)
    { this.urlID = urlID; }
    
    /**
     * Get the pdf ID
     * @return 
     */
    public String getPdfID()
    { return pdfID; }
    
    /**
     * Get the pdf hash code
     * @return 
     */
    public String getPdfHash()
    { return pdfHash; }
    
    /**
     * Get the pdf download date
     * @return 
     */
    public String getDlDate()
    { return dlDate; }
    
    /**
     * Get the pdf url
     * @return 
     */
    public String getPdfUrl()
    { return pdfUrl; }
    
    /**
     * Get the pdf name
     * @return 
     */
    public String getPdfName()
    { return pdfName; }
    
    /**
     * Get the url ID
     * @return 
     */
    public String getUrlID()
    { return urlID; }
}
