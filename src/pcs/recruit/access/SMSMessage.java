/*
 * Created on Mar 5, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SMSMessage
{
    public static final int SMS_PENDING= 1;
    public static final int SMS_SENT_OK = 2;
    public static final int SMS_ERROR = 3;
    public static final int MAX_LENGTH = 159;
    private int state = SMS_ERROR;
    private String phoneNumber = null;
    private String message = null;
    
    /**
     * @param phoneNumber
     * @param message
     */
    public SMSMessage(String phoneNumber, String message)
    {
        if (phoneNumber == null)
            this.phoneNumber = "";
        else
            this.phoneNumber = phoneNumber;
        
        if (message == null)
            this.message = "";
        else
            this.message = message;
        
        this.state = SMS_PENDING;
    }
    
    public boolean isGoodSMS()
    {
       return(isGoodPhoneNum() && isGoodMessage());
    }
    
    public boolean isGoodPhoneNum()
    {
        if (phoneNumber != null && phoneNumber.length() >= 2)
           return true;
        else
            return false;
    }
    
    public boolean isGoodMessage()
    {
        if (message != null && message.length() >= 1 && message.length() <= MAX_LENGTH)
            return true;
        else
            return false;
    }
    public boolean tryAppendMessage(String newMsg)
    {
        int newLen = getNewLength(newMsg);
        if (newMsg !=null && newMsg.length() > 0 && newLen <= MAX_LENGTH)
        {
            message = message + "\n" + newMsg;
            return true;
        }
        else
            return false;
    }
    
    public int getNewLength(String newMsg)
    {
        int newLength = 0;
        if (message != null)
            newLength = message.length();
        else
            newLength = 0;
        
        if (newMsg != null)
        {
            newLength += newMsg.length() + 1;
        }
        return newLength;

    }
    

    public String toString()
    {
        return ("Phone Number=" + phoneNumber + " Message="+ message + "State=" + state);
    }
    
    public String getMessage() {
        return message;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    
}
