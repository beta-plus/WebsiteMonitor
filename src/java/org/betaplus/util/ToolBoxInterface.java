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
     * MD5 Checksum digestType
     */
    static final int MD5 = 0;
    
    /**
     * SHA-1 Checksum digestType
     */
    static final int SHA_1 = 1;
    
    /**
     * SHA-512 Checksum digestType
     */
    static final int SHA_512 = 2;

    /**
     * Generates and compare checksums for two files.
     *
     * Return true if documents match.
     *
     * @param pdfA
     * @param pdfB
     * @param digestType
     * @return
     */
    boolean compareChecksums(File pdfA, File pdfB, int digestType);

    /**
     * Returns a list of differences between strings.
     * 
     * @param textA
     * @param textB
     * @return 
     */
    LinkedList<String> diffText(String textA, String textB);
    
    /**
     * Returns a string of content text from a given web address.
     * @param url
     * @return 
     */
    String getTextFromWeb(String url);
    
    /**
     * Returns a string of content text from a given PDF file.
     * @param pdf
     * @return 
     */
    String getTextFromPDF(File pdf);
    
    /**
     * Returns all PDF's stored at a given web address.
     * @param url
     * @return 
     */
    LinkedList<File> getPdfContent(String url);
    
    /**
     * Returns a double array of three values:
     * 
     *  § percentage removed
     *  § percentage added
     *  § percentage equal
     * 
     * @param diff
     * @return
     */
    double[] percentageChanged(LinkedList<String> diff);    
    
}
