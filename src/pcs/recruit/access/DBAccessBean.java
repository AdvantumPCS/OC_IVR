/*
 * Created on Jan 24, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pcs.recruit.access;

import java.sql.*;
import java.util.ArrayList;
/* import javax.sql.*; */
/*import javax.naming.*; */
import pcs.recruit.util.*;
import java.util.logging.*;

/**
 * @author Orville
 * This class accesses the database tables on the AS/400.
 * It contains the functions that are used to:
 * 1) Request messages for a given Man Number
 * 2) Retreive Mesages asscoated witht the Man Number
 * 3) Log the results of the played messages.
 * 
 */
public class DBAccessBean
{
    private Connection connection = null;

    /**
     * @param connection holds the jdbc connection to the AS/400.
     */
    public DBAccessBean(Connection aConnection)
    {
        this.connection = aConnection;
    }

    public DBAccessBean()
    {

    }

    /**
     * @return Returns the connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection
     *            The connection to set.
     */
    public void setConnection(Connection aConnection) {
        this.connection = aConnection;
    }
    
    public void logonDirect()
    {
        try
        {
           DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
            connection = DriverManager.getConnection("jdbc:as400:0.0.0.0;user=pcs;password=pcs");
            
         }
        catch (Exception e)
        {
            Logger logger = Logger.getLogger("pcs.code");
            logger.logp(Level.SEVERE,"DBAccessBean","logonDirect","Could not logon to AS/400",e);
            /* e.printStackTrace(); */
        }
    }
    
    public boolean logonDirect2()
    {
        boolean retVal = false;
        try
        {
           DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
           	DriverManager.setLoginTimeout(60);
            connection = DriverManager.getConnection("jdbc:as400:0.0.0.0;user=pcs;password=pcs");
            retVal = true;
         }
        catch (Exception e)
        {
            Logger logger = Logger.getLogger("pcs.code");
            logger.logp(Level.SEVERE,"DBAccessBean","logonDirect2","Could not logon to AS/400",e);
        }
        return(retVal);
    }

    /* Log on to the AS/400 using a given userName and password */
    public boolean logonDirect3(String userName, String password, String hostName, int timeout)
    {
        boolean retVal = false;
        try
        {
           DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
           	DriverManager.setLoginTimeout(timeout);
            connection = DriverManager.getConnection("jdbc:as400:"+hostName+";user="+userName+";password="+password);
            retVal = true;
         }
        catch (Exception e)
        {
            Logger logger = Logger.getLogger("pcs.code");
            logger.logp(Level.SEVERE,"DBAccessBean","logonDirect3","Could not logon to AS/400 (" + hostName + ")",e);
        }
        return(retVal);
    }
    
    
    /* fuction returns true if a conntion object exists */
    public boolean isConnected()
    {
        if (connection != null)
            return true;
        else
            return false;
    }

/*    public void logon(String userName, String password, String dataSrc)
            throws NamingException, SQLException {
        try
        {
            if (connection != null)
                connection.close();
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(dataSrc);
            ctx.close();
            connection = ds.getConnection(userName, password);
            setDfltConnectProperties(connection);
        } catch (NamingException ne)
        {
            throw ne;
        } catch (SQLException sqle)
        {
            throw sqle;
        }
    }
*/
    
   /* function disconnects from the AS/400 database */ 
    public void logoff() throws SQLException {
        if (connection != null)
            connection.close();
        connection = null;
    }
    
    /* sets the default properties for the database connection */
    /* e.g autoComitt = off, readonly = false */
    private void setDfltConnectProperties(Connection con) throws SQLException {
        con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setReadOnly(false);
        con.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        return;
    }

    /* properties for read-only transactions for the database */
    private void setReadConnectProperties(Connection con) throws SQLException {
        con.setAutoCommit(false);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setReadOnly(true);
        con.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
        return;
    }
    
    /* function retreives the parameters that are stored in the 
     * "IVR_CONFIG table. These parameters control the behaviour of 
     * the IVR program.
     */
    public RecruitParamsBean getCallParameters(short rec_num)
            throws SQLException {
        PreparedStatement pUpdate = null;
        PreparedStatement pSelect = null;
        ResultSet rs = null;
        try
        {
            setDfltConnectProperties(connection);
            pUpdate = connection
                    .prepareStatement("UPDATE TRSLIB.IVRCONFIG SET TRSLIB.IVRCONFIG.CALL_SEQ = TRSLIB.IVRCONFIG.CALL_SEQ +1 "
                            + "WHERE TRSLIB.IVRCONFIG.RECORD_ID = ?");
                            
/*           pUpdate = connection
            .prepareStatement("UPDATE TRSLIB.IVRCONFIG SET RCPGMLIB2.IVRCONFIG.TIMEOUT = 90 "
                    + "WHERE RCPGMLIB2.IVRCONFIG.RECORD_ID = 1 ");
*/                            

           pUpdate.setShort(1, rec_num);
           int numRows = pUpdate.executeUpdate();

            if (numRows != 1)
            {
                pUpdate.close();
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","getCallParameters","One configuration Record Not Updated, record_id =" + rec_num);

                throw new SQLException(
                        "One Configuration Record Not Updated, record_id = "
                                + rec_num);
                
            }
            
            pSelect = connection.prepareStatement(
                    "SELECT CALL_SEQ, TIMEOUT, RETRIES, MAX_MESSAGES FROM TRSLIB.IVRCONFIG "
                            + " WHERE RECORD_ID = ?" );

            
/*             pSelect = connection.prepareStatement(
              "SELECT * FROM TRSLIB.TRSSTDIN",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
*/                    
/*             pSelect = connection.prepareStatement(
                     "SELECT * FROM TSTSCH.CONFIGTABLE ",
                           ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
*/
             
            pSelect.setShort(1, rec_num);  
            rs = pSelect.executeQuery();
            

            boolean rowExist = rs.next();
            if (rowExist)
            {
                long call_seq = rs.getLong(1);
                boolean val1 = rs.wasNull();
                int timeout = rs.getInt(2);
                boolean val2 = rs.wasNull();
                int retries = rs.getInt(3);
                boolean val3 = rs.wasNull();
                int maxMessages = rs.getInt(4);
                boolean val4 = rs.wasNull();
                connection.commit();
                rs.close();
                pSelect.close();
                pUpdate.close();
                if (val1 || val2 || val3 || val4)
                {
                    Logger logger = Logger.getLogger("pcs.code");
                    logger.logp(Level.SEVERE,"DBAccessBean","getCallParameters","One or more fields in Configuration record is null, record_id =" + rec_num);
                    
                    throw new SQLException(
                            " One or more fields in Configuration record is null record_id = "
                                    + rec_num);
                }
                
                RecruitParamsBean rpb = new RecruitParamsBean(rec_num,
                        call_seq, timeout, retries,maxMessages);
                return (rpb);
            } else
            {
                
                rs.close();
                pSelect.close();
                pUpdate.close();
                connection.rollback();
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","getCallParameters","Could not access Configuration record record_id =" + rec_num);

                throw new SQLException(
                        "Could not access Configuration record record_id ="
                                + rec_num);
            }

        } catch (SQLException sqle)
        {
            try {
                sqle.printStackTrace();
                if (rs!= null)
                  rs.close();
                if (pSelect!= null)
                  pSelect.close();
                if (pUpdate != null)
                  pUpdate.close();
                if (connection != null)
                  connection.rollback();
            }
            catch (SQLException sqle2)
            {
                //* **Log error
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","getCallParameters","One configuration Record Not Updated, record_id =" + rec_num,sqle2);

            }
            throw sqle;
        }
    }

    /* updates the TRSSTDIN table with the request for Messages for a given man number */
    /* the information is collected from the caller by the IVR app */
    /* The request is then updated */
    public void sendAccess(AuthorizeBean authorize) throws SQLException {

        if (authorize.isLoggable())
        {
            try
            {
                setDfltConnectProperties(connection);
/*                PreparedStatement pInsert = connection
                        .prepareStatement("INSERT INTO RCPGMLIB2.TRSSTDIN "
                                + "(REQID, REQSEQ, REQMAN, REQPIN, REQPHNO, REQTIME, REQDATE) "
                                + " VALUES (?,?,?,?,?,?,?)");
*/                                
                PreparedStatement pInsert = connection
                .prepareStatement("INSERT INTO TRSLIB.TRSSTDIN "
                        + "(REQID, REQSEQ, REQMAN, REQPIN, REQPHNO, REQTIME, REQDATE) "
                        + " VALUES (?,?,?,?,?,?,?)");
                                

                
                pInsert.setLong(1, authorize.getCallSequence());
                pInsert.setLong(2, authorize.getRequestSequence());
                pInsert.setInt(3, authorize.getManNumber());
                pInsert.setInt(4, authorize.getPinNumber());
                pInsert.setString(5, authorize.getPhoneNumber());
                pInsert.setInt(6, DataConversion.convertLogTime(authorize.getRequestTime()));
                pInsert.setInt(7, DataConversion.convertLogDate(authorize.getRequestDate()));
                pInsert.executeUpdate();
                connection.commit();
                pInsert.close();

            } catch (SQLException sqle)
            {
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","sendAccess","Could not insert record:" + authorize,"sqle=" + sqle.getMessage());

                throw new SQLException("Could not insert record:" + authorize
                        + "sqle=" + sqle.getMessage());
            }

        } else
        {
            Logger logger = Logger.getLogger("pcs.code");
            logger.logp(Level.SEVERE,"DBAccessBean","sendAccess","Could not insert record:" + authorize);

            throw new SQLException("Invalid Authorization message = "
                    + authorize.toString());
        }
    }

    /* this function repeatedly checks the messages table 
     * for messages from the recruiting application.
     * The routine will wait for up to "timeoutInterval" seconds
     * for a response. After that it returns a timeout status
     */
    public MessageSetBean retrieveMessages(long callID, int requestId, int maxMessages, int timeoutInterval, int sleepInterval) throws SQLException
    {

        boolean stop = false;
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis() + (timeoutInterval * 1000); 
        setReadConnectProperties(connection);
        MessageSetBean msgSet = new MessageSetBean(maxMessages);
        while (System.currentTimeMillis()  <  endTime && !stop)
        {
            msgSet = tryGetMessages(callID,requestId,maxMessages); 
            /* msgSet = tryGetMessages(628,requestId,maxMessages); */
            if ( msgSet.getStatus() == MessageSetBean.STATUS_NO_MESSAGES)
            {    
               stop = false;
               try
               {
                   Thread.sleep(sleepInterval * 1000);
               }
               catch (Exception e)
               {
                   //* **Log an error message to logger.
                   Logger logger = Logger.getLogger("pcs.code");
                   logger.logp(Level.SEVERE,"DBAccessBean","retreiveMessages","Sleep interupted, sleepVal=" + sleepInterval,e);
                   throw new SQLException("Sleep interupted, sleepVal=" + sleepInterval + e.getMessage());
               }
            }   
            else
                stop = true;
        }
        if (!stop)
            msgSet.setTimeoutStatus();
        return msgSet;

    }
    
    /* This function reads the message table, and returns any
     * messages in a MessageSetBean.
     */
    private MessageSetBean tryGetMessages(long callID, int requestId, int maxMessages) throws SQLException
    {
        
        MessageSetBean msgSet = new MessageSetBean(maxMessages);
        PreparedStatement pSelect = null;
        ResultSet rs = null;
        try 
        {
        pSelect = connection.prepareStatement(
                "SELECT RSPSEQ, RSPCD, RSPMAN,RSPRQDT,RSPRQST,RSPWRF,RSPBRTH,RSPSHFT," +
                "RSPSHPNM,RSPWAVF,RSPMSGTXT, DYOFSUN, DYOFMON, DYOFTUE,DYOFWED," +
                "DYOFTHU, DYOFFRI, DYOFSAT, RSPTIME,RSPDATE, RSPRQSD, RSPPHNO, RSPSMSTXT" +
                " FROM TRSLIB.TRSSTDOUT "
                + " WHERE RSQID = ? AND RSQSEQ = ?",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        pSelect.setLong(1, callID);
        pSelect.setInt(2,requestId);
        rs = pSelect.executeQuery();
        
        int rowCount = 0;
        boolean rowExist = rs.next();
        while (rowExist && rowCount < maxMessages)
        {
            int responseSeq = rs.getInt(1);
            boolean val1 = rs.wasNull();
            if(val1)
                responseSeq = -1;
            
            int messageId = rs.getInt(2);
            boolean val2 = rs.wasNull();
            if (val2)
                messageId = MessageId.NO_MSG_SPECEFIED;
            
            int manNum = rs.getInt(3);
            boolean val3 = rs.wasNull();
            if (val3)
                manNum = -1;
            
            long sdw = rs.getLong(4);
            boolean val4 = rs.wasNull();
            Date startDateWork = null;
            if (!val4)
               startDateWork = DataConversion.parseWorkDate(sdw);
            if (startDateWork == null)
                val4 = false;
            
            float stw = rs.getFloat(5);
            boolean val5 = rs.wasNull();
            java.sql.Time startTimeWork = null;            
            if (!val5)
               startTimeWork = DataConversion.parseWorkTime(stw);
            if (startTimeWork == null)
                val5 = false;
                        
            String warf = rs.getString(6);
            boolean val6 = rs.wasNull();
            if ( !val6)
                warf = warf.trim();
            int berth = rs.getInt(7);
            boolean val7 = rs.wasNull();
            if(val7)
                berth = -1;
            
            String ts = rs.getString(8);
            boolean val8 = rs.wasNull();
            int shift = -1;
            if(!val8)
            {
                ts = ts.trim();
                shift = DataConversion.parseNullableInt(ts);
                
            }
            if (shift == -1)
                val8 = false;
            
            String ship = rs.getString(9);
            boolean val9 = rs.wasNull();
            
            if (!val9)
                ship = ship.trim();
            
            String wavFile = rs.getString(10);
            boolean val10 = rs.wasNull();
            if (!val10)
                wavFile = wavFile.trim();
            
            String msgText = rs.getString(11);
            boolean val11 = rs.wasNull();
            if (!val11)
                msgText = msgText.trim();
            
            String daySun = rs.getString(12);
            String dayMon = rs.getString(13);
            String dayTue = rs.getString(14);
            String dayWed = rs.getString(15);
            String dayThu = rs.getString(16);
            String dayFri = rs.getString(17);
            String daySat = rs.getString(18);
            
            String daysOff[] = DataConversion.daysOff(daySun,dayMon,dayTue,dayWed,
                    dayThu,dayFri,daySat);
            
            int rst = rs.getInt(19);
            boolean val19 = rs.wasNull();
            java.sql.Time responseTime = null;
            if (!val19)
               responseTime = DataConversion.parseLogTime(rst);
            if (responseTime == null)
                val19 = false;

            java.sql.Date responseDate = null;
            int rsd = rs.getInt(20);
            boolean val20 = rs.wasNull();
            if (!val20)
               responseDate = DataConversion.parseLogDate(rsd);
            if (responseDate == null)
                val20 = false;
            
            
            String tmpDbl = rs.getString(21).trim();
            boolean val21 = rs.wasNull();
            char doubling = DoubleStates.UNKNOWN;
            if (!val21 && tmpDbl.length() == 1 )
            {
                doubling = tmpDbl.charAt(0);
            }
            else
                doubling = DoubleStates.ASK_NO;
            
            int tmpPhone = rs.getInt(22);
            String pcsPhone = "";
            boolean val22 = rs.wasNull();
            if (!val22)
                pcsPhone = Integer.toString(tmpPhone);
            else
                pcsPhone = "";
            
            String smsText = rs.getString(23);
            boolean val23 = rs.wasNull();
            if (!val23)
                smsText = smsText.trim();
            else
                smsText = "";
            
            
            MessageBean message = null;
            if (MessageBean.isWorkMessage(messageId))
            {
                
                message = new WorkMessageBean(callID,requestId,
                        responseSeq, manNum,
                        startDateWork,startTimeWork,warf,
                        berth,ship,shift, doubling,pcsPhone,
                        responseDate, responseTime);     
            }
            else if (MessageBean.isDayOffMessage(messageId))
            {
                message = new DayOffMessageBean(callID,requestId,responseSeq,
                        messageId,manNum,pcsPhone,responseDate,responseTime,daysOff);
            }
            /* else if (MessageBean.isRecordedMessage(messageId)) */
            else
            {
                if(RecordedMessageBean.isBuiltInMessage(messageId) )
                {
                    wavFile = RecordedMessageBean.getBuiltInWavFile(messageId);
                    val10 = false;
                    msgText = RecordedMessageBean.getBuiltInMsgText(messageId);
                    val11 = false;
                }
                
                message = new RecordedMessageBean(callID,requestId,responseSeq,messageId,manNum,
                        pcsPhone,wavFile,msgText,smsText,responseDate,responseTime);
                
            }
            msgSet.addMessage(message);
            rowCount++;
            rowExist = rs.next();
        } /* end while */            
        if (rowCount >= maxMessages)
            msgSet.setLimitExceededStatus();
        
        if (rowCount == 0)
            msgSet.setNoMessages();
        
        connection.commit();
        rs.close();
        pSelect.close();
        }
        catch (SQLException sqle)
        {
            msgSet.setErrorStatus();
            try
            {
                if (rs != null)
                   rs.close();
                if (pSelect != null)
                  pSelect.close();
                if (connection != null)
                  connection.rollback();
                /* **log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","tryGetMessages","database exception",sqle);

                
            }
            catch (SQLException sql2)
            {
                /** log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","tryGetMessages","database exception",sql2);
            }    
            throw sqle;
        }
        return msgSet;
    } /* end try get message */
    
    
    /* this function stores the Results of the played messages
     * to the Message log table. A message is stored only if it
     * was not previously written to the database */
     
    public boolean logMessageSet(ResultSetBean results) 
    {
        boolean success = true;
        try
        {
            setDfltConnectProperties(connection);
            ArrayList set = results.getResults();
            for (int i = 0; i < set.size();i++)
            {
                try
                {
                    LogResultBean cur = (LogResultBean)set.get(i);
                    if (!cur.isWritten())
                    {
                        logMessage(cur);
                        cur.updateWriteNow();
                    }
                }
                catch(SQLException sqle)
                {
                    //   ** Log the message error
                    success = false;
                    sqle.printStackTrace();
                }
            } /* end for */
        } /* end try */
        catch (SQLException sqle)
        {
            success = false;
            try
            {
                if (connection != null)
                    connection.rollback();
                /* **log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","logMessageSet","database exception",sqle);

            }
            catch (SQLException sql2)
            {
                /** log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","logMessageSet","database exception",sql2);

            }    
        }
        return(success);
    }
    
    /* logs a single message to the TRSMSGLOG table */
        private void logMessage(LogResultBean log) throws SQLException
    {
        PreparedStatement pInsert = null;
        try
        {
            pInsert = connection
                    .prepareStatement("INSERT INTO TRSLIB.TRSMSGLOG "
                            + "(RSQID, RSQSEQ, RSPSEQ,RSPCD,RSPDUBLE,LOGTIME,LOGDATE) "
                            + " VALUES (?,?,?,?,?,?,?)");
            pInsert.setLong(1, log.getRequestId());
            pInsert.setInt(2, log.getRequestSeq());
            pInsert.setInt(3, log.getResponseSeq());
            pInsert.setInt(4, log.getMessageId());
            pInsert.setString(5, Character.toString(DataConversion.covertLogDoubling(log.getDoubling())));
            pInsert.setInt(6, DataConversion.convertLogTime(log.getLogTime()));
            pInsert.setInt(7, DataConversion.convertLogDate(log.getLogDate()));
            pInsert.executeUpdate();
            connection.commit();
            pInsert.close();
        } catch (SQLException sqle)
        {
            try
            {
                if (pInsert != null)
                    pInsert.close();
                if (connection != null)
                    connection.rollback();
                /* **log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","logMessage","database exception",sqle);

            }
            catch (SQLException sql2)
            {
                /** log a database error here */
                Logger logger = Logger.getLogger("pcs.code");
                logger.logp(Level.SEVERE,"DBAccessBean","tryGetMessages","database exception",sql2);

            }    
            
            throw new SQLException("Could not insert record:" + log
                    + "sqle=" + sqle.getMessage());
        }
    }
    public String toString()
    {
        if (connection == null)
        {
            return ("No Connection");
        }
        else
        {
            return ("connection= " + connection.toString());
        }
    }
}
        
        
