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
import org.betaplus.util.SimpleDataSource;

/**
 *
 * @author BenjaminAdamTaylor
 */
@ManagedBean
@RequestScoped
public class RssBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<RSSData> dataList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private RSSData dataItem = new RSSData();
    
    /**
     * Create a linked list containing all the data from the rss table
     * of the database and the url associated with it starting from the most recent, 
     * sorted by the download date in descending order
     * @throws Exception 
     */
    private void loadDataList() throws Exception {
        getConnection();
        dataList.clear();
        ResultSet rs = stat.executeQuery("SELECT rss.*, urls.Rss_Url "
                + "FROM rss, urls WHERE rss.Url_Id=urls.Url_Id "
                + "ORDER BY Dl_Date DESC");
        while(rs.next()) { 
            RSSData rss = new RSSData();
            rss.setRssID(rs.getString("Rss_Id"));
            rss.setFeedTitle(rs.getString("Feed_Title"));
            rss.setFeedDes(rs.getString("Feed_Des"));
            rss.setLinkTitle(rs.getString("Link_Title"));
            rss.setLinkDes(rs.getString("Link_Des"));
            rss.setLinkPubDate(rs.getString("Link_Pub_Date"));
            rss.setLinkLink(rs.getString("Link_Link"));
            rss.setDlDate(rs.getString("Dl_Date").substring(0, 10));
            rss.setUrlID(rs.getString("Url_Id"));
            rss.setUrl(rs.getString("Rss_Url"));
            dataList.add(rss);
        }
    }
    
    /**
     * Save the details of rss object when it is selected in the UI datatable
     */
    public void selectDataItem() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = dataList.get(curIndex-1);
            dataItemId.setValue(dataItem.getRssID());
        }
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
     * Get the list of rss objects from the database
     * @return
     * @throws Exception 
     */
    public List<RSSData> getDataList() throws Exception {
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
     * Get the chosen rss data object
     * @return 
     */
    public RSSData getDataItem() {
        return dataItem;
    }
    
    /**
     * Get the ID of the chosen rss data object
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
