package org.betaplus.beans;

/**
 *
 * @author BenjaminAdamTaylor
 */
public class UserData 
{
    private String userID;
    private String userName;
    private String userEmail;
    
    /**
     * Set the user ID
     * @param userID 
     */
    public void setUserID(String userID)
    { this.userID = userID; }
    
    /**
     * Set the user name
     * @param userName 
     */
    public void setUserName(String userName)
    { this.userName = userName; }
    
    /**
     * Set the user email
     * @param userEmail 
     */
    public void setUserEmail(String userEmail)
    { this.userEmail = userEmail; }
    
    /**
     * Get the user ID
     * @return 
     */
    public String getUserID()
    { return userID; }
    
    /**
     * Get the user name
     * @return 
     */
    public String getUserName()
    { return userName; }
    
    /**
     * Get the user email
     * @return 
     */
    public String getUserEmail()
    { return userEmail; }
}
