package org.betaplus.rsstestcase.pkg01;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Starting TestDB...");
        
        System.out.println("Attempting to initialise data source...");
        
        SimpleDataSource.init("/Users/Jay/Documents/Documents/University Work/Year 02/Year 02 - Semester 02/NetBeans Projects/Software Hut/RSS Monitor/src/pkg01/database.properties");
        
        System.out.println("Source initialised!");
        
        System.out.println("Getting connection...");
        
        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();
        
        System.out.println("Connection obtained!");
        
        try
        {
            Statement stat = conn.createStatement();
            
            stat.execute("drop table if exists Test");
            
            // Create the table
            stat.execute("CREATE TABLE Test (Name VARCHAR(20))");
            // Populate the table with a single value
            stat.execute("INSERT INTO Test VALUES ('Romeo')");
            
            // Run the query to get all records from the table
            ResultSet result = stat.executeQuery("SELECT * FROM Test");
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

/*1  DatabaseMetaData dbm = conn.getMetaData();
    ResultSet rs = dbm.getTables(null, null,"Test", args);
    if (rs.next()) {
        System.out.println("Table exists!");
        stat.execute("DROP TABLE Test");
    } else {
        System.out.println("Table does not exist"); 
    } 
*/