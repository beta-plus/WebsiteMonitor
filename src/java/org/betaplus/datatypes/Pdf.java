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
    private File pdfFile;
    private static WebSource ws;

    public Pdf(String pdfHash, String pdfName, File pdfFile, WebSource ws) {
        this.pdfHash = pdfHash;
        this.pdfName = pdfName;
        this.pdfFile = pdfFile;
        this.ws = ws;
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

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public static WebSource getWs() {
        return ws;
    }

    public void setWs(WebSource ws) {
        this.ws = ws;
    }
}
