package org.betaplus.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
public class KeywordBean {

    @ManagedProperty(value = "#{urlListBean}")
    private UrlListBean urlListBean;
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<KeywordData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private KeywordData dataItem = new KeywordData();

    /**
     * Create a linked list containing all the data from the keywords table
     * where the url ID matches a specified url ID
     * @throws Exception 
     */
    private void loadDataList() throws Exception {
        getConnection();
        // Cleaar existing data
        dataList.clear();
        // Get the specified UrlID from the urlListBean
        String urlID = urlListBean.getDataItem().getUrlID();
        // Get the data from the database
        ResultSet rs = stat.executeQuery("SELECT * FROM keywords "
                + "WHERE Url_Id='" + urlID + "'");
        while (rs.next()) {
            // Create a new keyword object for each record
            KeywordData keyword = new KeywordData();
            keyword.setKeywordID(rs.getString("Key_Word_Id"));
            keyword.setKeyword(rs.getString("Key_Word"));
            keyword.setUrlID(rs.getString("Url_Id"));
            // Add the object to the list
            dataList.add(keyword);
        }
    }

    /**
     * Save the details of keyword object when it is selected in the UI datatable
     */
    public void selectDataItem() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            // Get the keyword data item from the linked list
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = dataList.get(curIndex - 1);
            dataItemId.setValue(dataItem.getKeywordID());
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
        // Update the record with the corresponding keyword ID
        stat.execute("UPDATE keywords SET"
                + " Key_Word='" + dataItem.getKeyword() + "',"
                + " Url_Id='" + dataItem.getUrlID() + "' "
                + " WHERE Key_Word_Id='" + dataItem.getKeywordID() + "'");
        // Clear the data stored in the current data object
        clearDataItem();
    }

    /**
     * Create a new record in the database
     * @throws Exception 
     */
    public void newDataItem() throws Exception {
        getConnection();
        // Create a new record using the values given by the user
        stat.execute("INSERT INTO keywords (Key_Word_Id, Url_Id) "
                + "VALUES ('" + dataItem.getKeyword() + "', '"
                + dataItem.getUrlID() + "')");
        // Clear the data stored in the current data object
        clearDataItem();
    }

    /**
     * Delete the currently selected record from the database
     * @throws Exception 
     */
    public void deleteSelectedItems() throws Exception {
        getConnection();

        dataItem.setUrlID(dataItemId.getValue().toString());

        stat.execute("DELETE FROM keywords WHERE Key_Word_Id='"
                + dataItem.getKeywordID() + "'");
        clearDataItem();
    }

    /**
     * Clear any data stored in the current keyword data item
     */
    public void clearDataItem() {
        dataItem = new KeywordData();
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
     * Get the list of keyword objects from the database
     * @return
     * @throws Exception 
     */
    public List<KeywordData> getDataList() throws Exception {
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
     * Get the chosen keyword data object
     * @return 
     */
    public KeywordData getDataItem() {
        return dataItem;
    }

    /**
     * Get the ID of the chosen keyword data object
     * @return 
     */
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
    }

    /**
     * Get the url list bean
     * @return 
     */
    public UrlListBean getUrlListBean() {
        return urlListBean;
    }

    /**
     * Set the html datatable
     * @param dataTable 
     */
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }

    /**
     * Set the currently selected data item
     * @param dataItem 
     */
    public void setDataItem(KeywordData dataItem) {
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
     * Set the url list bean object
     * @param urlListBean 
     */
    public void setUrlListBean(UrlListBean urlListBean) {
        this.urlListBean = urlListBean;
    }

    /**
     * Opens a connection to the database
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void getConnection() throws Exception {
        String fp = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");
        SimpleDataSource.init(fp + "data/database.properties");

        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
