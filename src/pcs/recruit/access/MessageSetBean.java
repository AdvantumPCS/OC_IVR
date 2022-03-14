/*
 * Created on Jan 27, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MessageSetBean
{
    public static final int STATUS_ERROR = 0;

    public static final int STATUS_OK =1;
    public static final int STATUS_TIMEOUT = 2;
    public static final int STATUS_LIMIT_EXCEEDED =3 ;
    
    public static final int STATUS_NO_MESSAGES = 4;
    
    
    
    private int setStatus = STATUS_ERROR;
    private Vector messages = new Vector();
    private int maxMessages = -1;
    
    public String toString()
    {
        String ret;
        ret="setStatus=" + setStatus + " maxMessages="+ maxMessages ;
        StringBuffer buf= new StringBuffer();
        if (messages != null)
        {
            for (int i=0; i < messages.size();i++)
            {
                Object data = messages.get(i);
                buf.append(" message"+i+"= " + data);
            }
        }
        return ret + buf.toString();
    }
    
    public boolean isOkStatus()
    {
        if (setStatus == STATUS_OK)
            return true;
        else
            return false;
    }
    
    public void setMessageId(long newId)
    {
        for (int i=0; i < messages.size(); i++)
        {
            MessageBean mb = (MessageBean)messages.get(i);
            mb.setCallSequence(newId);
        }
    }
    public MessageSetBean(int maxMessages)
    {
        super();
        if (maxMessages < 0)
            this.maxMessages = 0;
        else
            this.maxMessages = maxMessages;
        setStatus = STATUS_ERROR;
        
    }
    
    public void addMessage(MessageBean aBean)
    {
        if (messages.size() >= this.maxMessages)
        {
            setStatus = STATUS_LIMIT_EXCEEDED;
        }
        else
        {
            messages.add(aBean);
            setStatus = STATUS_OK;
        }    
        return;
    }
    
    public void setTimeoutStatus()
    {
        setStatus = STATUS_TIMEOUT;
    }
    
    public void setLimitExceededStatus()
    {
        setStatus = STATUS_LIMIT_EXCEEDED;
    }
    
    public void setErrorStatus()
    {
        setStatus = STATUS_ERROR;
    }
    
    public void setNoMessages()
    {
        setStatus = STATUS_NO_MESSAGES;
    }
    
    public int getStatus()
    {
        return setStatus;
    }
    
    public Vector getMessages()
    {
        return messages;
    }
    
    public boolean isLastAttempt()
    {
        if (setStatus == STATUS_OK)
        {
            if (messages.size()== 1)
            {
                try {
                    MessageBean msb = (MessageBean) messages.get(0);
                    int msgType = msb.getMessageId();
                    if (msgType == MessageId.LAST_ATTEMPT)
                        return true;
                    else
                        return false;
                }
                catch (Exception e)
                {
                    //log the error message
                    //e.printStackTrace();
                    Logger logger = Logger.getLogger("pcs.code");
                    logger.logp(Level.SEVERE,"MessageSetBean","isLastAttempt","database exception",e);
                    return false;
                }
                
            }
            else
                return false;
        }
        else 
            return false;
    }

    public boolean isLastGoodBye()
    {
        if (setStatus == STATUS_OK)
        {
            if (messages.size()== 1)
            {
                try {
                    MessageBean msb = (MessageBean) messages.get(0);
                    int msgType = msb.getMessageId();
                    if (msgType == MessageId.LAST_GOODBYE)
                        return true;
                    else
                        return false;
                }
                catch (Exception e)
                {
                    //log the error message
                    Logger logger = Logger.getLogger("pcs.code");
                    logger.logp(Level.SEVERE,"MessageSetBean","isLastGoodBye","database exception",e);
                    
                   // e.printStackTrace();
                    return false;
                }
                
            }
            else
                return false;
        }
        else 
            return false;
    }

    public boolean isIncorrect()
    {
        if (setStatus == STATUS_OK)
        {
            if (messages.size()== 1)
            {
                try {
                    MessageBean msb = (MessageBean) messages.get(0);
                    int msgType = msb.getMessageId();
                    if (msgType == MessageId.LOGIN_INCORRECT)
                        return true;
                    else
                        return false;
                }
                catch (Exception e)
                {
                    //log the error message
                  //  e.printStackTrace();
                  Logger logger = Logger.getLogger("pcs.code");
                  logger.logp(Level.SEVERE,"MessageSetBean","isIncorrect","database exception",e);
                    
                    return false;
                }
             }
            else
                return false;
        }
        else 
            return false;
    }
}
