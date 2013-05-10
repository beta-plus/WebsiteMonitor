/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.util.ArrayList;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
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
 */
public class WebScraperTest {
    
    public WebScraperTest() {
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
     * Test of getTextFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetTextFromUrl() {
        System.out.println("getTextFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t01-text.xhtml", null, 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        WebScraper instance = new WebScraperImpl();
        String expResult = "Hello there! This is the page for test 01 of the Website Monitor, made by Team Beta+ at Bangor University. The test is quite simply just text on a webpage, lets hope it'll work! :)";
        ArrayList<WebText> result = instance.getTextFromUrl(url, dataSource);
        System.out.println(result.get(0).getPageContent());
        assertEquals(expResult, result.get(0).getPageContent());
    }
    /**
     * Test of getTextFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetTextFromUrl1() {
        System.out.println("getTextFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t02-no_text.xhtml", null, 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        WebScraper instance = new WebScraperImpl();
        String expResult = "";
        ArrayList<WebText> result = instance.getTextFromUrl(url, dataSource);
        System.out.println(result.get(0).getPageContent());
        assertEquals(expResult, result.get(0).getPageContent());
    }    
    /**
     * Test of getTextFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetTextFromUrl2() {
        System.out.println("getTextFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t11-link_to_other_page.xhtml", null, 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        WebScraper instance = new WebScraperImpl();
        String expResult = "Hello there! This is page 01 for test 11 of the Website Monitor, made by Team Beta+ at Bangor University. The test is quite simply just a link to another page, lets hope it'll work! :)";
        ArrayList<WebText> result = instance.getTextFromUrl(url, dataSource);
        System.out.println(result.get(0).getPageContent());
        assertEquals(expResult, result.get(1).getPageContent());
    }
    /**
     * Test of getTextFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetTextFromUrl3() {
        System.out.println("getTextFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t12-multi_links_to_other_pages.xhtml", null, 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        WebScraper instance = new WebScraperImpl();
        String expResult = "Hello there! This is page 05 out of 05 pages, for test 12 of the Website Monitor, made by Team Beta+ at Bangor University. The test is quite simply multiple links to other pages, lets hope it'll work! :)";
        ArrayList<WebText> result = instance.getTextFromUrl(url, dataSource);
        System.out.println(result.get(0).getPageContent());
        assertEquals(expResult, result.get(5).getPageContent());
    }
    /**
     * Test of getTextFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetTextFromUrl4() {
        System.out.println("getTextFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t13-no_link.xhtml#", null, 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        WebScraper instance = new WebScraperImpl();
        String expResult = "Test 13 - Page 01 - Should be nothing!";
        ArrayList<WebText> result = instance.getTextFromUrl(url, dataSource);
        System.out.println(result.get(0).getPageContent());
        assertEquals(expResult, result.get(1).getPageContent());
    }
    /**
     * Test of getTextFromRss method, of class WebScraper.
     */
    @Test
    public void testGetTextFromRss() {
        System.out.println("getTextFromRss");
        WebSource url = new WebSource("", "", "", 0);
        String dataSource = "";
        WebScraper instance = new WebScraperImpl();
        ArrayList expResult = new ArrayList();
        ArrayList result = instance.getTextFromRss(url, dataSource);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPDFsFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetPDFsFromUrl() {
        System.out.println("getPDFsFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t04-pdf_link.xhtml", "", 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        ArrayList<String> keyWords = new ArrayList<String>();
        keyWords.add(".pdf");
        keyWords.add("file_provider");
        WebScraper instance = new WebScraperImpl();        
        ArrayList<Pdf> result = instance.getPDFsFromUrl(url, dataSource, keyWords);
        File expResult = new File("data/t04-pdf01.pdf.pdf");
        assertEquals(expResult, result.get(0).getPdfFile());        
    }

    /**
     * Test of getPDFsFromUrl method, of class WebScraper.
     */
    @Test
    public void testGetPDFsFromUrl2() {
        System.out.println("getPDFsFromUrl");
        WebSource url = new WebSource("TestSource", "http://www.jamesmfinney.com/beta+/testing/t09-pdf_link_with_keyword_file.xhtml", "", 0);
        String dataSource = "http://www.jamesmfinney.com/beta+/testing/";
        ArrayList<String> keyWords = new ArrayList<String>();
        keyWords.add(".pdf");
        keyWords.add("file_provider");
        WebScraper instance = new WebScraperImpl();        
        ArrayList<Pdf> result = instance.getPDFsFromUrl(url, dataSource, keyWords);
        File expResult = new File("data/t09-pdf01.pdf.pdf");
        assertEquals(expResult, result.get(0).getPdfFile());        
    }
}
