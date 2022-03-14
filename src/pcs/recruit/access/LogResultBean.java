/*
 * Created on Jan 28, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogResultBean implements DoubleStates
{
    
    private long requestId = -1;
    private int requestSeq = -1;
    private int responseSeq = -1;
    private int messageId = MessageId.NO_MSG_SPECEFIED;
    private char doubling = DoubleStates.UNKNOWN;
    protected int playCount = 0;
    private Timestamp written = null;
    private Date logDate = null;
    private Time logTime = null;
    
public String toString()
{
    String ret = "";
    ret = "requestId= "+ requestId + " requestSeq =" + requestSeq + 
    " responseSeq=" + responseSeq + " messageId="+ messageId +
    " doubling="+ doubling + " playCount=" + playCount + " Timestamp= " + written +
    " logDate= " + logDate + " logTime="+logTime;
    return ret;
    	
}
    /**
     * @param requestId
     * @param requestSeq
     * @param responseSeq
     * @param messageId
     * @param doubling
     * @param logDate
     * @param logTime
     */
    public LogResultBean(long requestId, int requestSeq, int responseSeq,
            int messageId, char doubling, int playCount, Date logDate, Time logTime)
    {
        this.requestId = requestId;
        this.requestSeq = requestSeq;
        this.responseSeq = responseSeq;
        this.messageId = messageId;
        this.doubling = doubling;
        this.logDate = logDate;
        this.logTime = logTime;
        this.playCount = playCount;
        this.written = null;
    }
    
    public LogResultBean(PlayBean played)
    {
        this.requestId = played.getMsg().getCallSequence();
        this.requestSeq = played.getMsg().getRequestSequence();
        this.responseSeq =played.getMsg().getResponseSequence();
        this.messageId = played.getMsg().getMessageId();
        this.doubling = played.getDoubling();
        this.playCount = 1;
        this.logDate = played.getPlayDate();
        this.logTime = played.getPlayTime();
        this.written = null;
    }
    /**
     * @return Returns the doubling.
     */
    public char getDoubling() {
        return doubling;
    }
    
    
    public boolean isWritten()
    {
        if (written == null)
            return false;
        else
            return true;
    }
    
    public void incrementPlayCount()
    {
        playCount++;
    }
    
    public void updateWriteNow()
    {
        written = new Timestamp(System.currentTimeMillis());
    }
    
    public void updateDoubling(char response)
    {
        if (response == AVAILABLE || response == UNAVAILABLE)
        {
            this.doubling = response;
        }
    }
    /**
     * @param doubling The doubling to set.
     */
    /**
     * @return Returns the playCount.
     */
    public int getPlayCount() {
        return playCount;
    }
    
    /**
     * @return Returns the logDate.
     */
    public Date getLogDate() {
        return logDate;
    }
    /**
     * @return Returns the logTime.
     */
    public Time getLogTime() {
        return logTime;
    }
    /**
     * @return Returns the messageId.
     */
    public int getMessageId() {
        return messageId;
    }
    /**
     * @return Returns the requestId.
     */
    public long getRequestId() {
        return requestId;
    }
    /**
     * @return Returns the requestSeq.
     */
    public int getRequestSeq() {
        return requestSeq;
    }
    /**
     * @return Returns the responseSeq.
     */
    public int getResponseSeq() {
        return responseSeq;
    }
    public boolean equals(Object o)
    {
        if (!(o instanceof LogResultBean ))
                return false;
        LogResultBean alog = (LogResultBean) o;
        if( this.requestId == alog.requestId &&
             this.requestSeq == alog.requestSeq &&
             this.responseSeq == alog.responseSeq &&
             this.messageId == alog.messageId )
            return true;
        else
            return false;
    }
}
