/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.util.LinkedList;
import org.betaplus.testcases.DiffMatchPatch.Diff;
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
public class ComparatorTest {
    
    public ComparatorTest() {
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
     * Test of compareChecksums method, of class Comparator.
     */
    @Test
    public void testCompareChecksums() {
        System.out.println("compareChecksums");
        File pdfA = new File("data/testData/t04-pdf01.pdf");
        File pdfB = new File("data/testData/t04-pdf01.pdf");
        int digestType = Comparator.MD5;
        Comparator instance = new ComparatorImpl();
        boolean expResult = true;
        boolean result = instance.compareChecksums(pdfA, pdfB, digestType);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareChecksums method, of class Comparator.
     */
    @Test
    public void testCompareChecksums2() {
        System.out.println("compareChecksums");
        File pdfA = new File("data/testData/t04-pdf01.pdf");
        File pdfB = new File("data/testData/document_634081501182345000.pdf");
        int digestType = Comparator.MD5;
        Comparator instance = new ComparatorImpl();
        boolean expResult = false;
        boolean result = instance.compareChecksums(pdfA, pdfB, digestType);
        assertEquals(expResult, result);
    }

    /**
     * Test of diffFiles method, of class Comparator.
     */
    @Test
    public void testDiffFiles() {
        System.out.println("diffFiles");
        File fileA = new File("data/testData/t04-pdf01.pdf");;
        File fileB = new File("data/testData/t04-pdf01.pdf");;
        Comparator instance = new ComparatorImpl();
        LinkedList expResult = instance.diffFiles(fileA, fileB);
        LinkedList result = instance.diffFiles(fileA, fileB);
        assertEquals(expResult.get(0), result.get(0));
    }

    /**
     * Test of diffText method, of class Comparator.
     */
    @Test
    public void testDiffText() {
        System.out.println("diffText");
        String oldText = "";
        String newText = "";
        Comparator instance = new ComparatorImpl();
        LinkedList expResult = instance.diffText(oldText, newText);
        LinkedList result = instance.diffText(oldText, newText);
        assertEquals(expResult, result);
    }

    /**
     * Test of percentageChanged method, of class Comparator.
     */
    @Test
    public void testPercentageChanged() {
        System.out.println("percentageChanged");        
        Comparator instance = new ComparatorImpl();
        LinkedList<String> diff = instance.diffText("1234", "12");
        Object expResult = 50.0;
        Object result = instance.percentageChanged(diff)[0];
        assertEquals(expResult, result);
    }
}