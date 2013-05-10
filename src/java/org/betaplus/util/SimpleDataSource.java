package org.betaplus.util;

/*
 * Author: James Finney
 * Title: Simple Data Source
 * Created: 01/02/2013
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SimpleDataSource
{
    private static String url;
    private static String username;
    private static String password;
    
    /**
     * @param fileName - File containing the database properties
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static void init(String fileName)
            throws IOException, ClassNotFoundException
    {
        // Create new properties object
        Properties props = new Properties();
        // Inport the properties file
        FileInputStream in = new FileInputStream(fileName);
        props.load(in);
        
        String driver = props.getProperty("jdbc.driver");
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        
        if (username == null)
        {
            username = "";
        }
        
        password = props.getProperty("jdbc.password");
        
        if (password == null)
        {
            password = "";
        }
        
        if (driver != null)
        {
            Class.forName(driver);
        }
    }
    
    /**
     * @return - Returns the database connection
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }
}