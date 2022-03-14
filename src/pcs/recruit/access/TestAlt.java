/*
 * Created on Jan 30, 2009
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
public class TestAlt
{

    public static void main(String[] args)
    {
        DBAccessBean acc = new DBAccessBean();
        try 
        {
           
           acc.logonDirect(); 
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
