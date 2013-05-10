package org.betaplus.testcases;

/*
 * Author: James Finney
 * Title: Test Database
 * Created: 01/02/2013
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import org.betaplus.util.SimpleDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Starting TestDB...");
        
        System.out.println("Attempting to initialise data source...");
        
        SimpleDataSource.init("data/database.properties");
        
        System.out.println("Source initialised!");
        
        System.out.println("Getting connection...");
        
        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();
        
        System.out.println("Connection obtained!");
        
        try
        {
            Statement stat = conn.createStatement();
            
            stat.execute("drop table if exists test");
            
            // Create the table
            stat.execute("CREATE TABLE test (Name VARCHAR(30))");
            
            // Populate the table with a single value
            stat.execute("INSERT INTO test VALUES ('Romeo')");
            
            // Run the query to get and display all records from the table
            ResultSet result = stat.executeQuery("SELECT * FROM test");
            result.next();
            System.out.println("\n" + result.getString("Name"));
        }
        
        finally
        {
            // Close the connection
            conn.close();
        }
    }
}

/*
 * Another way -
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet rs = dbm.getTables(null, null,"Test", args);
    if (rs.next())
    {
        System.out.println("Table exists!");
        stat.execute("DROP TABLE Test");
    } else
    {
        System.out.println("Table does not exist"); 
    } 
*/