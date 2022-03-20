/*
 * Created on Jan 27, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.Date;
import java.sql.Time;


public class PlainMessageBean extends MessageBean
{
    String messageText;

    public PlainMessageBean(long callSequence, int requestSequence,
            int responseSequence, int messageId, int manNumber, String pcsPhone,
            Date responseDate, Time responseTime, String messageText)
    {
        super(callSequence, requestSequence, responseSequence, messageId,
                manNumber,pcsPhone, responseDate, responseTime);
        this.messageText = messageText;
    }

    
    public boolean isValidMessage()
    {
        if (this.messageText.isEmpty())
            return false;
        else
            return true;
    }
    
    
 
    public String generateSMSText()
    {
    	return this.messageText;
    }
    
    public SMSMessage generateSMSMsg()
    {
        return new SMSMessage(super.phoneNum,generateSMSText());
    }
}
