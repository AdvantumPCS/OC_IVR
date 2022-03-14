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
public class RecordedMessageBean extends MessageBean
{

    String wavFile;
    String msgText;
    String smsText;
    
    /**
     * @param callSequence
     * @param requestSequence
     * @param responseSequence
     * @param messageId
     * @param manNumber
     * @param responseDate
     * @param responseTime
     */
    public RecordedMessageBean(long callSequence, int requestSequence,
            int responseSequence, int messageId, int manNumber,String pcsPhone,
            String wavFile, String msgText,String smsText, Date responseDate, Time responseTime)
    {
        super(callSequence, requestSequence, responseSequence, messageId,
                manNumber,pcsPhone, responseDate, responseTime);
        this.wavFile = wavFile;
        this.msgText = msgText;
        this.smsText = smsText;        
    }
    
    public String toString()
    	{
        String ret;
        ret="wavFile=" + wavFile + " msgText="+ msgText + "smsText=" + smsText + "message= " + super.toString();
        return (ret);
    	}    
    
    public boolean isValidWavFile()
    {
        boolean retVal= false;
        if (wavFile != null)
            if (wavFile.length() >= 5 && wavFile.endsWith(".wav"))
                retVal = true;
        return retVal;
    }
    
    public boolean isValidMsgText()
    {
        if (msgText != null && msgText.length() > 5)
            return true;
        else
            return false;
    }
    
    public boolean isValidSMSText()
    {
        if (smsText != null && smsText.length() > 2)
            return true;
        else
            return false;
    }
    
    public boolean isValidRecordedMsgType()
    {
            
        return super.isRecordedMessage(super.getMessageId());
        
    }
    
    public boolean isValidMessage()
    {
        boolean retVal = false;
        if (super.isValidMessage() && isValidRecordedMsgType() )
            if (!super.isBuiltInMessage(super.messageId))
                retVal = isValidWavFile() || isValidMsgText();
            else
                retVal = true;
        else
            retVal = false;
        
        return retVal;
    }
    
    public static String getBuiltInWavFile(int messageId)
    {
        if (messageId == LOGIN_INCORRECT)
            return WAV_INCORRECT;
        else if (messageId == NO_WORK)
            return WAV_NOWORK;
        else if (messageId == NO_MSG_SPECEFIED)
            	return WAV_NOMSG;
        else if (messageId == OUTSIDE_TIME)
            return WAV_OUTSIDE;
        else if (messageId == LAST_ATTEMPT)
            return WAV_LASTATTEMPT;
        else if (messageId == LAST_GOODBYE)
            return(WAV_LASTBYE);
        else if (messageId == BARRED)
            return (WAV_BARRED);
        else if (messageId == SYS_ERROR)
            	return (WAV_SYSERROR);
        else if (messageId == LIMIT_EXCEEDED)
            	return (WAV_EXCEEDED);
        else if (messageId == UNAVAILABLE)
            	return (WAV_UNAVAILABLE);
        else
            return WAV_NOMSG;
            	
        
    }

    public static String getBuiltInMsgText(int messageId)
    {
        if (messageId == LOGIN_INCORRECT)
            return TEXT_INCORRECT;
        else if (messageId == NO_WORK)
            return TEXT_NOWORK;
        else if (messageId == NO_MSG_SPECEFIED)
            return TEXT_NOMSG;
        else if (messageId == OUTSIDE_TIME)
            return TEXT_OUTSIDE;
        else if (messageId == LAST_ATTEMPT)
            return TEXT_LASTATTEMPT;
        else if (messageId == LAST_GOODBYE)
            return(TEXT_LASTBYE);
        else if (messageId == BARRED)
            return (TEXT_BARRED);
        else if (messageId == SYS_ERROR)
            return (TEXT_SYSERROR);
        else if (messageId == LIMIT_EXCEEDED)
            return (TEXT_EXCEEDED);
        else if (messageId == UNAVAILABLE)
            return (TEXT_UNAVAILABLE);
        else
            return TEXT_NOMSG;
        
    }
    
    public static String getBuiltInSMSText(int messageId)
    {
        if (messageId == NO_WORK)
            return SMS_NOWORK;
        else if (messageId == NO_MSG_SPECEFIED)
            return SMS_NOMSG;
        else if (messageId == OUTSIDE_TIME)
            return SMS_OUTSIDE;
        else if (messageId == LAST_GOODBYE)
            return(SMS_LASTBYE);
        else if (messageId == BARRED)
            return (SMS_BARRED);
        else if (messageId == SYS_ERROR)
            return (SMS_SYSERROR);
        else if (messageId == LIMIT_EXCEEDED)
            return (SMS_EXCEEDED);
        else if (messageId == UNAVAILABLE)
            return (SMS_UNAVAILABLE);
        else if (messageId == LOGIN_INCORRECT)
            return SMS_INCORRECT;
        else if (messageId == LAST_ATTEMPT)
            return SMS_LASTATTEMPT;
        

        else
            return SMS_NOMSG;
        
    }
    
    
    public String getMsgText() {
        return msgText;
    }
    public String getWavFile() {
        return wavFile;
    }
    
    public String getSmsText() {
        return smsText;
    }
    
    public SMSMessage generateSMSMsg()
    {
        if (isBuiltInMessage())
            return new SMSMessage(super.phoneNum,getBuiltInSMSText(super.messageId));
        else
        {
            return new SMSMessage(super.phoneNum,smsText);
        }
    }
}
