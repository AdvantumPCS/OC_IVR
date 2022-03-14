/*
 * Created on Jan 29, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.util.ArrayList;



/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResultSetBean
{
    java.util.ArrayList results = null;
    
    public String toString()
    {
        StringBuffer ret = new StringBuffer("results =");
        if (results != null)
        {
            for (int i = 0; i < results.size();i ++)
            {
                ret.append(" Result"+ i +" ="+ results.get(i));
            }
            
        }
        return ret.toString();
    }
    public ResultSetBean()
    {
        results = new ArrayList();
    }
    
    public void addMessage(PlayBean msg)
    {
        LogResultBean log = new LogResultBean(msg);
        int index= results.indexOf(log);
        if (index == -1)
        {
            results.add(log);
        }
        else
        {
            LogResultBean info = (LogResultBean )results.get(index);
            info.incrementPlayCount();
            info.updateDoubling(msg.getDoubling());
        }
        return;
    }
    
    public void updateDoublingStatus(PlayBean msg) throws Exception
    {
        LogResultBean log = new LogResultBean(msg);
        int index= results.indexOf(log);
        if (index == -1)
        {
            throw new Exception("Play bean has not been prviously played" + msg.toString());
        }
        else
        {
            LogResultBean info = (LogResultBean )results.get(index);
            info.updateDoubling(msg.getDoubling());
        }
        return;
    }
    
    public ArrayList getResults()
    {
        return results;
    }
}
