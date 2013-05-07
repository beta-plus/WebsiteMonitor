package org.betaplus.testcases;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author James Finney
 * @Title Database Generator
 * @edited Stephen John Russell
 * @Created 01/02/2013
 * @editedOn 07/05/2013
 * @version 1.1
 */
public class DatabaseGenerator {

    public DatabaseGenerator() {
        System.out.println("Starting Database Generator...");

        System.out.println("Attempting to initialise data source...");
        Connection conn = null;
        try {
            SimpleDataSource.init("data/database.properties");

            System.out.println("Source initialised!");

            System.out.println("Getting connection...");

            // Get and make the connection
            conn = SimpleDataSource.getConnection();

            System.out.println("Connection obtained!");


            Statement stat = conn.createStatement();

            /**
             * Buffered reader changed to scanner by Stephen Russell 7/5/13 for
             * ease of use of ; as delimiter in SQL script.
             */
            // Read the SQL file
            Scanner in = new Scanner(new FileReader("data/database.sql"));
            String str;
            in.useDelimiter(";");
            System.out.println("Reading file...\n");

            while (in.hasNext()) {
                str = in.next();
                //System.out.println(str);
                stat.execute(str);
            }

            System.out.println("Database creation complete!");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Close the connection
                if (conn != null) {
                    conn.close();
                }                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Database Generator...");

        System.out.println("Attempting to initialise data source...");

        SimpleDataSource.init("data/database.properties");

        System.out.println("Source initialised!");

        System.out.println("Getting connection...");

        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();

        System.out.println("Connection obtained!");

        try {
            Statement stat = conn.createStatement();

            /**
             * Buffered reader changed to scanner by Stephen Russell 7/5/13 for
             * ease of use of ; as delimiter in SQL script.
             */
            // Read the SQL file
            Scanner in = new Scanner(new FileReader("data/database.sql"));
            String str;
            in.useDelimiter(";");
            System.out.println("Reading file...\n");

            while (in.hasNext()) {
                str = in.next();
                System.out.println(str);
                stat.execute(str);
            }

            System.out.println("Database creation complete!");
        } finally {
            // Close the connection
            conn.close();
        }
    }
}