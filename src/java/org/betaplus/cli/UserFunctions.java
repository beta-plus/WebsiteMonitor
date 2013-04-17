package org.betaplus.cli;

/*
 Title: UserFunctions
 Author: Ben Taylor
 Date: Apr 5, 2013
 Version: 1.0
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.betaplus.testcases.SimpleDataSource;

public class UserFunctions {

    Statement stat;

    public UserFunctions() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    public boolean addUser(String username, String email) throws Exception {
        ResultSet checkUrl = stat.executeQuery("SELECT UserEmail FROM users WHERE UserEmail= '" + email + "'");

        if (checkUrl.next()) {
            System.out.println("This user email is already in use.");
            return false;
        } else {
            stat.execute("INSERT INTO users (UserName, UserEmail) VALUES('" + username + "','" + email + "')");
            System.out.println("User Successfully Added.");
            return true;
        }
    }

    public boolean removeUser(String email) throws Exception {
        ResultSet checkUrl = stat.executeQuery("SELECT UserEmail FROM users WHERE UserEmail= '" + email + "'");

        if (checkUrl.next()) {
            stat.execute("DELETE FROM users WHERE UserEmail= '" + email + "'");
            System.out.println("User Successfully Deleted.");
            return true;
        } else {
            System.out.println("This user email cannot be found in the system.");
            return false;
        }
    }

    public void viewUser() throws Exception {
        ResultSet allUrls = stat.executeQuery("SELECT * FROM users");

        ResultSetMetaData rsmd = allUrls.getMetaData();
        String firstColumnName = rsmd.getColumnName(1);
        String secondColumnName = rsmd.getColumnName(2);
        String thirdColumnName = rsmd.getColumnName(3);

        System.out.printf("%-30s%-30s%s\n", firstColumnName, secondColumnName, thirdColumnName);

        while (allUrls.next()) {
            System.out.printf("%-30s%-30s%s\n", allUrls.getString(1), allUrls.getString(2), allUrls.getString(3));
        }
    }
}
