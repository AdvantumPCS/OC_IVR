/*
 * Created on Jan 27, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.Date;
import java.sql.Time;
import java.text.*;
import pcs.recruit.util.*;

/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkMessageBean extends MessageBean implements DoubleStates
{
    /**
     * @param callSequence
     * @param requestSequence
     * @param responseSequence
     * @param messageId
     * @param manNumber
     * @param responseDate
     * @param responseTime
     */
    public WorkMessageBean(long callSequence, int requestSequence,
            int responseSequence, int manNumber,
            Date startWorkDate, Time startWorkTime, String warf,
            int berth , String ship, int shift,char doubling,String pcsPhone,
            Date responseDate, Time responseTime) 
    {
        super(callSequence, requestSequence, responseSequence, MessageBean.WORK,
                manNumber, pcsPhone, responseDate, responseTime);
        this.startWorkDate = startWorkDate;
        this.startWorkTime = startWorkTime;
        this.warf = warf;
        this.berth = berth;
        this.ship = ship;
        this.shift = shift;
        this.doubling = doubling;
        // TODO Auto-generated constructor stub
    }
    java.sql.Date startWorkDate = null;
    java.sql.Time startWorkTime = null;
    String warf = null;
    int berth = -1;
    String ship = null;
    int shift = -1;
    char doubling = UNKNOWN;
    
    public String toString()
    {
        String ret;
        ret = "startWorkDate="+ startWorkDate +" startWorkTime="+ startWorkTime +
        " warf" + warf + " berth=" + berth + " ship="+ ship + " shift="+shift +
        " doubling=" + doubling + " message="+ super.toString();
        return(ret);
    }
    public boolean isValidShift()
    {
        if (shift >= 1 && shift <=3)
            return true;
        else
            return false;
    }
    
    public boolean isValidMessage()
    { if (super.isValidMessage() && super.isWorkMessage(super.messageId)&&this.startWorkDate != null 
        && this.startWorkTime != null && isValidShift())
        return true;
      else
          return false;
    	
    }
    
    public boolean isPlayableShipBerth()
    {
        if (berth > 0 && ship !=null && ship.length() > 0)
            return true;
        else
            return false;
    }
    
    
    /**
     * @return Returns the berth.
     */
    public int getBerth() {
        return berth;
    }
    /**
     * @return Returns the shift.
     */
    public int getShift() {
        return shift;
    }
    /**
     * @return Returns the ship.
     */
    public String getShip() {
        return ship;
    }
    /**
     * @return Returns the startWorkDate.
     */
    public java.sql.Date getStartWorkDate() {
        return startWorkDate;
    }
    /**
     * @return Returns the startWorkTime.
     */
    public java.sql.Time getStartWorkTime() {
        return startWorkTime;
    }
    /**
     * @return Returns the warf.
     */
    public String getWarf() {
        return warf;
    }
    public char getDoubling() {
        return doubling;
    }
    public void setDoubling(char doubling) {
        this.doubling = doubling;
    }
    
    public String generateSMSText()
    {
        
	    if (!isValidMessage() )
	    {
	        return MessageId.SMS_WORK_MISSING;
	    }
	    else //valid work msg
	    {
		    StringBuffer first = new StringBuffer("");
		    StringBuffer second = new StringBuffer("");
		    StringBuffer third = new StringBuffer("");
		    first.append("Work on:");
		    
		    SimpleDateFormat dayFmt = new SimpleDateFormat("EEE");
		    SimpleDateFormat monthFmt= new SimpleDateFormat("MMM");
		    SimpleDateFormat dayDateFmt= new SimpleDateFormat("dd");
		    String date = "";
		    date = dayFmt.format(getStartWorkDate()) + " " + 
		    monthFmt.format(getStartWorkDate()) + " ";
		    String dayVal = dayDateFmt.format(getStartWorkDate());
		    date = date + dayVal+ ".";
		    first.append(date);
		    
		    if (isPlayableShipBerth() )
		    	second.append("On ship,"+ getShip() + ",berth " + getBerth() + ".");
		    else
		        second.append(" ");
		        
		    third.append("Start time:");
		    
		    SimpleDateFormat hourFmt = new SimpleDateFormat("hh");
		    SimpleDateFormat minFmt = new SimpleDateFormat("mm");
		    SimpleDateFormat ampmFmt = new SimpleDateFormat("a");
		    String hour = hourFmt.format(getStartWorkTime());
		    String min = minFmt.format(getStartWorkTime());
		    String ampm = ampmFmt.format(getStartWorkTime());
		    String time = hour + ":";
		    time = time + min + " ";
		    time = time + ampm;
		    
		    third.append(time);
		    /* third.append("On the "); */
		    third.append(" ");
		    third.append(DataConversion.shiftInWords(getShift()) +" shift. " );
		   /* third.append("Arrive at Terminal at least one hour before shift start."); */
		    third.append("Arrive Terminal, min 1 hr before shift");
		    return first.toString() + second.toString() + third.toString();
	    }    
    }
    
    public SMSMessage generateSMSMsg()
    {
        return new SMSMessage(super.phoneNum,generateSMSText());
    }
    
    
}
