/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.betaplus.datatypes.KeyWord;
import org.betaplus.datatypes.Pdf;
import org.betaplus.datatypes.RssFeed;
import org.betaplus.datatypes.User;
import org.betaplus.datatypes.WebSource;
import org.betaplus.datatypes.WebText;
import org.betaplus.testcases.DatabaseGenerator;
import org.betaplus.testcases.SimpleDataSource;

/**
 *
 * @author StephenJohnRussell
 */
public class DataDumperImpl implements DataDumper {

    Connection conn;
    Statement stat;

    public static void main(String[] args) {

        DatabaseGenerator g = new DatabaseGenerator();
        System.out.println("\n"
                + "========================================================\n"
                + "********************************************************\n"
                + "***********TESTING DATA DUMPER METHODS *****************\n"
                + "********************************************************\n"
                + "========================================================\n");

        DataDumper dd = new DataDumperImpl();
        String[] urls = {"http://www.lga.org.mt/lga/content.aspx?id=92272", "http://www.gov.im/gambling/regulatory.xml", "http://regulations.porezna-uprava.hr/"};
        String[] sources = {"http://www.lga.org.mt/lga/content", "http://www.gov.im/gambling/", "http://regulations.porezna-uprava.hr/"};
        for (int i = 0; i < urls.length; i++) {
            WebSource ws = new WebSource(sources[i], urls[i], null, i);
            dd.dumpWebSource(ws);
            if (i == 0) {
                dd.dumpKeyWord(new KeyWord("file_provider", ws));
            }            
            dd.dumpKeyWord(new KeyWord("pdf", ws));
        }
    }

    public DataDumperImpl() {
        try {
            SimpleDataSource.init("data/database.properties");
            this.conn = SimpleDataSource.getConnection();
            this.stat = conn.createStatement();
        } catch (IOException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean dumpWebSource(WebSource ws) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Urls (Rss_Url, Http_Url, Url_Name) "
                    + "VALUES ('" + ws.getRssURL() + "', '" + ws.getWebPageURL()
                    + "', '" + ws.getSource() + "')");
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean dumpPDF(Pdf pdf) {
        boolean ret = false;
        FileInputStream is = null;
        try {
            ResultSet r = stat.executeQuery("SELECT Url_Id FROM Urls WHERE Url_Name='" + Pdf.getWs().getSource() + "'");
            String urlId = "";
            if (r.next()) {
                urlId = r.getString("Url_Id");
            }
            File f = pdf.getPdfFile();
            is = new FileInputStream(f);
            byte[] b = new byte[(int) f.length()];
            is.read(b);
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Pdfs (Pdf_Hash, Url_Id, Pdf_File, Pdf_Name) "
                    + "VALUES ('" + pdf.getPdfHash() + "', '" + urlId + ""
                    + "', '" + b + "', '" + pdf.getPdfName() + "')");
            statement.execute();
            ret = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                return ret;
            }
        }
    }

    @Override
    public boolean dumpRSS(RssFeed rss) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean dumpWebText(WebText webText) {
        try {
            ResultSet r = stat.executeQuery("SELECT Url_Id FROM Urls WHERE Url_Name='" + webText.getWe().getSource() + "'");
            String urlId = "";
            if (r.next()) {
                urlId = r.getString("Url_Id");
            }
            for (String s : webText.getPageContent()) {
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '\'') {
                        s = s.substring(0, i-1) + "\\" + s.substring(i);
                    }
                }
                PreparedStatement statement = conn.prepareStatement("INSERT INTO Html (Html_Content, Html_Name, Url_Id) "
                        + "VALUES ('" + s + "', '" + webText.getPageTitle()
                        + "', '" + urlId + "')");
                statement.execute();
            }

            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean dumpUser(User user) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Users (User_Name, User_Email) "
                    + "VALUES ('" + user.getUserName() + "', '" + user.getUserEmail() + "')");
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean dumpKeyWord(KeyWord keyWord) {
        try {
            ResultSet r = stat.executeQuery("SELECT Url_Id FROM Urls WHERE Url_Name='" + keyWord.getWs().getSource() + "'");
            String urlId = "";
            if (r.next()) {
                urlId = r.getString("Url_Id");
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO KeyWords (Key_Word, Url_Id) "
                    + "VALUES ('" + keyWord.getKeyWord() + "', '" + urlId + "')");
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
