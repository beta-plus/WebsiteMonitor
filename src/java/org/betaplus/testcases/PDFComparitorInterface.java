/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.testcases;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author Stephen John Russell
 * @date 07-Feb-2013
 * @version 0.1
 */
public interface PDFComparitorInterface {

    /**
     * MD5 Checksum digestType
     */
    final int MD5 = 0;
    
    /**
     * SHA-1 Checksum digestType
     */
    final int SHA_1 = 1;
    
    /**
     * SHA-512 Checksum digestType
     */
    final int SHA_512 = 2;

    /**
     * Generate and compare checksums for Files a & b.
     *
     * Return true if documents match.
     *
     * @param a
     * @param b
     * @param digestType
     * @return
     */
    boolean compareChecksums(File a, File b, int digestType);

    /**
     * Return a String[] of previously unused sentences.
     *
     * @param a
     * @param b
     * @return
     */
    LinkedList<String> getDifference(File a, File b);
    
    /**
     * Return a double array of three values:
     * 
     *  ? percentage removed
     *  ? percentage added
     *  ? percentage equal
     * 
     * @param diff
     * @return
     */
    double[] percentageChanged(LinkedList<String> diff);
}
