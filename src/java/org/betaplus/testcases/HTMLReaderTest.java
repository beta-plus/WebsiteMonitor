package org.betaplus.testcases;

public class HTMLReaderTest 
{
    public static void main(String[] arg) throws Exception 
    {
        HTMLReader reader = new HTMLReader();
        System.out.println("Starting HTML Reader Test...");
        
        String url = "http://www.oracle.com/index.html";
        
        System.out.println("Creating new HTML Reader to read \"" + url + "\"...");
        reader.setSource(url);
        
        System.out.println("Reading HTML from \"" + url + "\"...");
        reader.readHTML();
        
        System.out.println("Removing Markup...");
        reader.removeMarkup();
        
        System.out.println("Displaying Text...");
        reader.printText();
        
        System.out.println("Ending Test...");
    }
}
