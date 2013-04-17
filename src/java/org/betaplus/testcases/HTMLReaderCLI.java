package org.betaplus.testcases;

import java.util.Scanner;

/*
	Title: HTMLReaderCLI
	Author: Ben Taylor
	Date: Mar 28, 2013
	Version: 1.0
*/

public class HTMLReaderCLI 
{
    public static void main(String[] args) throws Exception 
    {
        Scanner in = new Scanner(System.in);
        HTMLReader reader = new HTMLReader();
        
        System.out.println("Starting HTML Reader Interface...");
        System.out.println("Creating new HTML Reader...");
        System.out.print("Enter the URL of the site :>");
        String url = in.nextLine();
        
        boolean valid = reader.setSource(url);
        
        while(!valid)
        {
            System.out.println("The URL you entered was not valid.");
            System.out.print("Please enter another :> ");
            url = in.nextLine();
            valid = reader.setSource(url);
        }
        
        System.out.println("Reading HTML from \"" + url + "\"...");
        reader.readHTML();

        System.out.println("Removing Markup...");
        reader.removeMarkup();
        
        System.out.println("Displaying Text...");
        reader.printText();
        
        System.out.println("Storing in database...");
        reader.insertDatabase();
    }
}
