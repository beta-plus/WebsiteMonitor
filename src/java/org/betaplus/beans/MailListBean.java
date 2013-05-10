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
public class MailListBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<UserData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private UserData dataItem = new UserData();
    
    /**
     * Create a linked list containing all the data from the users table
     * @throws Exception 
     */
    private void loadDataList() throws Exception {
        getConnection();
        dataList.clear();
        ResultSet rs = stat.executeQuery("SELECT * FROM users");
        while(rs.next()) { 
            UserData user = new UserData();
            user.setUserID(rs.getString("User_Id"));
            user.setUserName(rs.getString("User_Name"));
            user.setUserEmail(rs.getString("User_Email"));
            dataList.add(user);
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
            dataItemId.setValue(dataItem.getUserID());
        }
    }
    
    /**
     * Update the selected record in the database
     * @throws Exception 
     */
    public void updateDataItem() throws Exception {
        getConnection();
        // Retain the ID of the data item from hidden input element.
        dataItem.setUserID(dataItemId.getValue().toString());

        stat.execute("UPDATE users SET"
                + " User_Name='" + dataItem.getUserName() + "',"
                + " User_Email='" + dataItem.getUserEmail() + "' "
                + " WHERE User_Id='" + dataItem.getUserID() + "'");
        clearDataItem();
    }
    
    /**
     * Create a new record in the database
     * @throws Exception 
     */
    public void newDataItem() throws Exception {
        getConnection();

        stat.execute("INSERT INTO users (User_Name, User_Email) VALUES ('"
                + dataItem.getUserName() + "', '"
                + dataItem.getUserEmail() + "')");
        
        
        clearDataItem();
    }
    
    /**
     * Delete the currently selected record from the database
     * @throws Exception 
     */
    public void deleteSelectedItems() throws Exception {
        getConnection();
        
        dataItem.setUserID(dataItemId.getValue().toString());
        
        stat.execute("DELETE FROM users WHERE User_Id='" 
            + dataItem.getUserID() + "'");
        clearDataItem();
    }
    
    /**
     * Clear any data stored in the current user data item
     */
    public void clearDataItem() {
        dataItem = new UserData();
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
     * Get the list of user objects from the database
     * @return
     * @throws Exception 
     */
    public List<UserData> getDataList() throws Exception {
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
     * Get the chosen user data object
     * @return 
     */
    public UserData getDataItem() {
        return dataItem;
    }
    
    /**
     * Get the ID of the chosen user data object
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
     * Set the currently selected user data item
     * @param dataItem 
     */
    public void setDataItem(UserData dataItem) {
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
