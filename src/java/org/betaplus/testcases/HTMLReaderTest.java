package org.betaplus.testcases;

public class HTMLReaderTest 
{
    public static void main(String[] arg) 
    {
        System.out.println("Starting HTML Reader Test...");
        
        String siteName = "Oracle";
        String url = "http://www.oracle.com/index.html";
        
        System.out.println("Creating new HTML Reader to read \"" + url + "\"...");
        HTMLReader reader = new HTMLReader(siteName, url);
        
        System.out.println("Reading HTML from \"" + url + "\"...");
        reader.readHTML();
        
        System.out.println("Removing Markup...");
        reader.removeMarkup();
        
        System.out.println("Displaying Text...");
        reader.printText();
        
        System.out.println("Ending Test...");
    }
}
