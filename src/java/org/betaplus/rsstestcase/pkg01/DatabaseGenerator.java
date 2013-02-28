package org.betaplus.rsstestcase.pkg01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseGenerator 
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

            BufferedReader in = new BufferedReader(new FileReader("/Users/Jay/Documents/Documents/University Work/Year 02/Year 02 - Semester 02/NetBeans Projects/Software Hut/RSS Monitor/src/pkg01/database.sql"));
            String str;
            StringBuffer sb = new StringBuffer();
            System.out.println("Read!\n");
            while ((str = in.readLine()) != null) 
            {
                sb.delete(0, sb.length());
                sb.append(str + "\n");
                System.out.println(sb.toString());
                stat.execute(sb.toString());
            }
            System.out.println("Database creation complete!");
        }
        finally
        {
            // Close the connection
            conn.close();
        }
    }
}