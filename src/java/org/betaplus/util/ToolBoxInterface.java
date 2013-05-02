/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author StephenJohnRussell
 */
public interface ToolBoxInterface {
    
    /**
     * Returns a string of content text from a given PDF file.
     * @param pdf
     * @return 
     */
    String getTextFromPDF(File pdf);
       
    
}
