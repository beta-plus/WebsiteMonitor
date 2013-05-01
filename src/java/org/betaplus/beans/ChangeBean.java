/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.betaplus.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ben
 */
@ManagedBean
@SessionScoped
public class ChangeBean implements Serializable{
    private String type;
    
    public void setType(String type)
    { this.type = type; }
    
    public String getType()
    { return type; }
}
