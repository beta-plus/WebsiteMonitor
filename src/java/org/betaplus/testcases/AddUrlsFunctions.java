package org.betaplus.testcases;

/*
 * Author: James Finney
 * Title: Add Urls Functions
 * Created: 29/03/2013
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

public class AddUrlsFunctions
{
    Statement stat;
    String url = "";
    String type = "";
    String input;
    boolean menuOp;
    boolean urlValid = false;
    boolean continue1 = false;
    Scanner in = new Scanner(System.in);
    
    public AddUrlsFunctions() throws Exception
    {
        SimpleDataSource.init("data/database.properties");
        
        // Get and make the connection
        Connection conn = SimpleDataSource.getConnection();
        
        stat = conn.createStatement();
    }
    
    public void doMenu() throws Exception
    {
        menuOp = true;
        
        while(menuOp)
        {
            System.out.println("\nPlease select an option:\n"
                             + "1.Add new URL\n"
                             + "2.Remove a URL\n"
                             + "3.View all URLS\n"
                             + "0.Close & Exit");
            
            input = in.nextLine();
            
            int sel = -1;
            
            try
            {
                sel = Integer.parseInt(input);
            } catch(NumberFormatException ex)
            {
                System.out.println("\nPlease enter a valid menu selection.");
            }
            
            if (sel != -1)
            {
                doWork(sel);
            }
        }
    }
        
    public void doWork(int sel) throws Exception
    {
        if (sel == 0)
        {
            System.exit(0);
        } else if (sel == 1)
        {
            addUrl();
            menuOp = false;
        } else if (sel == 2)
        {
            removeUrl();
            menuOp = false;
        } else if (sel == 3)
        {
            viewAllUrls();
            menuOp = false;
        } else
        {
            System.out.println("\nI'm sorry but the option isn't recongised. Please try agin");
            doMenu();
        }
    }
    
    public void addUrl() throws Exception
    {
        System.out.println("\nAdd a URL");
        System.out.println("*********\n");
        
        if (continue1)
        {
            in.nextLine();
        }
        
        System.out.println("Please enter the URL you wish to add...");
        url = in.nextLine();
            
        ResultSet checkUrl = stat.executeQuery("SELECT Url FROM urls WHERE Url= '" + url + "'");
        
        if (checkUrl.next())
        {
            System.out.println("This URL is already included in the system!\n");

            continue1 = true;
            while (continue1)
            {
                System.out.print("Try again (Y/N)\t:> ");
                String cont = in.next();

                if (cont.equalsIgnoreCase("N"))
                {
                    continue1 = false;
                    doMenu();
                } else if (cont.equalsIgnoreCase("Y"))
                {
                    addUrl();
                } else
                {   
                    System.out.println("I'm sorry but you've entered an invalid "
                            + "selection. Please try again");
                }
            }
        } else
        {
            urlValid = true;
        }

        try
        {
            URL test = new URL(url);
        } catch (MalformedURLException ex)
        {
            urlValid = false;
            System.out.println("I'm sorry but the URL you provided isn't suitable.\n");

            continue1 = true;
            while (continue1)
            {
                System.out.print("Try again (Y/N)\t:> ");
                String cont = in.next();

                if (cont.equalsIgnoreCase("N"))
                {
                    continue1 = false;
                    doMenu();
                } else if (cont.equalsIgnoreCase("Y"))
                {
                    addUrl();
                } else
                {   
                    System.out.println("I'm sorry but you've entered an invalid "
                            + "selection. Please try again");
                }
            }
        }

        while (true)
        {
            System.out.println("\nPlease enter the URL type...");
            System.out.println("Types can either be - html, rss or pdf");
            System.out.println("Or type ./exit to exit");
            
            type = in.nextLine();
            type = type.toLowerCase();

            if ("html".equals(type) || "rss".equals(type) || "pdf".equals(type))
            {
                break;
            } else if ("./exit".equals(type))
            {
                continue1 = false;
                doMenu();
            } else
            {
                System.out.println("I'm sorry but that type is not accepted. Please try again.");
                
            }
        }

        if (urlValid)
        {
            stat.execute("INSERT INTO urls (Url, Type) VALUES('" + url + "','" + type + "')");
            System.out.println("URL added!\n");
        }

        continue1 = true;
        while (continue1)
        {
            System.out.print("Enter another (Y/N)\t:> ");
            String cont = in.next();

            if (cont.equalsIgnoreCase("N"))
            {
                continue1 = false;
                doMenu();
            } else if (cont.equalsIgnoreCase("Y"))
            {
                addUrl();
            } else
            {   
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
    
    public void removeUrl() throws Exception
    {
        System.out.println("\nRemove a URL");
        System.out.println("************\n");
        
        if (continue1)
        {
            in.nextLine();
        }
        
        System.out.println("Please enter the URL you wish to delete...");
        url = in.nextLine();

        ResultSet checkUrl = stat.executeQuery("SELECT Url FROM urls WHERE Url= '" + url + "'");

        if (checkUrl.next())
        {
            stat.execute("DELETE FROM urls WHERE Url= '" + url + "'");
            System.out.println("Deleted URL!");
        } else
        {
            System.out.println("This URL is not already included in the system!\n");
            
            continue1 = true;
            while (continue1)
            {
                System.out.print("Try again (Y/N)\t:> ");
                String cont = in.next();

                if (cont.equalsIgnoreCase("N"))
                {
                    continue1 = false;
                    doMenu();
                } else if (cont.equalsIgnoreCase("Y"))
                {
                    removeUrl();
                } else
                {   
                    System.out.println("I'm sorry but you've entered an invalid "
                            + "selection. Please try again");
                }
            }
        }
        
        continue1 = true;
        while (continue1)
        {
            System.out.print("Delete another (Y/N)\t:> ");
            String cont = in.next();

            if (cont.equalsIgnoreCase("N"))
            {
                continue1 = false;
                doMenu();
            } else if (cont.equalsIgnoreCase("Y"))
            {
               removeUrl();
            } else
            {
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
    
    public void viewAllUrls() throws Exception
    {
        System.out.println("\nView all URLs");
        System.out.println("*************\n");
        
        ResultSet allUrls = stat.executeQuery("SELECT * FROM urls");
            
        ResultSetMetaData rsmd = allUrls.getMetaData();
        String firstColumnName = rsmd.getColumnName(1);
        String secondColumnName = rsmd.getColumnName(2);
        String thirdColumnName = rsmd.getColumnName(3);
        
        System.out.printf("%-30s%-30s%s\n", firstColumnName, secondColumnName, thirdColumnName);
            
        while(allUrls.next())
        {
            System.out.printf("%-30s%-30s%s\n", allUrls.getString(1), allUrls.getString(2), allUrls.getString(3));        
        }
        
        continue1 = true;
        while(continue1)
        {
            System.out.print("\nReturn to menu (Y/N)\t:> ");
            String cont = in.next();

            if (cont.equalsIgnoreCase("Y"))
            {
                continue1 = false;
                doMenu();
            } else if (cont.equalsIgnoreCase("N"))
            {
                viewAllUrls();
            } else
            {
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
}