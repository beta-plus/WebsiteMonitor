/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.betaplus.testcases.DiffMatchPatch;

/**
 *
 * @author StephenJohnRussell
 */
public class ComparatorImpl implements Comparator {

    /**
     * Generate checksums for files a & b with digestType and compare.
     *
     * @param fileA
     * @param fileB
     * @param digestType
     * @return
     */
    @Override
    public boolean compareChecksums(File fileA, File fileB, int digestType) {
        //InputStreams for checksum generation
        InputStream isA = null;
        InputStream isB = null;
        //Bool to return
        boolean tf = false;
        try {
            //Construct streams
            isA = new FileInputStream(fileA);
            isB = new FileInputStream(fileB);
            //Genorate & recover checksums
            String[] checkSums = getChecksums(isA, isB, digestType);
            String digestA = checkSums[0];
            String digestB = checkSums[1];
            tf = digestA.equals(digestB);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Close streams
                isA.close();
                isB.close();
            } catch (IOException ex) {
                Logger.getLogger(Comparator.class.getName()).log(Level.INFO, null, ex);
            }
            //Final comparison
            return tf;
        }
    }

    @Override
    public LinkedList<String> diffFiles(File oldVersion, File newVersion) {
        //The list to return
        LinkedList<String> diff = new LinkedList<String>();
        //Recover text from PDF.
        String pdfA = pdftoText(oldVersion);
        String pdfB = pdftoText(newVersion);
        //Googles diff_match_patch
        DiffMatchPatch differ = new DiffMatchPatch();
        //The list of diffs
        LinkedList<DiffMatchPatch.Diff> l = differ.diff_main(pdfA, pdfB);
        //Convert diffs to string
        for (DiffMatchPatch.Diff d : l) {
            diff.add(d.toString());
        }
        return diff;
    }

    /**
     * Extract text from PDF Document or return text if file not a pdf.
     *
     * @param f
     * @return
     */
    private static String pdftoText(File f) {
        boolean notPDF = false;
        //Document parser
        PDFParser parser;
        //The text to return
        String parsedText = null;
        //Stripper object
        PDFTextStripper pdfStripper;
        //The document to be operated on
        PDDocument pdDoc = null;
        //The document to construcPDF from
        COSDocument cosDoc = null;
        try {
            //Parse new doc from file
            parser = new PDFParser(new FileInputStream(f));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            //pdfStripper.setLineSeparator("\n");
            parsedText = pdfStripper.getText(pdDoc);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
            notPDF = true;            
        } catch (IOException ex) {
            Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
            notPDF = true;
        } finally {
            //Close open documents.
            if (cosDoc != null) {
                try {
                    cosDoc.close();
                } catch (IOException ex) {
                    Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pdDoc != null) {
                try {
                    pdDoc.close();
                } catch (IOException ex) {
                    Logger.getLogger(Comparator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (notPDF) {
                
            }
        }
        return parsedText;
    }

    @Override
    public LinkedList<String> diffText(String oldText, String newText) {
        //The list to return
        LinkedList<String> diff = new LinkedList<String>();
        //Googles diff_match_patch
        DiffMatchPatch differ = new DiffMatchPatch();
        //The list of diffs
        LinkedList<DiffMatchPatch.Diff> l = differ.diff_main(oldText, newText);
        //Convert diffs to string
        for (DiffMatchPatch.Diff d : l) {
            diff.add(d.toString());
        }
        return diff;
    }

    

    /**
     * Return an integer percentage indicating the amount of text removed from a
     * previous document.
     *
     * @param diff
     * @return
     */
    @Override
    public double[] percentageChanged(LinkedList<String> diff) {

        //The values to return.
        double removed = 0;
        double added = 0;
        double equal = 0;

        for (String s : diff) {
            if (s.contains("Diff(DELETE")) {
                //Remove header & footer tags from string
                s = s.replace("Diff(DELETE,\"", "");
                s = s.substring(0, s.length() - 2);
                removed += s.length();
            } else if (s.contains("Diff(INSERT,\"")) {
                //Remove header & footer tags from string
                s = s.replace("Diff(INSERT,\"", "");
                s = s.substring(0, s.length() - 2);
                added += s.length();
            } else if (s.contains("Diff(EQUAL,\"")) {
                //Remove header & footer tags from string
                s = s.replace("Diff(EQUAL,\"", "");
                s = s.substring(0, s.length() - 2);
                equal += s.length();
            }
        }
        //The total number of chars
        double total = removed + added + equal;
        //Calculate %
        removed = (removed / total) * 100;
        added = (added / total) * 100;
        equal = (equal / total) * 100;
        double[] changes = {removed, added, equal};
        return changes;
    }

    
    /**
     * Generate checksums and return a String[].
     *
     * @param isA
     * @param isB
     * @param type
     * @return
     * @throws IOException
     */
    private static String[] getChecksums(InputStream isA, InputStream isB, int type)
            throws IOException {
        //Strings to hold checksums
        String checkSumA;
        String checkSumB;
        if (type == 0) {        // 0 == MD5
            checkSumA = org.apache.commons.codec.digest.DigestUtils.md5Hex(isA);
            checkSumB = org.apache.commons.codec.digest.DigestUtils.md5Hex(isB);
        } else if (type == 1) { // 1 == SHA-1
            checkSumA = org.apache.commons.codec.digest.DigestUtils.sha1Hex(isA);
            checkSumB = org.apache.commons.codec.digest.DigestUtils.sha1Hex(isB);
        } else {                // 2 == SHA-512
            checkSumA = org.apache.commons.codec.digest.DigestUtils.sha512Hex(isA);
            checkSumB = org.apache.commons.codec.digest.DigestUtils.sha512Hex(isB);
        }
        //Construct array
        String[] checkSums = {checkSumA, checkSumB};
        return checkSums;
    }
    
}
