/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.betaplus.testcases.SimpleDataSource;

/**
 *
 * @author Ben
 */
@ManagedBean
@RequestScoped
public class UrlListBean {
    private Connection conn;
    private Statement stat;
    String test = "test";
    
    /**
     * Creates a new instance of UrlListBean
     */
    public UrlListBean() {
    }
    
    public String getURL() throws Exception
    {
        getConnection();
        StringBuilder str = new StringBuilder();
        ResultSet urls = stat.executeQuery("SELECT * FROM urls");
        
        ResultSetMetaData rsmd = urls.getMetaData();
        
        str.append("<table>");
        str.append("<tr>");
        str.append("<th>").append(rsmd.getColumnName(1)).append("</th>");
        str.append("<th>").append(rsmd.getColumnName(2)).append("</th>");
        str.append("<th>").append(rsmd.getColumnName(3)).append("</th>");
        str.append("</tr>");
        while(urls.next())
        {
            String urlID = urls.getString(1);
            String url = urls.getString(2);
            String type = urls.getString(3);
            
            str.append("<tr>");
            str.append("<td>").append(urlID).append("</td>");
            str.append("<td>").append(url).append("</td>");
            str.append("<td>").append(type).append("</td>");
            str.append("</tr>");
        }
        str.append("</table>");
        return str.toString();
    }
    
    /**
     * Opens a connection to the database
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private void getConnection() throws Exception
    {
        // Need absolute path if running directly in Glassfish, can use relative if deployed within a WAR
        
        // Ben's absolute path
        //SimpleDataSource.init("C:\\Users\\Ben\\Desktop\\Uni\\Software Hut\\"
        //        + "Project\\WebsiteMonitor\\data\\database.properties");
        
        // James's absolute path
        SimpleDataSource.init("/Users/Jay/Documents/Documents/University Work"
                + "/Year 02/Year 02 - Semester 02/NetBeans Projects/Software Hut"
                + "/Website Monitor/data/database.properties");
        
        // Steve's absolute path
        //SimpleDataSource.init("");
        
        // Relative path
        //SimpleDataSource.init("/WEB-INF/database.properties");
        
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
