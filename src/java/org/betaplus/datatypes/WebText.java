/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

/**
 *
 * @author StephenJohnRussell
 */
public class WebText {
    
    private String pageContent;
    private String pageTitle;
    private String pageUrl;
    private WebSource we;

    public WebText(String pageContent, String pageTitle, String pageUrl, WebSource we) {
        this.pageContent = pageContent;
        this.pageTitle = pageTitle;
        this.pageUrl = pageUrl;
        this.we = we;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public WebSource getWe() {
        return we;
    }

    public void setWe(WebSource we) {
        this.we = we;
    }
}
