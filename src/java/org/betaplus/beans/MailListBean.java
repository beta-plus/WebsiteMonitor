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
public class MailListBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<UserData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private UserData dataItem = new UserData();
    
    
    // Action Methods ----------------------------------------------------------
    private void loadDataList() throws Exception {
        getConnection();
        dataList.clear();
        ResultSet rs = stat.executeQuery("SELECT * FROM users");
        while(rs.next()) { 
            UserData user = new UserData();
            user.setUserID(rs.getString(1));
            user.setUserName(rs.getString(2));
            user.setUserEmail(rs.getString(3));
            dataList.add(user);
        }
    }
    
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
    
    public void updateDataItem() throws Exception {
        getConnection();
        // Retain the ID of the data item from hidden input element.
        dataItem.setUserID(dataItemId.getValue().toString());

        stat.execute("UPDATE users SET"
                + " UserName='" + dataItem.getUserName() + "',"
                + " UserEmail='" + dataItem.getUserEmail() + "' "
                + " WHERE UserId='" + dataItem.getUserID() + "'");
        clearDataItem();
    }
    
    public void newDataItem() throws Exception {
        getConnection();

        stat.execute("INSERT INTO users (UserName, UserEmail) VALUES ('"
                + dataItem.getUserName() + "', '"
                + dataItem.getUserEmail() + "')");
        clearDataItem();
    }
    
    public void deleteSelectedItems() throws Exception {
        getConnection();
        
        dataItem.setUserID(dataItemId.getValue().toString());
        
        stat.execute("DELETE FROM users WHERE UserId='" 
            + dataItem.getUserID() + "'");
        clearDataItem();
    }
    
    public void clearDataItem() {
        dataItem = new UserData();
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
    public List<UserData> getDataList() throws Exception {
        loadDataList();
        return dataList;
    }
    
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    public UserData getDataItem() {
        return dataItem;
    }
    
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
    }
    
    // Setter Methods ----------------------------------------------------------
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    public void setDataItem(UserData dataItem) {
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
        String fp = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/");
        SimpleDataSource.init(fp + "data/database.properties");
        
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }
}
