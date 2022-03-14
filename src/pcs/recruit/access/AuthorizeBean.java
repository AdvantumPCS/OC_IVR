/*
 * Created on Jan 26, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.Date;
import java.sql.Time;



/**
 * @author Orville
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

public class AuthorizeBean
{
    private long callSequence = -1;
    private long requestSequence = -1;
    private int manNumber = -1;
    private int pinNumber = -1;
    private String phoneNumber = null;
    private java.sql.Date requestDate = null;
    private java.sql.Time requestTime = null;

 /*   public static final String DATE_LOG_FORMAT = "ddMMYYYY";

    public static final String TIME_LOG_FORMAT = "HHmmss";
*/
    public boolean isLoggable() {
        if (callSequence > 0 && requestSequence > 0 && manNumber > 0
                && pinNumber > 0 && requestDate != null && requestTime != null)
            return true;
        else
            return false;
    }

/*    public int getLogRequestDate() {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(DATE_LOG_FORMAT);
            String tempDate = df.format(requestDate);

            int tempInt = Integer.parseInt(tempDate);
            return (tempInt);
        } catch (Exception e)
        {
            return (0);
            /* **Log a Format Exception error         }
    }
*/    
    
    /*
    public int getLogRequestTime() {
        try
        {
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(TIME_LOG_FORMAT);
            String tempTime = df.format(requestTime);

            int tempInt = Integer.parseInt(tempTime);
            return (tempInt);
        } catch (Exception e)
        {
            return (0);
            /* **Log a Format Exception error 
        }
    }
*/
    /**
     * @param callSequence
     * @param requestSequence
     * @param manNumber
     * @param pinNumber
     * @param phoneNumber
     * @param requestDate
     * @param requestTime
     */
    public AuthorizeBean(long callSequence, long requestSequence,
            int manNumber, int pinNumber, String phoneNumber,
            java.sql.Date requestDate, java.sql.Time requestTime)
    {
        this.callSequence = callSequence;
        this.requestSequence = requestSequence;
        this.manNumber = manNumber;
        this.pinNumber = pinNumber;
        this.phoneNumber = phoneNumber;
        this.requestDate = requestDate;
        this.requestTime = requestTime;
    }

    
    public AuthorizeBean(long callSequence, long requestSequence,
            int manNumber, int pinNumber, String phoneNumber)
    {
        this.callSequence = callSequence;
        this.requestSequence = requestSequence;
        this.manNumber = manNumber;
        this.pinNumber = pinNumber;
        this.phoneNumber = phoneNumber;
        this.requestDate = new Date(System.currentTimeMillis());
        this.requestTime = new Time(System.currentTimeMillis());
    }    
    
    /**
     * @return Returns the phoneNumber.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return Returns the pinNumber.
     */
    public int getPinNumber() {
        return pinNumber;
    }

    /**
     * @return Returns the requestDate.
     */
    public java.sql.Date getRequestDate() {
        return requestDate;
    }

    /**
     * @return Returns the requestSequence.
     */
    public long getRequestSequence() {
        return requestSequence;
    }

    /**
     * @return Returns the requestTime.
     */
    public java.sql.Time getRequestTime() {
        return requestTime;
    }

    public long getCallSequence() {
        return callSequence;
    }

    public int getManNumber() {
        return manNumber;
    }

    public String toString() {
        return ("callSequence = " + callSequence + " requestSequence = "
                + requestSequence + " manNumber = " + manNumber
                + " pinNumber = " + pinNumber + " phoneNumber =" + phoneNumber
                + " requestDate = " + requestDate + " requestTime = " + requestTime);
    }
}
