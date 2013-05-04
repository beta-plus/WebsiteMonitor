/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ben
 */
@ManagedBean
@SessionScoped
public class ChangeBean implements Serializable {
    private String type;
    private PDFData pdf = new PDFData();
    
    public void setType(String type)
    { this.type = type; }
    
    public void setPdf(PDFData pdf)
    { this.pdf = pdf; }
    
    public String getType()
    { return type; }
    
    public PDFData getPdf()
    { return pdf; }
}
