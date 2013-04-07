package org.betaplus.cli;

/*
 Title: URLFunctions
 Author: Ben Taylor
 Date: Apr 5, 2013
 Version: 1.0
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.betaplus.testcases.SimpleDataSource;

public class URLFunctions {

    Statement stat;

    public URLFunctions() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    public boolean addURL(String url, String type) throws Exception {
        ResultSet checkUrl = stat.executeQuery("SELECT Url FROM urls WHERE Url= '" + url + "'");

        if (checkUrl.next()) {
            System.out.println("This URL is already in use.");
            return false;
        }

        try {
            URL test = new URL(url);
        } catch (MalformedURLException ex) {
            System.out.println("The URL you provided is not valid.");
            return false;
        }

        type = type.toLowerCase();
        if ("html".equals(type) || "rss".equals(type) || "pdf".equals(type)) {
            stat.execute("INSERT INTO urls (Url, Type) VALUES('" + url + "','" + type + "')");
            System.out.println("URL Successfully Added");
            return true;
        } else {
            System.out.println("The type you entered is not valid.");
            return false;
        }
    }

    public boolean removeURL(String url) throws Exception {
        ResultSet checkUrl = stat.executeQuery("SELECT Url FROM urls WHERE Url= '" + url + "'");

        if (checkUrl.next()) {
            stat.execute("DELETE FROM urls WHERE Url= '" + url + "'");
            System.out.println("URL Successfully Deleted.");
            return true;
        } else {
            System.out.println("This URL cannot be found in the system.");
            return false;
        }
    }

    public void viewURL() throws Exception {
        ResultSet allUrls = stat.executeQuery("SELECT * FROM urls");

        ResultSetMetaData rsmd = allUrls.getMetaData();
        String firstColumnName = rsmd.getColumnName(1);
        String secondColumnName = rsmd.getColumnName(2);
        String thirdColumnName = rsmd.getColumnName(3);

        System.out.printf("%-10s%-100s%s\n", firstColumnName, secondColumnName, thirdColumnName);

        while (allUrls.next()) {
            System.out.printf("%-10s%-100s%s\n", allUrls.getString(1), allUrls.getString(2), allUrls.getString(3));
        }
    }
}