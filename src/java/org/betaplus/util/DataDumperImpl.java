/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.betaplus.core.WebsiteMonitor;
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

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        DatabaseGenerator g = new DatabaseGenerator();
        System.out.println("\n"
                + "========================================================\n"
                + "********************************************************\n"
                + "***********TESTING DATA DUMPER METHODS *****************\n"
                + "********************************************************\n"
                + "========================================================\n");

        DataDumper dd = new DataDumperImpl();
        String[] urls = {"http://www.gov.im/gambling/regulatory.xml", "http://regulations.porezna-uprava.hr/PrikaziPropis.asp?file=agc.XML&ime=Act%20on%20Games%20of%20Chance&idAktualni=1098", "http://www.lga.org.mt/lga/content.aspx?id=92272", };
        String[] sources = {"http://www.gov.im/gambling/", "http://regulations.porezna-uprava.hr/", "http://www.lga.org.mt/lga/content"};
        String[] rssSource = {"www.gov.im/rssnews.gov", "test.xml", "test.xml"};
        for (int i = 0; i < urls.length; i++) {
            WebSource ws = new WebSource(sources[i], urls[i], rssSource[i], i);
            dd.dumpWebSource(ws);
            if (i == 2) {
                dd.dumpKeyWord(new KeyWord("file_provider", ws));
            }
            dd.dumpKeyWord(new KeyWord(".pdf", ws));
        }
        dd.dumpUser(new User("James Finney", "eeue3c@bangor.ac.uk"));
        dd.dumpUser(new User("Stephen Russell", "eeue0f@bangor.ac.uk"));        
    }

    /**
     *
     */
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
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Pdfs (Pdf_Hash, Url_Id, Pdf_File, Pdf_Name, Pdf_Url) "
                    + "VALUES ('" + pdf.getPdfHash() + "', '" + urlId + ""
                    + "', '" + b + "', '" + pdf.getPdfName() + "', '" + pdf.getPdfUrl() + "')");
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
        try {
            ResultSet r = stat.executeQuery("SELECT Url_Id FROM Urls WHERE Url_Name='" + rss.getWs().getSource() + "'");
            String urlId = "";
            if (r.next()) {
                urlId = r.getString("Url_Id");
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Rss (Feed_Title, Feed_Des, Link_Title, Link_Des, Link_Pub_Date, Link_Link, Url_Id) "
                    + "VALUES ('" + rss.getFeedTitle() + "', '" + rss.getFeedDes()
                    + "', '" + rss.getLinkTitle() + "', '" + rss.getLinkDes()
                    + "', '" + rss.getLinkPubDate() + "', '" + rss.getLinkLink()
                    + "', '" + urlId + "')");
            statement.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean dumpWebText(WebText webText) {
        try {
            ResultSet r = stat.executeQuery("SELECT Url_Id FROM Urls WHERE Url_Name='" + webText.getWe().getSource() + "'");
            String urlId = "";
            if (r.next()) {
                urlId = r.getString("Url_Id");
            }
            String s = webText.getPageContent();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '\'') {
                    s = s.substring(0, i - 1) + "\\" + s.substring(i);
                }
            }
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Html (Html_Content, Html_Name, Html_Url, Url_Id) "
                    + "VALUES ('" + s + "', '" + webText.getPageTitle() + "', '" + webText.getPageUrl()
                    + "', '" + urlId + "')");
            statement.execute();


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
    
    
    private static String getHash(File f) {
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            return DigestUtils.md5Hex(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (IOException ex) {
            Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(WebsiteMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
