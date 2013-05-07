/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

import java.util.LinkedList;

/**
 *
 * @author StephenJohnRussell
 */
public class WebText {
    
    private LinkedList<String> pageContent;
    private String pageTitle;
    private WebSource we;

    public WebText(LinkedList<String> pageContent, String pageTitle, WebSource we) {
        this.pageContent = pageContent;
        this.pageTitle = pageTitle;
        this.we = we;
    }

    public LinkedList<String> getPageContent() {
        return pageContent;
    }

    public void setPageContent(LinkedList<String> pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public WebSource getWe() {
        return we;
    }

    public void setWe(WebSource we) {
        this.we = we;
    }
}
