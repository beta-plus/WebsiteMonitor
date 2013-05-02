package org.betaplus.beans;

/*
	Title: UserData
	Author: Ben Taylor
	Date: May 1, 2013
	Version: 1.0
*/

public class UserData 
{
    private String userID;
    private String userName;
    private String userEmail;
    
    public void setUserID(String userID)
    { this.userID = userID; }
    
    public void setUserName(String userName)
    { this.userName = userName; }
    
    public void setUserEmail(String userEmail)
    { this.userEmail = userEmail; }
    
    public String getUserID()
    { return userID; }
    
    public String getUserName()
    { return userName; }
    
    public String getUserEmail()
    { return userEmail; }
}
