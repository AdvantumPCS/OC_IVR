/*
 * Created on Jan 25, 2009
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

public class RecruitParamsBean
{
    /**
     * @return Returns the callSequence.
     */
    public static final int SLEEP_INTERVAL= 10;
    private short recordId = -1;
    private long callSequence = -1;
    private int timeout = -1;
    private int retries = -1;
    private int maxMessages = -1;

    public String toString()
    {
        String ret;
        ret=" recordId="+recordId + " callSequence= " +
        		callSequence + " timeout"+ timeout +
        		" retries"+ retries + "maxMessages= "+ maxMessages;
        return ret;
    }
    

    public long getCallSequence() {
        return callSequence;
    }
    
    
    /**
     * @return Returns the recordId.
     */
    public short getRecordId() {
        return recordId;
    }
    /**
     * @return Returns the retries.
     */
    public int getRetries() {
        return retries;
    }
    /**
     * @return Returns the timeout.
     */
    public int getTimeout() {
        return timeout;
    }

    
    /**
     * @param recordId
     * @param callSequence
     * @param timeout
     * @param retries
     */
    public RecruitParamsBean(short recordId, long callSequence, int timeout,
            int retries, int maxMessages)
    {
        this.recordId = recordId;
        this.callSequence = callSequence;
        this.timeout = timeout;
        this.retries = retries;
        this.maxMessages = maxMessages;
    }
    
    /**
     * @return Returns the maxMessages.
     */
    public int getMaxMessages() {
        return maxMessages;
    }
}
