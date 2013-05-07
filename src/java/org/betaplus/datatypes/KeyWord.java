/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

/**
 *
 * @author StephenJohnRussell
 */
public class KeyWord {
    
    private String keyWord;
    private WebSource ws;

    public KeyWord(String keyWord, WebSource ws) {
        this.keyWord = keyWord;
        this.ws = ws;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public WebSource getWs() {
        return ws;
    }

    public void setWs(WebSource ws) {
        this.ws = ws;
    }
}
