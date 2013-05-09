/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

/**
 *
 * @author StephenJohnRussell
 */
public class RssFeed {

    private String feedTitle;
    private String feedDes;
    private String linkTitle;
    private String linkDes;
    private String linkPubDate;
    private String linkLink;
    private WebSource ws;

    public RssFeed(String feedTitle, String feedDes, String linkTitle, String linkDes, String linkPubDate, String linkLink, WebSource ws) {
        this.feedTitle = feedTitle;
        this.feedDes = feedDes;
        this.linkTitle = linkTitle;
        this.linkDes = linkDes;
        this.linkPubDate = linkPubDate;
        this.linkLink = linkLink;
        this.ws = ws;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedDes() {
        return feedDes;
    }

    public void setFeedDes(String feedDes) {
        this.feedDes = feedDes;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkDes() {
        return linkDes;
    }

    public void setLinkDes(String linkDes) {
        this.linkDes = linkDes;
    }

    public String getLinkPubDate() {
        return linkPubDate;
    }

    public void setLinkPubDate(String linkPubDate) {
        this.linkPubDate = linkPubDate;
    }

    public String getLinkLink() {
        return linkLink;
    }

    public void setLinkLink(String linkLink) {
        this.linkLink = linkLink;
    }

    public WebSource getWs() {
        return ws;
    }

    public void setWs(WebSource ws) {
        this.ws = ws;
    }
    
}    