/*
 * Created on Jan 27, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.util;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pcs.recruit.access.DoubleStates;


/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataConversion
{
    public static final String DATE_LOG_FORMAT = "yyyyMMdd";
    public static final String TIME_LOG_FORMAT = "HHmmss";
    public static final String WORK_DATE_FMT = "yyyyMMdd";
    public static final String WORK_TIME_FMT = "HH.mm";
    public static final char YES = 'Y';
    public static int convertLogDate(java.sql.Date date) {
        
        if (date == null)
            return 0;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(DATE_LOG_FORMAT);
            String tempDate = df.format(date);

            int tempInt = Integer.parseInt(tempDate);
            return (tempInt);
        } catch (Exception e)
        {
            return (0);
            /* **Log a Format Exception error */
        }
    }
    
    public static int convertLogTime(java.sql.Time time) {
        if (time == null)
            return 0;
        try
        {
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern(TIME_LOG_FORMAT);
            String tempTime = df.format(time);

            int tempInt = Integer.parseInt(tempTime);
            return (tempInt);
        } catch (Exception e)
        {
            return (0);
            /* **Log a Format Exception error */
        }
    }
    
    public static java.sql.Date parseWorkDate(long date)
    {
        try
        {
            SimpleDateFormat dateFmt = new SimpleDateFormat(WORK_DATE_FMT);
            String tmpDate = Long.toString(date);
            if (tmpDate.length() < 8)
                tmpDate = "0" + tmpDate;
            java.sql.Date ret = new java.sql.Date(dateFmt.parse(tmpDate).getTime());
            return (ret);
        }
        catch(Exception e)
        {
            return(null);
        }
    }

    public static java.sql.Date parseLogDate(long date)
    {
        try
        {
            SimpleDateFormat dateFmt = new SimpleDateFormat(DATE_LOG_FORMAT);
            String tmpDate = Long.toString(date);
            if (tmpDate.length() < 8)
                tmpDate = "0" + tmpDate;            
            java.sql.Date ret = new java.sql.Date(dateFmt.parse(tmpDate).getTime());
            return (ret);
        }
        catch(Exception e)
        {
            return(null);
        }
    }

    public static Time parseLogTime(int time)
    {
        try
        {     
            String tmpTime = Integer.toString(time);
            int needed = 6 - tmpTime.length();
            String prefix = null;
            if(needed > 0 && needed <= 6)
              prefix = "000000".substring(0,needed);
            else
                prefix = "";
            tmpTime = prefix + tmpTime; 
            SimpleDateFormat dateFmt = new SimpleDateFormat(TIME_LOG_FORMAT);
            Time t = new Time(dateFmt.parse(tmpTime).getTime());
            return(t);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    
    
    public static Time parseWorkTime(float time)
    {
        try
        {
            DecimalFormat df = new DecimalFormat("00.00");
            String ret = df.format((double)time);
            SimpleDateFormat dateFmt = new SimpleDateFormat(WORK_TIME_FMT);
            Time t = new Time(dateFmt.parse(ret).getTime());
            return(t);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    public static int parseNullableInt(String val)
    {
        try
        {
            if (val == null || val.length() == 0)
            {
                return -1;
            }
            else
            {
                int ret = Integer.parseInt(val);
                return ret;
            }
        }
        catch (Exception e)
        {
            return -1;
        }
    }
    
    public static String[] daysOff(String sun,String mon,
            String tue, String wed, String thu, String fri,String sat)
    {
        int loc = 0;
        ArrayList list = new ArrayList();
        if (sun.charAt(0)== YES)
        {
            list.add("Sunday");
        }
        if (mon.charAt(0)== YES)
            list.add("Monday");
        if (tue.charAt(0)== YES)
            list.add("Tuesday");
        if (wed.charAt(0) == YES)
            list.add("Wednesday");
        if (thu.charAt(0) == YES)
            list.add("Thursday");
        if (fri.charAt(0) == YES)
            list.add("Friday");
        if (sat.charAt(0) == YES)
            list.add("Saturday");
            
        String[] ret = new String[list.size()];
        
        
        for (int i =0; i < list.size(); i++)
        {   
          ret[i] = (String) list.get(i);
        }
        return ret;
    }
    
    public static String shiftInWords(int shift)
    {
        if (shift == 1)
            return "first";
        else if (shift == 2)
            return "second";
        else if (shift == 3)
            return "third";
        else if (shift == 4)
            return "fourth";
        else
            return "unspecified";
    }
    
    public static char covertLogDoubling(char val)
    {
        if (val == DoubleStates.AVAILABLE)
            return 'Y';
        else if (val == DoubleStates.UNAVAILABLE)
            return 'N';
        else if (val == DoubleStates.UNKNOWN)
            return 'X';
        else
            return val;
            
    }


}
