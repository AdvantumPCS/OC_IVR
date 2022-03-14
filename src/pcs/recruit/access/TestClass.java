/*
 * Created on Jan 30, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.*;


/**
 * @author Orville
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestClass
{

    public static void main(String[] args) throws Exception
    {
      /*  WorkMessageBean wmb = new WorkMessageBean((long)1, 1,2,454,new Date(System.currentTimeMillis()),
                new Time(System.currentTimeMillis()),(String)null,0,(String)null,0,'c',(Date)null,(Time)null);
      */          

        /* System.out.println(wmb.toString()); 
        Logger logger= Logger.getLogger("pcs.code"); */
        /* logger.addHandler(new FileHandler("c:\\temp\\okpw.log")); 
        logger.warning("This is a test warning"); */
        
/*        public WorkMessageBean(long callSequence, int requestSequence,
                int responseSequence, int manNumber,
                Date startWorkDate, Time startWorkTime, String warf,
                int berth , String ship, int shift,char doubling,
                Date responseDate, Time responseTime) 
*/
       DBAccessBean acc = new DBAccessBean();
        try 
        {

           /* acc.logon("pcs","pcs","jdbc/recruitDB"); */
           acc.logonDirect3("pcs","pcs","192.168.1.9",60); 
            RecruitParamsBean rpb=  acc.getCallParameters((short)1);
/*            MessageSetBean msb = acc.retrieveMessages((long)79933275, 1,rpb.getMaxMessages(), rpb.getTimeout(),10);
            ResultSetBean rsb = new ResultSetBean();
            
            for ( int i = 0 ; i < msb.getMessages().size();i++)
            {
                MessageBean mb = (MessageBean) msb.getMessages().get(i);
                PlayBean pb = new PlayBean(mb,DoubleStates.UNKNOWN);
                rsb.addMessage(pb);
            }
            MessageBean mb = (MessageBean) msb.getMessages().get(1);
            PlayBean pb = new PlayBean(mb,DoubleStates.AVAILABLE);
            rsb.addMessage(pb);

            mb = (MessageBean) msb.getMessages().get(3);
            pb = new PlayBean(mb,DoubleStates.UNAVAILABLE);
            rsb.addMessage(pb);
            
            acc.logMessageSet(rsb);
*/            
            /* WorkMessageBean wmb = (WorkMessageBean) msb.getMessages().get(0);
            boolean isValid= wmb.isValidMessage();
           AuthorizeBean auth = new AuthorizeBean( rpb.getCallSequence(),(long)1,4546,
                   2111,"3301234",new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
*/
           AuthorizeBean auth = new AuthorizeBean( rpb.getCallSequence(),(long)1,4732,
                   1234,"3312771",new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis()));
           
           acc.sendAccess(auth);
           MessageSetBean msb = acc.retrieveMessages(rpb.getCallSequence(), 1,rpb.getMaxMessages(), rpb.getTimeout(),10);
           ResultSetBean rsb = new ResultSetBean();
           
/*           for ( int i = 0 ; i < msb.getMessages().size();i++)
           {
               MessageBean mb = (MessageBean) msb.getMessages().get(i);
               mb.setPhoneNum("8763309939");
               msb.addMessage(mb);
               msb.addMessage(mb);
               PlayBean pb = new PlayBean(mb,DoubleStates.AVAILABLE);
               rsb.addMessage(pb);
           }
*/           
       		SMSMessagesBean smb;
       		if ( msb != null )
       		{    
       		    smb= new SMSMessagesBean("","","","","","shippingja","shippingja",true,true, msb,rsb);
       		    smb.generateAndSend();
       		}

           
    /*       acc.logMessageSet(rsb);
           acc.logMessageSet(rsb);
    */       
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } 
    } 
}
