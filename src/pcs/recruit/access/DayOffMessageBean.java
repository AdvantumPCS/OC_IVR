/*
 * Created on Jan 27, 2009
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DayOffMessageBean extends MessageBean
{
    String daysOff[];

    /**
     * @param callSequence
     * @param requestSequence
     * @param responseSequence
     * @param messageId
     * @param manNumber
     * @param responseDate
     * @param responseTime
     */
    public DayOffMessageBean(long callSequence, int requestSequence,
            int responseSequence, int messageId, int manNumber, String pcsPhone,
            Date responseDate, Time responseTime, String daysOff[])
    {
        super(callSequence, requestSequence, responseSequence, messageId,
                manNumber,pcsPhone, responseDate, responseTime);
        this.daysOff = daysOff;
        // TODO Auto-generated constructor stub
    }

    public String[] getDaysOff() {
        return daysOff;
    }
    
    
    public boolean isValidMessage()
    {
        if (super.isValidMessage() &&
                isValidDayOffMsgType()&& isDayOffExist())
            return true;
        else
            return false;
    }
    public boolean isValidDayOffMsgType()
    {
        
        return super.isDayOffMessage(super.getMessageId());
    }
    public boolean isDayOffExist()
    {
        boolean retVal = false;
        if (daysOff != null)
            if (daysOff.length >= 1)
                retVal = true;	
        return true;
    }
    public boolean isDayOffThisWeek()
    {
        if (messageId == DAY_OFF_THISWEEK)
            return true;
        else
            return false;
    }
    
    public boolean isDayOffNextWeek()
    {
        if (messageId == DAY_OFF_NEXTWEEK)
            return true;
        else
            return false;
    }
    
    
    
    public String toString()
    {
        StringBuffer retVal = new StringBuffer();
        retVal.append("Days off = ");
        if (daysOff != null)
        {
            for (int i=0; i < daysOff.length; i++)
            	retVal.append(daysOff[i] + ": ");

        }
        retVal.append("Message= " + super.toString());
        return retVal.toString();
    }
    
    public String generateSMSText()
    {
        
    	String txtFirst = "";
    	String txtSecond="";

    	if (daysOff == null || daysOff.length == 0)
    	{
    	    if ( isDayOffThisWeek() )
    	    {
	    	    txtFirst = "Days off this week: unspecified.";
	    	    txtSecond = " ";
    	    }
    	    else /*( dayMsg.isDayOffNextWeek() ) */
    	    {
	    	    txtFirst = "Days off next week: unspecified.";
	    	    txtSecond = " ";
    	    }
    	}
    	else if (daysOff.length == 1)
    	{
    	    if ( isDayOffThisWeek() )
    	    {
	    	    txtFirst = "Day off this week:  ";
	    	    txtSecond= daysOff[0].substring(0,4);
    	    }
    	    else /*( dayMsg.isDayOffNextWeek() ) */
    	    {
	    	    txtFirst = "Day off next week: ";
	    	    txtSecond= daysOff[0].substring(0,4);
    	    }
    	}
    	else // days off >= 2
    	{
    	    if ( isDayOffThisWeek() )
    	    {
	    	    txtFirst = "Days off this week: ";
	    	    for ( int i = 0; i < daysOff.length -1; i++)
	    	    {
	    	        txtSecond = txtSecond + daysOff[i].substring(0,4) + ", ";
	    	    }
	    	    txtSecond = txtSecond + daysOff[daysOff.length -1].substring(0,4);
    	    }
    	    else /*( dayMsg.isDayOffNextWeek() ) */
    	    {
	    	    txtFirst = "Days off next week: ";
	    	    for ( int i = 0; i < daysOff.length -1; i++)
	    	    {
	    	        txtSecond = txtSecond + daysOff[i].substring(0,4) + ", ";
	    	    }
	    	    txtSecond = txtSecond +  daysOff[daysOff.length -1].substring(0,4);
    	    }
    	}  // end days off >=2
    	return txtFirst + txtSecond;
    }
    
    public SMSMessage generateSMSMsg()
    {
        return new SMSMessage(super.phoneNum,generateSMSText());
    }
}
