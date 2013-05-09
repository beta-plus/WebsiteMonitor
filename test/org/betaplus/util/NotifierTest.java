/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.util.LinkedList;
import org.betaplus.datatypes.Notification;
import org.betaplus.datatypes.User;
import org.betaplus.datatypes.WebSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author StephenJohnRussell
 */
public class NotifierTest {
    
    public NotifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sendNotification method, of class Notifier.
     */
    @Test
    public void testSendNotification() {
        System.out.println("sendNotification");
        LinkedList<User> to = new LinkedList<User>();
        to.add(new User("Stephen Russell", "stephenjrussell@gmail.com"));
        Notifier instance = new NotifierImpl();
        instance.addNotification(new Notification("TEST | TEST , testing", "TestSource001"));
        boolean expResult = true;
        boolean result = instance.sendNotification(to);
        assertEquals(expResult, result);
    }

    /**
     * Test of addNotification method, of class Notifier.
     */
    @Test
    public void testAddNotification() {
        System.out.println("addNotification");
        Notification note = new Notification("TEST | TEST , testing", "TestSource001");
        Notifier instance = new NotifierImpl();
        boolean expResult = true;
        boolean result = instance.addNotification(note);
        assertEquals(expResult, result);
    }

    /**
     * Test of clearNotifications method, of class Notifier.
     */
    @Test
    public void testClearNotifications() {
        System.out.println("clearNotifications");
        Notifier instance = new NotifierImpl();
        boolean expResult = true;
        boolean result = instance.clearNotifications();
        assertEquals(expResult, result);
    }
}
