/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.testcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.betaplus.testcases.DiffMatchPatch.Diff;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class implementing PDFComparitorInterface to provide PDF comparison.
 * Provides: ?-Checksum comparison. ?-Text comparison. ?-% of document changed.
 *
 * @author Stephen John Russell
 * @date 07-Feb-2013
 * @version 0.1
 */
public class Comparitor {//implements PDFComparitorInterface {

    /**
     * MD5 Checksum digestType
     */
    public static final int MD5 = 0;
    /**
     * SHA-1 Checksum digestType
     */
    final int SHA_1 = 1;
    /**
     * SHA-512 Checksum digestType
     */
    final int SHA_512 = 2;

    /**
     * Default constructor.
     */
    public Comparitor() {
    }

    /**
     * Generate checksums for files a & b with digestType and compare.
     *
     * @param fileA
     * @param fileB
     * @param digestType
     * @return
     */
    public static boolean compareChecksums(File fileA, File fileB, int digestType) {
        //InputStreams for checksum generation
        InputStream isA = null;
        InputStream isB = null;
        //The strings to compare
        String digestA = "a";
        String digestB = "b";
        try {
            //Construct streams
            isA = new FileInputStream(fileA);
            isB = new FileInputStream(fileB);
            //Genorate & recover checksums
            String[] checkSums = getChecksums(isA, isB, digestType);
            digestA = checkSums[0];
            digestB = checkSums[1];
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Close streams
                isA.close();
                isB.close();
            } catch (IOException ex) {
                Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Final comparison
            return digestA.equals(digestB);
        }
    }

    /**
     * Generate checksums.
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

    /**
     * For each line separated by "\n" in new doc if it does not exist in new
     * doc, add to list.
     *
     * @param oldVersion
     * @param newVersion
     * @return
     */
    public static LinkedList<String> getDifference(File oldVersion, File newVersion) {
        //The list to return
        LinkedList<String> diff = new LinkedList<String>();
        //Recover text

        String pdfA = pdftoText(oldVersion);
        String pdfB = pdftoText(newVersion);
        //Googles diff_match_patch
        DiffMatchPatch differ = new DiffMatchPatch();
        //The list of diffs
        LinkedList<Diff> l = differ.diff_main(pdfA, pdfB);
        //Convert diffs to string
        for (Diff d : l) {
            diff.add(d.toString());
        }
        return diff;
    }

    /**
     * Extract text from PDF Document.
     *
     * @param f
     * @return
     */
    private static String pdftoText(File f) {
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
            Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //Close open documents.
            if (cosDoc != null) {
                try {
                    cosDoc.close();
                } catch (IOException ex) {
                    Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pdDoc != null) {
                try {
                    pdDoc.close();
                } catch (IOException ex) {
                    Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return parsedText;
    }

    /**
     * Return an integer percentage indicating the amount of text removed from a
     * previous document.
     *
     * @param diff
     * @return
     */
    public static double[] percentageChanged(LinkedList<String> diff) {

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
     * Download all PDF's from a given URL.
     * 
     * @param url
     * @return 
     */
    public static LinkedList<File> downloadPDFS(String url) {
        LinkedList<File> pdfs = new LinkedList<File>();
        try {
            URL host = new URL(url);
            Document doc = Jsoup.parse(host, 100000);
            Elements els = doc.body().select("a[href]");
            int c = 0;
            for(Element e : els) {
                String absUrl = e.absUrl("href");
                System.out.println(absUrl);
                if (absUrl.contains("file") || absUrl.contains(".pdf")) {
                    //File f = new File(downloadFile(e.absUrl("href"), "temp"));
                    File f = new File(downloadFile(absUrl, "File" + c++));
                    pdfs.add(f);
                }                   
            }
            System.out.println("SIZE:" + pdfs.size());
        } catch (IOException ex) {
            Logger.getLogger(Comparitor.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return pdfs;
    }
    
    /**
     * Downloads a single file from a given URL.
     * 
     * @throws IOException
     *             ,MalformedURLException
     */
    private static String downloadFile(String urlIn, String urlOut) throws IOException,
            MalformedURLException {
 
        //Get a connection to the URL and start up a buffered reader.
        URL url = new URL(urlIn);
        url.openConnection();
        InputStream reader = url.openStream();
 
        //Setup a buffered file writer to write out what is read from URL.
        FileOutputStream writer = new FileOutputStream("data/" + urlOut);
        
        byte[] buffer = new byte[153600];
        int bytesRead; 
        while ((bytesRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, bytesRead);
            buffer = new byte[153600];            
        }
        writer.close();
        reader.close();
        
        return urlOut;
    }
}
