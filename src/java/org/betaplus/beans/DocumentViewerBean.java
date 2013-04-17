/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.sql.Connection;
import java.sql.ResultSet;
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
public class DocumentViewerBean {
    private Connection conn;
    private Statement stat;
    String test = "test";
    
    /**
     * Creates a new instance of DocumentViewerBean
     */
    public DocumentViewerBean() {
    }
    
    public String getPDF() throws Exception
    {
        getConnection();
        ResultSet changes = stat.executeQuery("SELECT * FROM pdfs t "
                + "WHERE DlDate = (SELECT MAX(DlDate) FROM pdfs x WHERE "
                + "x.UrlId = t.UrlId) ORDER BY DlDate DESC");
        StringBuilder str = new StringBuilder();
        str.append("<table>");
        while(changes.next())
        {
            String locationDir = changes.getString("LocationDir");
            String name = locationDir.substring(locationDir.lastIndexOf("/") + 1);
            
            str.append("<tr>");
            str.append("<td>").append(name).append("</td>");
            str.append("</tr>");
        }
        str.append("</table>");
        return str.toString();
    }
    
    public String getHTML() throws Exception
    {
        getConnection();
        ResultSet changes = stat.executeQuery("SELECT Url FROM urls WHERE Type='html'");
        StringBuilder str = new StringBuilder();
        str.append("<table>");
        while(changes.next())
        {
            String url = changes.getString("Url");
            
            str.append("<tr>");
            str.append("<td>").append(url).append("</td>");
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
