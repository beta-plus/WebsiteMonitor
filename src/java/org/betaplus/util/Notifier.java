/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

/**
 * Interface defining notification generation.
 * 
 * @author StephenJohnRussell
 */
public interface Notifier {
    
    /**
     * RSS data identifier for messageConstructor.
     */
    static final int RSS_NOTIFICATION = 0;
    
    /**
     * PDF data identifier for messageConstructor.
     */
    static final int PDF_NOTIFICATION = 1;
    
    /**
     * Web page data identifier for messageConstructor.
     */
    static final int WEB_NOTIFICATION = 2;
    
    /**
     * Send an update to a given users email.
     * 
     * @param message
     * @param email
     * @return 
     */
    public boolean sendNotification(String message, String email);
    
    /**
     * Construct a notification message.
     * 
     * @param percentChanged
     * @param data
     * @param notificationType
     * @return 
     */
    public String messageConstructor(double[] percentChanged, String data, int notificationType);
    
}
