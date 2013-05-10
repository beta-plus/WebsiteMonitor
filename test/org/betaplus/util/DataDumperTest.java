/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import org.betaplus.datatypes.KeyWord;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.User;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author StephenJohnRussell
 * 
 */
public class DataDumperTest {
    
    public DataDumperTest() {
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
     * Test of dumpPDF method, of class DataDumper.
     */
    @Test
    public void testDumpPDF() {
        System.out.println("dumpPDF");
        Pdf pdf = new Pdf("", "", "", new File("data/testData/t04-pdf01.pdf"), new WebSource("http://www.lga.org.mt/lga/content", "", "", 2));
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpPDF(pdf);
        assertEquals(expResult, result);
    }

    /**
     * Test of dumpRSS method, of class DataDumper.
     */
    @Test   
    public void testDumpRSS() {
        System.out.println("dumpRSS");
        RssFeed rss = new RssFeed("", "", "", "", "", "", new WebSource("http://www.lga.org.mt/lga/content", "", "", 2));
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpRSS(rss);
        assertEquals(expResult, result);
    }

    /**
     * Test of dumpWebText method, of class DataDumper.
     */
    @Test
    public void testDumpWebText() {
        System.out.println("dumpWebText");
        WebText webText = new WebText("", "", "", new WebSource("http://www.lga.org.mt/lga/content", "", "", 2));
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpWebText(webText);
        assertEquals(expResult, result);
    }

    /**
     * Test of dumpUser method, of class DataDumper.
     */
    @Test
    public void testDumpUser() {
        System.out.println("dumpUser");
        User user = new User("", "");
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpUser(user);
        assertEquals(expResult, result);
    }

    /**
     * Test of dumpKeyWord method, of class DataDumper.
     */
    @Test
    public void testDumpKeyWord() {
        System.out.println("dumpKeyWord");
        KeyWord keyWord = new KeyWord("", new WebSource("http://www.lga.org.mt/lga/content", "", "", 2));
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpKeyWord(keyWord);
        assertEquals(expResult, result);
    }

    /**
     * Test of dumpWebSource method, of class DataDumper.
     */
    @Test
    public void testDumpWebSource() {
        System.out.println("dumpWebSource");
        WebSource ws = new WebSource("http://www.lga.org.mt/lga/content", "", "", 2);
        DataDumper instance = new DataDumperImpl();
        boolean expResult = true;
        boolean result = instance.dumpWebSource(ws);
        assertEquals(expResult, result);
    }
}
