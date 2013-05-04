/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
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
public class RssBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<RSSData> changesList = new LinkedList();
    private List<RSSData> latestList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private RSSData dataItem = new RSSData();
    
    private void loadChangesList() throws Exception {
        getConnection();
        changesList.clear();
        ResultSet rs = stat.executeQuery("SELECT rss.*, urls.Url, urls.Type "
                + "FROM rss, urls WHERE rss.UrlId=urls.UrlId ORDER BY DlDate DESC");
        while(rs.next()) { 
            RSSData rss = new RSSData();
            rss.setRssID(rs.getString(1));
            rss.setFeedTitle(rs.getString(2));
            rss.setFeedDes(rs.getString(3));
            rss.setLinkTitle(rs.getString(4));
            rss.setLinkDes(rs.getString(5));
            rss.setLinkPubDate(rs.getString(6));
            rss.setLinkLink(rs.getString(7));
            rss.setDlDate(rs.getString(8).substring(0, 10));
            rss.setUrlID(rs.getString(9));
            rss.setUrl(rs.getString(10));
            rss.setType(rs.getString(11));
            changesList.add(rss);
        }
    }
    
    private void loadLatestList() throws Exception {
        getConnection();
        latestList.clear();
        ResultSet rs = stat.executeQuery("SELECT rss.*, urls.Url, urls.Type "
                + "FROM rss, urls WHERE rss.UrlId=urls.UrlId GROUP BY rss.UrlId");
        while(rs.next()) { 
            RSSData rss = new RSSData();
            rss.setRssID(rs.getString(1));
            rss.setFeedTitle(rs.getString(2));
            rss.setFeedDes(rs.getString(3));
            rss.setLinkTitle(rs.getString(4));
            rss.setLinkDes(rs.getString(5));
            rss.setLinkPubDate(rs.getString(6));
            rss.setLinkLink(rs.getString(7));
            rss.setDlDate(rs.getString(8).substring(0, 10));
            rss.setUrlID(rs.getString(9));
            rss.setUrl(rs.getString(10));
            rss.setType(rs.getString(11));
            latestList.add(rss);
        }
    }
    
    public void selectChange() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = changesList.get(curIndex-1);
            dataItemId.setValue(dataItem.getRssID());
        }
    }
    
    public void selectDocument() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = latestList.get(curIndex-1);
            dataItemId.setValue(dataItem.getRssID());
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
    public List<RSSData> getChangesList() throws Exception {
        loadChangesList();
        return changesList;
    }
    
    public List<RSSData> getLatestList() throws Exception {
        loadLatestList();
        return latestList;
    }
    
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    public RSSData getDataItem() {
        return dataItem;
    }
    
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
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
