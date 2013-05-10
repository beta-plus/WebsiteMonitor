package org.betaplus.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.betaplus.testcases.SimpleDataSource;

/**
 *
 * @author BenjaminAdamTaylor
 */
@ManagedBean
@SessionScoped
public class UrlListBean implements Serializable{
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<URLData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private URLData dataItem = new URLData();
  
    /**
     * Create a linked list containing all the data from the urls table
     * @throws Exception 
     */
    private void loadDataList() throws Exception {
        getConnection();
        dataList.clear();
        ResultSet rs = stat.executeQuery("SELECT * FROM urls");
        while(rs.next()) { 
            URLData url = new URLData();
            url.setUrlID(rs.getString("Url_Id"));
            url.setRssUrl(rs.getString("Rss_Url"));
            url.setHttpUrl(rs.getString("Http_Url"));
            url.setUrlName(rs.getString("Url_Name"));
            dataList.add(url);
        }
    }
    
    /**
     * Save the details of user object when it is selected in the UI datatable
     */
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
    
    /**
     * Update the selected record in the database
     * @throws Exception 
     */
    public void updateDataItem() throws Exception {
        getConnection();
        // Retain the ID of the data item from hidden input element.
        dataItem.setUrlID(dataItemId.getValue().toString());
        
        stat.execute("UPDATE urls SET"
                + " Rss_Url='" + dataItem.getRssUrl() + "',"
                + " Http_Url='" + dataItem.getHttpUrl() + "',"
                + " Url_Name='" + dataItem.getUrlName() + "' "
                + " WHERE Url_Id='" + dataItem.getUrlID() + "'");
        clearDataItem();
    }
    
    /**
     * Create a new record in the database
     * @throws Exception 
     */
    public void newDataItem() throws Exception {
        getConnection();
        
        stat.execute("INSERT INTO urls (Rss_Url, Http_Url, Url_Name) VALUES ('"
            + dataItem.getRssUrl() + "', '" 
            + dataItem.getHttpUrl() + "', '"
            + dataItem.getUrlName() + "')");
        
        clearDataItem();
    }
    
    /**
     * Delete the currently selected record from the database
     * @throws Exception 
     */
    public void deleteSelectedItems() throws Exception {
        getConnection();
        
        dataItem.setUrlID(dataItemId.getValue().toString());
        
        stat.execute("DELETE FROM urls WHERE Url_Id='" 
            + dataItem.getUrlID() + "'");
        clearDataItem();
    }
    
    /**
     * Clear any data stored in the current url data item
     */
    public void clearDataItem() {
        dataItem = new URLData();
    }
    
    /**
     * Navigate to the front page of the datatable in the UI
     */
    public void pageFirst() {
        dataTable.setFirst(0);
    }

    /**
     * Navigate to the previous page of the datatable in the UI
     */
    public void pagePrevious() {
        dataTable.setFirst(dataTable.getFirst() - dataTable.getRows());
    }

    /**
     * Navigate to the next page of the datatable in the UI
     */
    public void pageNext() {
        dataTable.setFirst(dataTable.getFirst() + dataTable.getRows());
    }

    /**
     * Navigate to the last page of the datatable in the UI
     */
    public void pageLast() {
        int count = dataTable.getRowCount();
        int rows = dataTable.getRows();
        dataTable.setFirst(count - ((count % rows != 0) ? count % rows : rows));
    }
    
    /**
     * Get the current page of the datatable in the UI
     * @return 
     */
    public int getCurrentPage() {
        int rows = dataTable.getRows();
        int first = dataTable.getFirst();
        int count = dataTable.getRowCount();
        return (count / rows) - ((count - first) / rows) + 1;
    }

    /**
     * Get the total pages of the datatable in the UI
     * @return 
     */
    public int getTotalPages() {
        int rows = dataTable.getRows();
        int count = dataTable.getRowCount();
        return (count / rows) + ((count % rows != 0) ? 1 : 0);
    }
    
    /**
     * Get the list of url objects from the database
     * @return
     * @throws Exception 
     */
    public List<URLData> getDataList() throws Exception {
        loadDataList();
        return dataList;
    }
    
    /**
     * Get the html datatable
     * @return 
     */
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    /**
     * Get the chosen url data object
     * @return 
     */
    public URLData getDataItem() {
        return dataItem;
    }
    
    /**
     * Get the ID of the chosen url data object
     * @return 
     */
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
    }
    
    /**
     * Set the html datatable
     * @param dataTable 
     */
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    /**
     * Set the currently selected url data item
     * @param dataItem 
     */
    public void setDataItem(URLData dataItem) {
        this.dataItem = dataItem;
    }
    
    /**
     * Set the currently selected data item id
     * @param dataItemId 
     */
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
        String fp = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");
        SimpleDataSource.init(fp + "data/database.properties");
        
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
