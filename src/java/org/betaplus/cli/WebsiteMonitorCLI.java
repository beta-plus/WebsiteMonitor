package org.betaplus.cli;

/*
	Title: WebsiteMonitorCLI
	Author: Ben Taylor
	Date: Apr 4, 2013
	Version: 1.0
*/

import java.util.Scanner;

public class WebsiteMonitorCLI
{
    private WebScanner scanner = new WebScanner();
    private URLFunctions urlFunc;
    private UserFunctions userFunc;
    private Scanner in = new Scanner(System.in);
    private boolean valid = false;
    
    /**
     * Constructor method to create a new CLI
     * @throws Exception 
     */
    public WebsiteMonitorCLI() throws Exception
    {
        urlFunc = new URLFunctions();
        userFunc = new UserFunctions();
        doMainMenu();
    }
    
    /* ---------- Menu Methods ---------- */
    
    /**
     * Method to create the Main Menu of the CLI
     * @throws Exception 
     */
    private void doMainMenu() throws Exception
    {
        System.out.println("\nMain Menu");
        System.out.println("*********");
        
        System.out.println("1. URL's");
        System.out.println("2. Users");
        System.out.println("3. Scan");
        System.out.println("0. Exit & Close");
        
        System.out.print("\n Enter the option you wish to choose (1,2,3,0) :> ");
        
        switch(in.nextLine())
        {
            case "1": doURLMenu(); break;
            case "2": doUserMenu(); break;
            case "3": scanner.scan(); doMainMenu(); break;
            case "0": System.exit(0); break;
            default: System.out.println("Not A Valid Option"); doMainMenu();
        }
    }
    
    /**
     * Method to create the URL Menu of the CLI
     * @throws Exception 
     */
    private void doURLMenu() throws Exception
    {
        System.out.println("\nURL Menu");
        System.out.println("********");
        
        System.out.println("1. Add New URL");
        System.out.println("2. Remove Existing URL");
        System.out.println("3. View All URL's");
        System.out.println("0. Return To Main Menu");
        
        System.out.print("\n Enter the option you wish to choose (1,2,3,0) :> ");
        
        switch(in.nextLine())
        {
            case "1": doAddURL(); break;
            case "2": doRemoveURL(); break;
            case "3": doViewURL(); break;
            case "0": doMainMenu(); break;
            default: System.out.println("Not A Valid Option"); doURLMenu();
        }
    }
    
    /**
     * Method to create the User Menu of the CLI
     * @throws Exception 
     */
    private void doUserMenu() throws Exception
    {
        System.out.println("\nUser Menu");
        System.out.println("*********");
        
        System.out.println("1. Add New User");
        System.out.println("2. Remove Existing User");
        System.out.println("3. View All User's");
        System.out.println("0. Return To Main Menu");
        
        System.out.print("\n Enter the option you wish to choose (1,2,3,0) :> ");
        
        switch(in.nextLine())
        {
            case "1": doAddUser(); break;
            case "2": doRemoveUser(); break;
            case "3": doViewUser(); break;
            case "0": doMainMenu(); break;
            default: System.out.println("Not A Valid Option"); doUserMenu();
        }
    }
    
    /* ---------- Add Methods ---------- */
    
    /**
     * Method to add a new URL to the system
     * @throws Exception 
     */
    private void doAddURL() throws Exception
    {
        System.out.println("\nAdd URL");
        System.out.println("*******");
        
        System.out.println("Please enter the information you wish to add...");
        System.out.print(" URL :> ");
        String url = in.nextLine();
        System.out.print(" URL Type (html/rss/pdf) :> ");
        String type = in.nextLine();
        
        valid = urlFunc.addURL(url, type);
        
        while(true)
        {
            if(!valid)
            {
                System.out.print(" Try Again (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doAddURL(); break;
                    case "n": doURLMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
            else
            {
                System.out.print(" Enter Another (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doAddURL(); break;
                    case "n": doURLMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
        }
    }
    
    /**
     * Method to add a new user to the system
     * @throws Exception 
     */
    private void doAddUser() throws Exception
    {
        System.out.println("\nAdd User");
        System.out.println("********");
        
        System.out.println("Please enter the information you wish to add...");
        System.out.print(" Username :> ");
        String username = in.nextLine();
        System.out.print(" Email :> ");
        String email = in.nextLine();
        
        valid = userFunc.addUser(username, email);
        
        while(true)
        {
            if(!valid)
            {
                System.out.print(" Try Again (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doAddUser(); break;
                    case "n": doUserMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
            else
            {
                System.out.print(" Enter Another (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doAddUser(); break;
                    case "n": doUserMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
        }
    }
    
    /* ---------- Remove Methods ---------- */
    
    /**
     * Method to remove a URL from the system
     * @throws Exception 
     */
    private void doRemoveURL() throws Exception
    {
        System.out.println("\nRemove URL");
        System.out.println("**********");
        
        System.out.println("Please enter the information you wish to remove...");
        System.out.print(" URL :> ");
        String url = in.nextLine();
        
        valid = urlFunc.removeURL(url);
        
        while(true)
        {
            if(!valid)
            {
                System.out.print(" Try Again (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doRemoveURL(); break;
                    case "n": doURLMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
            else
            {
                System.out.print(" Remove Another (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doRemoveURL(); break;
                    case "n": doURLMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
        }
    }
    
    /**
     * Method to remove a user from the system
     * @throws Exception 
     */
    private void doRemoveUser() throws Exception
    {
        System.out.println("\nRemove User");
        System.out.println("***********");
        
        System.out.println("Please enter the information you wish to remove...");
        System.out.print(" User Email :> ");
        String email = in.nextLine();
        
        valid = userFunc.removeUser(email);
        
        while(true)
        {
            if(!valid)
            {
                System.out.print(" Try Again (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doRemoveUser(); break;
                    case "n": doUserMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
            else
            {
                System.out.print(" Remove Another (Y/N) :> ");
            
                switch(in.nextLine().toLowerCase())
                {
                    case "y": doRemoveUser(); break;
                    case "n": doUserMenu(); break;
                    default: System.out.println("Entry not valid.");
                }
            }
        }
    }
    
    /* ---------- View Methods ---------- */
    
    /**
     * Method to view all URL's stored in the system
     * @throws Exception 
     */
    private void doViewURL() throws Exception
    {
        System.out.println("\nView All URL's");
        System.out.println("**************");
        
        urlFunc.viewURL();
        
        while(true)
        {
            System.out.print(" Print Again (Y/N) :> ");
            
            switch(in.nextLine().toLowerCase())
            {
                case "y": doViewURL(); break;
                case "n": doURLMenu(); break;
                default: System.out.println("Entry not valid.");
            }
        }
    }
    
    /**
     * Method to view all users stored in the system
     * @throws Exception 
     */
    private void doViewUser() throws Exception
    {
        System.out.println("\nView All Users's");
        System.out.println("**************");
        
        userFunc.viewUser();
        
        while(true)
        {
            System.out.print(" Print Again (Y/N) :> ");
            
            switch(in.nextLine().toLowerCase())
            {
                case "y": doViewUser(); break;
                case "n": doUserMenu(); break;
                default: System.out.println("Entry not valid.");
            }
        }
    }
}
