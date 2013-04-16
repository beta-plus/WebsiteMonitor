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
public class MailListBean {
    private Connection conn;
    private Statement stat;
    String test = "test";
    
    /**
     * Creates a new instance of MailListBean
     */
    public MailListBean() {
    }
    
    public String getUsers() throws Exception
    {
        getConnection();
        StringBuilder str = new StringBuilder();
        ResultSet users = stat.executeQuery("SELECT * FROM users");
        
        ResultSetMetaData rsmd = users.getMetaData();
        
        str.append("<table>");
        str.append("<tr>");
        str.append("<th>").append(rsmd.getColumnName(1)).append("</th>");
        str.append("<th>").append(rsmd.getColumnName(2)).append("</th>");
        str.append("<th>").append(rsmd.getColumnName(3)).append("</th>");
        str.append("</tr>");
        while(users.next())
        {
            String urlID = users.getString(1);
            String url = users.getString(2);
            String type = users.getString(3);
            
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
        SimpleDataSource.init("C:\\Users\\Ben\\Desktop\\Uni\\Software Hut\\"
                + "Project\\WebsiteMonitor\\data\\database.properties");
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
