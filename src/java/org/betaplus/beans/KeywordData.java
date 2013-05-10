package org.betaplus.beans;

/*
	Title: KeywordData
	Author: Ben Taylor
	Date: May 9, 2013
	Version: 1.0
*/

public class KeywordData 
{
    private String keywordID;
    private String keyword;
    private String urlID;

    /**
     * Get the keyword ID
     * @return 
     */
    public String getKeywordID() {
        return keywordID;
    }

    /**
     * Set the keyword ID
     * @param keywordID 
     */
    public void setKeywordID(String keywordID) {
        this.keywordID = keywordID;
    }

    /**
     * Get the keyword
     * @return 
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Set the keyword
     * @param keyword 
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Get the url ID
     * @return 
     */
    public String getUrlID() {
        return urlID;
    }

    /**
     * Set the url ID
     * @param urlID 
     */
    public void setUrlID(String urlID) {
        this.urlID = urlID;
    }
}
