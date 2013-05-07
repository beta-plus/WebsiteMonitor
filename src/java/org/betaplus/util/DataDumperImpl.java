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
        FileInputStream is = null;
        DatabaseGenerator g = new DatabaseGenerator();
        System.out.println("\n\n" +
                  "========================================================\n"
                + "********************************************************\n"
                + "***********TESTING DATA DUMPER METHODS *****************\n"
                + "========================================================\n"
                + "********************************************************\n");
        try {
            DataDumper dd = new DataDumperImpl();
            WebSource ws = new WebSource("http://www.lga.org.mt/lga/content.", "http://www.lga.org.mt/lga/content.aspx?id=92272", "", new LinkedList<String>());
            LinkedList<String> wc = new LinkedList<String>();
            for (int i = 0; i < 10; i++) {
                wc.add("Content " + i);
            }
            File pdf = new File("/Users/StephenJohnRussell/NetBeansProjects/PDFUtility/document_635005030191227226.pdf");
            is = new FileInputStream(pdf);
            boolean test = dd.dumpWebSource(ws);
            boolean test2 = dd.dumpUser(new User("Stephen Russell", "stephenjohnrussell@me.com"));
            boolean test3 = dd.dumpWebText(new WebText(wc, "Content Test", ws));
            boolean test4 = dd.dumpKeyWord(new KeyWord("file_provider", ws));
            boolean test5 = dd.dumpKeyWord(new KeyWord("pdf", ws));
            boolean test6 = dd.dumpPDF(new Pdf(DigestUtils.md5Hex(is), "LGA633570702574217500", pdf, ws));
            System.out.println("Test Add WebSoource \t:" + test);
            System.out.println("Test Add User \t\t:" + test2);
            System.out.println("Test Add WebText \t:" + test3);
            System.out.println("Test Add Key Word \t:" + test4);
            System.out.println("Test Add Key Word \t:" + test5);
            System.out.println("Test Add Pdf \t\t:" + test6);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(DataDumperImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
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
