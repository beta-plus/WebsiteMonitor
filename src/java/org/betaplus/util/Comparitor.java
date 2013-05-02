/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.util.LinkedList;

/**
 * Interface defining comparison between data collected and data stored.
 * 
 * @author StephenJohnRussell
 */
public interface Comparitor {
    
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
    public boolean compareChecksums(File pdfA, File pdfB, int digestType);
    
    /**
     * Returns a list of differences between strings.
     * 
     * @param textA
     * @param textB
     * @return 
     */
    public LinkedList<String> diffText(String textA, String textB);
    
    /**
     * Returns a double array of three values:
     * 
     *  0 - percentage removed
     *  1 - percentage added
     *  2 - percentage equal
     * 
     * @param diff
     * @return
     */
    public double[] percentageChanged(LinkedList<String> diff);
    
}