/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.sql.ResultSet;
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
public class DataRetrieverTest {
    
    public DataRetrieverTest() {
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
     * Test of getTable method, of class DataRetriever.
     */
    @Test
    public void testGetTable() {
        System.out.println("getTable");
        String tableName = "";
        DataRetriever instance = new DataRetrieverImpl();
        ResultSet expResult = null;
        ResultSet result = instance.getTable(tableName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLatestTable method, of class DataRetriever.
     */
    @Test
    public void testGetLatestTable() {
        System.out.println("getLatestTable");
        String tableName = "";
        DataRetriever instance = new DataRetrieverImpl();
        ResultSet expResult = null;
        ResultSet result = instance.getLatestTable(tableName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTableWhere method, of class DataRetriever.
     */
    @Test
    public void testGetTableWhere() {
        System.out.println("getTableWhere");
        String tableName = "";
        String columnName = "";
        String columnValue = "";
        DataRetriever instance = new DataRetrieverImpl();
        ResultSet expResult = null;
        ResultSet result = instance.getTableWhere(tableName, columnName, columnValue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class DataRetrieverImpl implements DataRetriever {

        public ResultSet getTable(String tableName) {
            return null;
        }

        public ResultSet getLatestTable(String tableName) {
            return null;
        }

        public ResultSet getTableWhere(String tableName, String columnName, String columnValue) {
            return null;
        }
    }
}
