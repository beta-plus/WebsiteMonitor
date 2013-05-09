/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.datatypes;

import java.io.File;

/**
 *
 * @author StephenJohnRussell
 */
public class Pdf {
    
    private String pdfHash;
    private String pdfName;
    private String pdfUrl;
    private File pdfFile;
    private static WebSource ws;

    public Pdf(String pdfHash, String pdfName, String pdfUrl, File pdfFile, WebSource ws) {
        this.pdfHash = pdfHash;
        this.pdfName = pdfName;
        this.pdfUrl = pdfUrl;
        this.pdfFile = pdfFile;
        Pdf.ws = ws;
    }

    public String getPdfHash() {
        return pdfHash;
    }

    public void setPdfHash(String pdfHash) {
        this.pdfHash = pdfHash;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public static WebSource getWs() {
        return ws;
    }

    public static void setWs(WebSource ws) {
        Pdf.ws = ws;
    }

    
}
