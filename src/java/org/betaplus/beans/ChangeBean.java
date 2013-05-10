package org.betaplus.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author BenjaminAdamTaylor
 */
@ManagedBean
@SessionScoped
public class ChangeBean implements Serializable {
    private String type;
    private PDFData pdf = new PDFData();
    
    /**
     * Set the type of data (PDF/RSS/HTML)
     * @param type 
     */
    public void setType(String type)
    { this.type = type; }
    
    public void setPdf(PDFData pdf)
    { this.pdf = pdf; }
    
    /**
     * Get the type of data (PDF/RSS/HTML)
     * @return type
     */
    public String getType()
    { return type; }
    
    public PDFData getPdf()
    { return pdf; }
}
