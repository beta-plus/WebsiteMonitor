/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.util;

import java.sql.ResultSet;

/**
 *
 * @author StephenJohnRussell
 */
public interface DataRetriever {
    
    /**
     * Returns Name, Url & id.
     * @param tableName
     * @return 
     */
    public ResultSet getTable(String tableName);
    
    /**
     * Returns Name, Url & id.
     * @param tableName
     * @return 
     */
    public ResultSet getLatestTable(String tableName);
    
    /**
     * Returns RS where column name = column value.
     * @param tableName
     * @param columnName
     * @param columnValue
     * @return 
     */
    public ResultSet getTableWhere(String tableName, String columnName, String columnValue);
    
}
