/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.betaplus.testcases.SimpleDataSource;

/**
 *
 * @author StephenJohnRussell
 */
public class DataRetrieverImpl implements DataRetriever {

    private Connection conn;
    private Statement stat;

    /**
     *
     */
    public DataRetrieverImpl() {
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
    public ResultSet getTable(String tableName) {
        try {
            return stat.executeQuery("SELECT * FROM " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(DataRetrieverImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ResultSet getLatestTable(String tableName) {
        ResultSet r = null;
        try {
            return stat.executeQuery("SELECT * FROM " + tableName + " ORDER BY Dl_Date DESC");
        } catch (SQLException ex) {
            Logger.getLogger(DataRetrieverImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    @Override
    public ResultSet getTableWhere(String tableName, String columnName, String columnValue) {
        ResultSet r = null;
        try {
            return stat.executeQuery("SELECT * FROM " + tableName + " WHERE " + columnName + " ='" + columnValue + "'");
        } catch (SQLException ex) {
            Logger.getLogger(DataRetrieverImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            DataRetrieverImpl dr = new DataRetrieverImpl();
            ResultSet r = dr.getLatestTable("Html");
            while(r.next()) {
                String s = "";
                for (int i = 0; i < 5; i++) {
                    s = s + r.getString(i) + "\t|\t";
                }
                System.out.println(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataRetrieverImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
