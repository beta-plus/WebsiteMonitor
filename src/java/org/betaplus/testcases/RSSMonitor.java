package org.betaplus.testcases;

/*
 * Author: James Finney
 * Title: RSS Monitor
 * Created: 01/02/2012
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class RSSMonitor
{
    public static void main(String[] args) throws Exception
    {
        String urlFeed = "";
        Boolean urlValid = false;
        System.out.println("Starting RSS Monitor...");
        
        System.out.println("Please enter the feed URL...");
        Scanner in = new Scanner(System.in);
        
        do
        {
            urlFeed = in.nextLine();
            String checkExt = urlFeed.substring(urlFeed.length() - 3);
            if ("xml".equals(checkExt))
            {
                urlValid = true;
                try
                {
                    URL test = new URL(urlFeed);
                } catch (MalformedURLException ex)
                {
                    urlValid = false;
                }
                
                if (urlValid)
                {
                    break;
                }
            }
            System.out.println("I'm sorry but the URL you provided isn't a suitable RSS feed. Please try another URL...");
        } while (!urlValid);
        
        in.close();
        
        RSSMonitorFunctions monitor = new RSSMonitorFunctions(urlFeed);
        
        //System.out.println("Getting feed...");
        //monitor.displayFeed();
        
        System.out.println("Inserting feed...");
        monitor.insertDatabase();
        
        System.out.println("Insert complete!");
    }
}