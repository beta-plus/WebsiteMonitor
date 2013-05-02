/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.betaplus.testcases.SimpleDataSource;

/**
 *
 * @author Ben
 */
@ManagedBean
@RequestScoped
public class HtmlBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<HTMLData> changesList = new LinkedList();
    private List<HTMLData> latestList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private HTMLData dataItem = new HTMLData();
    
    
    // Action Methods ----------------------------------------------------------
    private void loadChangesList() throws Exception {
        getConnection();
        changesList.clear();
        ResultSet rs = stat.executeQuery("SELECT html.*, urls.Url, urls.Type "
                + "FROM html, urls WHERE urls.UrlID = html.UrlId "
                + "ORDER BY DlDate DESC");
        while(rs.next()) { 
            HTMLData html = new HTMLData();
            html.setHtmlID(rs.getString(1));
            html.setText(rs.getString(2));
            html.setDlDate(rs.getString(3).substring(0, 10));
            html.setUrlID(rs.getString(4));
            html.setUrl(rs.getString(5));
            html.setType(rs.getString(6));
            changesList.add(html);
        }
    }
    
    private void loadLatestList() throws Exception {
        getConnection();
        latestList.clear();
        ResultSet rs = stat.executeQuery("SELECT html.*, urls.Url, urls.Type "
                + "FROM html, urls "
                + "WHERE urls.Type='html' "
                + "AND urls.UrlId=html.UrlId");
        while(rs.next()) { 
            HTMLData html = new HTMLData();
            html.setHtmlID(rs.getString(1));
            html.setText(rs.getString(2));
            html.setDlDate(rs.getString(3).substring(0, 10));
            html.setUrlID(rs.getString(4));
            html.setUrl(rs.getString(5));
            html.setType(rs.getString(6));
            latestList.add(html);
        }
    }
    
    public void selectChange() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = changesList.get(curIndex-1);
            dataItemId.setValue(dataItem.getHtmlID());
        }
    }
    
    public void selectDocument() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = latestList.get(curIndex-1);
            dataItemId.setValue(dataItem.getHtmlID());
        }
    }
    
    
    // Navigation Methods ------------------------------------------------------
    public void pageFirst() {
        dataTable.setFirst(0);
    }

    public void pagePrevious() {
        dataTable.setFirst(dataTable.getFirst() - dataTable.getRows());
    }

    public void pageNext() {
        dataTable.setFirst(dataTable.getFirst() + dataTable.getRows());
    }

    public void pageLast() {
        int count = dataTable.getRowCount();
        int rows = dataTable.getRows();
        dataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
    }
    
    public int getCurrentPage() {
        int rows = dataTable.getRows();
        int first = dataTable.getFirst();
        int count = dataTable.getRowCount();
        return (count / rows) - ((count - first) / rows) + 1;
    }

    public int getTotalPages() {
        int rows = dataTable.getRows();
        int count = dataTable.getRowCount();
        return (count / rows) + ((count % rows != 0) ? 1 : 0);
    }
    
    // Getter Methods ----------------------------------------------------------
    public List<HTMLData> getChangesList() throws Exception {
        loadChangesList();
        return changesList;
    }
    
    public List<HTMLData> getLatestList() throws Exception {
        loadLatestList();
        return latestList;
    }
    
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    // Setter Methods ----------------------------------------------------------
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    /**
     * Opens a connection to the database
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private void getConnection() throws Exception
    {
        String fp = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");
        SimpleDataSource.init(fp + "data/database.properties");
        
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
