/*
 * Created on Jan 29, 2009
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
public class PlayBean
{
    MessageBean msg = null;
    char doubling = DoubleStates.UNKNOWN;
    java.sql.Date playDate = null;
    java.sql.Time playTime = null;

    public String toString()
    {
        String ret;
        ret= "doubling="+ doubling+ " playDate"+playDate + " playTime="+playTime +
        " msg="+ msg;
        return ret;
    }

    /**
     * @param msg
     * @param doubling
     * @param playDate
     * @param playTime
     */
    public PlayBean(MessageBean msg, char doubling, java.sql.Date playDate,
            java.sql.Time playTime)
    {
        this.msg = msg;
        this.doubling = doubling;
        this.playDate = playDate;
        this.playTime = playTime;
    }
    
    public PlayBean(MessageBean msg, char doubling)
    {
        this.msg = msg;
        this.doubling = doubling;
        long curStamp = System.currentTimeMillis();
        this.playDate = new Date(curStamp);
        this.playTime = new Time(curStamp);
    }
    /**
     * @return Returns the doubling.
     */
    public char getDoubling() {
        return doubling;
    }
    /**
     * @return Returns the msg.
     */
    public MessageBean getMsg() {
        return msg;
    }
    /**
     * @return Returns the playDate.
     */
    public java.sql.Date getPlayDate() {
        return playDate;
    }
    /**
     * @return Returns the playTime.
     */
    public java.sql.Time getPlayTime() {
        return playTime;
    }
}
