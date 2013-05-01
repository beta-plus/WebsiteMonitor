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
    private HtmlDataTable dataTable;
    private List<URLData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private URLData dataItem = new URLData();
  
    // Action Methods ----------------------------------------------------------
    private void loadDataList() throws Exception {
        getConnection();
        dataList.clear();
        ResultSet rs = stat.executeQuery("SELECT * FROM urls");
        while(rs.next()) { 
            URLData url = new URLData();
            url.setUrlID(rs.getString(1));
            url.setUrl(rs.getString(2));
            url.setType(rs.getString(3));
            dataList.add(url);
        }
    }
    
    public void selectDataItem() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = dataList.get(curIndex-1);
            dataItemId.setValue(dataItem.getUrlID());
        }
    }
    
    public void updateDataItem() throws Exception {
        getConnection();
        // Retain the ID of the data item from hidden input element.
        dataItem.setUrlID(dataItemId.getValue().toString());

        stat.execute("UPDATE urls SET"
                + " Url='" + dataItem.getUrl() + "',"
                + " Type='" + dataItem.getType() + "' "
                + " WHERE UrlId='" + dataItem.getUrlID() + "'");
        clearDataItem();
    }
    
    public void newDataItem() throws Exception {
        getConnection();

        stat.execute("INSERT INTO urls (Url, Type) VALUES ('"
                + dataItem.getUrl() + "', '"
                + dataItem.getType() + "')");
        clearDataItem();
    }
    
    public void deleteSelectedItems() throws Exception {
        getConnection();
        
        dataItem.setUrlID(dataItemId.getValue().toString());
        
        stat.execute("DELETE FROM urls WHERE UrlId='" 
            + dataItem.getUrlID() + "'");
        clearDataItem();
    }
    
    public void clearDataItem() {
        dataItem = new URLData();
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
    public List<URLData> getDataList() throws Exception {
        loadDataList();
        return dataList;
    }
    
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    public URLData getDataItem() {
        return dataItem;
    }
    
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
    }
    
    // Setter methods ----------------------------------------------------------
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    public void setDataItem(URLData dataItem) {
        this.dataItem = dataItem;
    }
    
    public void setDataItemId(HtmlInputHidden dataItemId) {
        this.dataItemId = dataItemId;
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
        SimpleDataSource.init("C:\\Users\\Ben\\Desktop\\Uni\\Software Hut\\"
                + "Project\\WebsiteMonitor\\data\\database.properties");
        
        // James's absolute path
        //SimpleDataSource.init("/Users/Jay/Documents/Documents/University Work"
        //        + "/Year 02/Year 02 - Semester 02/NetBeans Projects/Software Hut"
        //        + "/Website Monitor/data/database.properties");
        
        // Steve's absolute path
        //SimpleDataSource.init("");
        
        // Relative path
        //SimpleDataSource.init("/WEB-INF/database.properties");
        
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
