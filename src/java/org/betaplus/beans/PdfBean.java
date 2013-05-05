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
public class PdfBean {
    private Connection conn;
    private Statement stat;
    private HtmlDataTable dataTable;
    private List<PDFData> changesList = new LinkedList();
    private List<PDFData> latestList = new LinkedList();
    private HtmlInputHidden dataItemId = new HtmlInputHidden();
    private PDFData dataItem = new PDFData();
    
    // Action Methods ----------------------------------------------------------
    private void loadChangesList() throws Exception {
        getConnection();
        changesList.clear();
        ResultSet rs = stat.executeQuery("SELECT pdfs.*, urls.Url, urls.Type "
                + "FROM pdfs, urls WHERE urls.UrlId=pdfs.UrlId "
                + "ORDER BY DlDate DESC");
        while(rs.next()) { 
            PDFData pdf = new PDFData();
            pdf.setPdfID(rs.getString(1));
            pdf.setLocationDir(rs.getString(2));
            pdf.setDlDate(rs.getString(3).substring(0, 10));
            pdf.setUrlID(rs.getString(4));
            pdf.setUrl(rs.getString(5));
            pdf.setType(rs.getString(6));
            pdf.setPdfName(rs.getString(2).substring(rs.getString(2).lastIndexOf("/") + 1));
            changesList.add(pdf);
        }
    }
    
    private void loadLatestList() throws Exception {
        getConnection();
        latestList.clear();
        ResultSet rs = stat.executeQuery("SELECT t.*, urls.Url, urls.Type FROM pdfs t, urls "
                + "WHERE DlDate = (SELECT MAX(DlDate) FROM pdfs x WHERE "
                + "x.UrlId = t.UrlId) "
                + "AND t.UrlId=urls.UrlId ORDER BY DlDate DESC");
        while(rs.next()) {
            PDFData pdf = new PDFData();
            pdf.setPdfID(rs.getString(1));
            pdf.setLocationDir(rs.getString(2));
            pdf.setDlDate(rs.getString(3).substring(0, 10));
            pdf.setUrlID(rs.getString(4));
            pdf.setUrl(rs.getString(5));
            pdf.setType(rs.getString(6));
            pdf.setPdfName(rs.getString(2).substring(rs.getString(2).lastIndexOf("/") + 1));
            latestList.add(pdf);
        }
    }
    
    public void selectChange() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = changesList.get(curIndex-1);
            dataItemId.setValue(dataItem.getPdfID());
        }
    }
    
    public void selectDocument() {
        // Obtain the row index from the hidden input element.
        String rowIndex = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestParameterMap().get("rowIndex");
        if (rowIndex != null && rowIndex.trim().length() != 0) {
            int curIndex = dataTable.getFirst() + Integer.parseInt(rowIndex);
            dataItem = latestList.get(curIndex-1);
            dataItemId.setValue(dataItem.getPdfID());
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
    public List<PDFData> getChangesList() throws Exception {
        loadChangesList();
        return changesList;
    }
    
    public List<PDFData> getLatestList() throws Exception {
        loadLatestList();
        return latestList;
    }
    
    public HtmlDataTable getDataTable() {
        return dataTable;
    }
    
    public PDFData getDataItem() {
        return dataItem;
    }
    
    public HtmlInputHidden getDataItemId() {
        return dataItemId;
    }
    
    // Setter Methods ----------------------------------------------------------
    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    public void setDataItem(PDFData dataItem) {
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
