package org.betaplus.cli;

/*
 Title: PDFReader
 Author: Ben Taylor
 Date: Apr 7, 2013
 Version: 1.0
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.betaplus.testcases.SimpleDataSource;

public class PDFReader {

    private Statement stat;
    private Comparitor c = new Comparitor();
    private URL url;
    private int urlID;
    private File file1, file2;

    public PDFReader() throws Exception {
        SimpleDataSource.init("data/database.properties");
        Connection conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    public void setSource(String url, int urlID) {
        try {
            this.url = new URL(url);
            this.urlID = urlID;
        } catch (MalformedURLException ex) {
        }
    }

    private String pdfName(String locationDir, int versionInc) throws Exception {
        int index1 = locationDir.lastIndexOf("/");
        int index2 = locationDir.lastIndexOf("_");
        String name = locationDir.substring(index1 + 1, index2);

        index1 = locationDir.lastIndexOf(".");
        int version = Integer.parseInt(locationDir.substring(index2 + 1, index1));
        version += versionInc;

        return name + "_" + version;
    }

    public void store() throws Exception {
        ResultSet latestEntry = stat.executeQuery("SELECT * FROM pdfs WHERE UrlId= '" + urlID + "'"
                + "AND DLDate= (select max(DLDate) FROM pdfs WHERE UrlID= '" + urlID + "')");

        if (latestEntry.next()) {
            boolean valid = c.downloadFile(url, "Temp");

            if (valid) {
                String locDir = latestEntry.getString("LocationDir");
                file1 = new File("pdf/" + pdfName(locDir, 0) + ".pdf");
                file2 = new File("pdf/Temp.pdf");

                boolean equal = c.compareChecksums(file1, file2, 0);

                if (!equal) {
                    String name = pdfName(locDir, 1);
                    c.downloadFile(url, name);

                    stat.execute("INSERT INTO pdfs (LocationDir,UrlId)"
                            + "VALUES('pdf/" + name + ".pdf','" + urlID + "')");
                }
            }
        } else {
            int index1 = url.toString().lastIndexOf("/") + 1;
            int index2 = url.toString().lastIndexOf(".");
            String name = url.toString().substring(index1, index2);

            boolean valid = c.downloadFile(url, name + "_1");

            if (valid) {
                stat.execute("INSERT INTO pdfs (LocationDir,UrlId)"
                        + "VALUES('pdf/" + name + "_1.pdf','" + urlID + "')");
            }
        }
    }
}
