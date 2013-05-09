/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

/**
 *
 * @author StephenJohnRussell
 */
public class Notification {
    
    private String notifyContent;
    private String ws;

    public Notification(String notifyContent, String ws) {
        this.notifyContent = notifyContent;
        this.ws = ws;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }
    
    @Override
    public String toString() {
        return ws + " | " + notifyContent;
    }
}
