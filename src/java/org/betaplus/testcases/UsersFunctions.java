package org.betaplus.testcases;

/*
 * Author: James Finney
 * Title: Users Functions
 * Created: 29/03/2013
 * Version: 1.0
 */

/**
 * @author James Finney
 * @version 1.0
 */

import org.betaplus.util.SimpleDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

public class UsersFunctions
{
    Statement stat;
    String userName = "";
    String userEmail = "";
    String input;
    boolean menuOp;
    boolean userValid = false;
    boolean continue1 = false;
    Scanner in = new Scanner(System.in);
    
    public UsersFunctions() throws Exception
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
                             + "1.Add new User\n"
                             + "2.Remove a User\n"
                             + "3.View all Users\n"
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
            addUser();
            menuOp = false;
        } else if (sel == 2)
        {
            removeUser();
            menuOp = false;
        } else if (sel == 3)
        {
            viewAllUsers();
            menuOp = false;
        } else
        {
            System.out.println("\nI'm sorry but the option isn't recongised. Please try agin");
            doMenu();
        }
    }
    
    public void addUser() throws Exception
    {
        System.out.println("\nAdd a User");
        System.out.println("**********\n");
        
        if (continue1)
        {
            in.nextLine();
        }
        
        System.out.println("Please enter your username...");
        userName = in.nextLine();
        
        System.out.println("Please enter your useremail...");
        userEmail = in.nextLine();
            
        ResultSet checkUrl = stat.executeQuery("SELECT UserEmail FROM users WHERE UserEmail= '" + userEmail + "'");
        
        if (checkUrl.next())
        {
            System.out.println("This useremail is already included in the system!\n");

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
                    addUser();
                } else
                {   
                    System.out.println("I'm sorry but you've entered an invalid "
                            + "selection. Please try again");
                }
            }
        } else
        {
            userValid = true;
        }

        if (userValid)
        {
            stat.execute("INSERT INTO users (UserName, UserEmail) VALUES('" + userName + "','" + userEmail + "')");
            System.out.println("User added!\n");
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
                addUser();
            } else
            {   
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
    
    public void removeUser() throws Exception
    {
        System.out.println("\nRemove a User");
        System.out.println("*************\n");
        
        if (continue1)
        {
            in.nextLine();
        }
        
        System.out.println("Please enter the Useremail of the user you wish to delete...");
        userEmail = in.nextLine();

        ResultSet checkUrl = stat.executeQuery("SELECT UserEmail FROM users WHERE UserEmail= '" + userEmail + "'");

        if (checkUrl.next())
        {
            stat.execute("DELETE FROM users WHERE UserEmail= '" + userEmail + "'");
            System.out.println("Deleted user!");
        } else
        {
            System.out.println("This user is not already included in the system!\n");
            
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
                    removeUser();
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
               removeUser();
            } else
            {
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
    
    public void viewAllUsers() throws Exception
    {
        System.out.println("\nView all Users");
        System.out.println("**************\n");
        
        ResultSet allUrls = stat.executeQuery("SELECT * FROM users");
            
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
                viewAllUsers();
            } else
            {
                System.out.println("I'm sorry but you've entered an invalid "
                        + "selection. Please try again");
            }
        }
    }
}