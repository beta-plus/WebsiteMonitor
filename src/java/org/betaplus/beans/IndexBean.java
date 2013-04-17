/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class IndexBean {
    private Connection conn;
    private Statement stat;
    String test = "test";
    
    /**
     * Creates a new instance of IndexBean
     */
    public IndexBean(){
        
    }
    
    public String getPDFChanges() throws Exception
    {
        getConnection();
        int i = 0;
        ResultSet changes = stat.executeQuery("SELECT * FROM pdfs ORDER BY DlDate DESC");
        StringBuilder str = new StringBuilder();
        str.append("<table>");
        while(changes.next())
        {
            String locationDir = changes.getString("LocationDir");
            String name = locationDir.substring(locationDir.lastIndexOf("/") + 1);
            String dlDate = changes.getString("DlDate");
            String date = dlDate.substring(0, 10);
            
            str.append("<tr"); 
            if(i%2==0)
                str.append(" style=\"background-color:#d0d0d0\" ");
            else
                str.append(" style=\"background-color:#f0f0f0\" ");
            str.append(">");
            str.append("<td>").append(name).append("</td>");
            str.append("<td>").append(date).append("</td>");
            str.append("</tr>");
            i++;
        }
        str.append("</table>");
        return str.toString();
    }
    
    public String getHTMLChanges() throws Exception
    {
        getConnection();
        int i = 0;
        ResultSet changes = stat.executeQuery("SELECT urls.Url,html.DlDate FROM urls,html WHERE urls.UrlID = html.UrlId ORDER BY DlDate DESC");
        StringBuilder str = new StringBuilder();
        str.append("<table>");
        while(changes.next())
        {
            String dlDate = changes.getString("DlDate");
            String date = dlDate.substring(0, 10);
            String url = changes.getString("Url");
            
            str.append("<tr"); 
            if(i%2==0)
                str.append(" style=\"background-color:#d0d0d0\" ");
            else
                str.append(" style=\"background-color:#f0f0f0\" ");
            str.append(">");
            str.append("<td>").append(url).append("</td>");
            str.append("<td>").append(date).append("</td>");
            str.append("</tr>");
            i++;
        }
        str.append("</table>");
        return str.toString();
    }
    
    public String getRSSChanges() throws Exception
    {
        getConnection();
        int i = 0;
        ResultSet changes = stat.executeQuery("SELECT * FROM rss ORDER BY DlDate DESC");
        StringBuilder str = new StringBuilder();
        str.append("<table>");
        while(changes.next())
        {
            String dlDate = changes.getString("DlDate");
            String date = dlDate.substring(0, 10);
            String title = changes.getString("LinkTitle");
            
            str.append("<tr"); 
            if(i%2==0)
                str.append(" style=\"background-color:#d0d0d0\" ");
            else
                str.append(" style=\"background-color:#f0f0f0\" ");
            str.append(">");
            str.append("<td>").append(title).append("</td>");
            str.append("<td>").append(date).append("</td>");
            str.append("</tr>");
            i++;
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
