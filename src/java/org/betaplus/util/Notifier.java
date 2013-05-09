/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.util.LinkedList;
import org.betaplus.datatypes.Notification;
import org.betaplus.datatypes.User;

/**
 * Interface defining notification generation.
 * 
 * @author StephenJohnRussell
 */
public interface Notifier {
    
    /**
     * Send an update.
     * 
     * @param to
     * @return success 
     */
    public boolean sendNotification(LinkedList<User> to);
    
    /**
     * Add an update to the list.
     * 
     * @param note
     * @return success
     */
    public boolean addNotification(Notification note);
    
    /**
     * Reset list of notifications.
     * 
     * @param note
     * @return success
     */
    public boolean clearNotifications();
}
