package com.advantumpcs.shannonb;

import com.frameworks.apptools.AppLog;
import com.frameworks.apptools.ConfigManager;
import com.frameworks.datatools.DBPortal;
import java.io.PrintStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import pcs.recruit.access.DayOffMessageBean;
import pcs.recruit.access.MessageBean;
import pcs.recruit.access.RecordedMessageBean;
import pcs.recruit.access.WorkMessageBean;
import pcs.recruit.util.DataConversion;

public class AppData
{
  public static final String DELIM = "~~";
  public static final String LINEBREAK = "<BR>";
  private int monitorInterval;
  private String sms_DBType;
  private String[] sms_ConnInfo = new String[4];
  
  public AppData(String dbType, String dbHostName, String dbUsr, String dbPwd, String dbName)
  {
    String[] attributeNames = { "sms_DBType", "sms_DBHostName", "sms_DBUsr", "sms_DBPwd", "sms_DBName" };
    if((dbType.isEmpty()) || (dbType == null)) {
	    ConfigManager cfg = new ConfigManager("config.properties", attributeNames);
	    cfg.getConfig();
	    this.sms_DBType = cfg.getParameter("sms_DBType");
	    this.sms_ConnInfo[0] = cfg.getParameter("sms_DBHostName");
	    this.sms_ConnInfo[1] = cfg.getParameter("sms_DBUsr");
	    this.sms_ConnInfo[2] = cfg.getParameter("sms_DBPwd");
	    this.sms_ConnInfo[3] = cfg.getParameter("sms_DBName");
    }
    else {
    	this.sms_DBType = dbType;
    	this.sms_ConnInfo[0] = dbHostName;
	    this.sms_ConnInfo[1] = dbUsr;
	    this.sms_ConnInfo[2] = dbPwd;
	    this.sms_ConnInfo[3] = dbName;
    }
    
    if (this.sms_DBType == null) {
      AppLog.addToLog("SMS DB Configuration Data is Missing. Unable to Start.");
    }
  }
  
  public String[] getConnInfo(String type)
  {
    String[] output = null;
    if (type.equals("MSSQL")) {
      output = this.sms_ConnInfo;
    }
    return output;
  }
  
  public String updateNewMessage(int id, String stat)
  {
    String strUpdate = "UPDATE NewMessages SET Msg_Status='" + stat + "' WHERE Msg_ID=" + id + ";";
    return DBPortal.RunCommand(strUpdate, this.sms_DBType, this.sms_ConnInfo);
  }
  
  public String deleteNewMessage(int id)
  {
    String strDelete = "DELETE FROM NewMessages WHERE Msg_ID=" + id + ";";
    return DBPortal.RunCommand(strDelete, this.sms_DBType, this.sms_ConnInfo);
  }
  
  public String addProcessedMessage(int msg_ID, String msg_Type, String msg_From, String msg_To, String msg_Title, String msg_Body, String msg_RequestedBy, String msg_RequestDateTime, String msg_ProcessDateTime, String msg_Provider, String msg_SendDateTime, String msg_Confirmation, String msg_Status)
  {
    String strMoveToProcessed = "INSERT INTO ProcessedMessages VALUES(" + 
      msg_ID + ",'" + msg_Type + "','" + msg_From + "','" + 
      msg_To + "','" + msg_Title + "','" + msg_Body + "','" + msg_RequestedBy + "','" + 
      msg_RequestDateTime + "', '" + msg_ProcessDateTime + "','" + msg_Provider + "','" + 
      msg_SendDateTime + "','" + msg_Confirmation + "','" + msg_Status + "');";
    return DBPortal.RunCommand(strMoveToProcessed, this.sms_DBType, this.sms_ConnInfo);
  }
  
  public int[] getList(String id_Column, String tbl_Name)
  {
    AppLog.addToLog("DBLog", "Entering getList('" + id_Column + "','" + tbl_Name + "').....");
    String ids = "";
    int[] output = null;
    try
    {
      ResultSet rs = DBPortal.getResultSet("SELECT " + id_Column + " FROM " + tbl_Name + ";", this.sms_DBType, this.sms_ConnInfo);
      while (rs.next())
      {
        if (!rs.isFirst()) {
          ids = ids + ";";
        }
        ids = ids + rs.getInt(id_Column);
      }
      rs.close();
      System.out.println("getList: ResultSet closed.");
    }
    catch (SQLException sqle)
    {
      AppLog.addToLog("DBLog", sqle.getMessage() + "..." + sqle.getStackTrace());
    }
    if (ids.length() > 2)
    {
      String[] items = ids.split(";");
      output = new int[items.length];
      for (int i = 0; i < items.length; i++) {
        output[i] = Integer.parseInt(items[i]);
      }
    }
    return output;
  }
  
  public String get_SavedMessages()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String output = "";
    try
    {
      ResultSet rs = DBPortal.getResultSet("SELECT * FROM dbo.NewMessages", this.sms_DBType, this.sms_ConnInfo);
      while (rs.next()) {
        output = 
        
          output + rs.getInt("Msg_ID") + "~~" + cleanText(rs.getString("Msg_Type")) + "~~" + cleanText(rs.getString("Msg_From")) + "~~" + cleanText(rs.getString("Msg_To")) + "~~" + cleanText(rs.getString("Msg_Title")) + "~~" + cleanText(rs.getString("Msg_Body")) + "~~" + cleanText(rs.getString("Msg_RequestedBy")) + "~~" + cleanText(sdf.format(rs.getTimestamp("Msg_RequestDateTime"))) + "~~" + cleanText(rs.getString("Msg_Status")) + "<BR>";
      }
      rs.close();
      System.out.println("get_SavedMessages: MSSQL ResultSet closed.");
    }
    catch (SQLException sqle)
    {
      AppLog.addToLog("DBLog", sqle.getMessage() + "..." + sqle.getStackTrace());
    }
    return output;
  }
  
  public String get_TRSSTDOUT_Messages()
  {
    String output = "";
    String strSQL = "SELECT * FROM TRSLIB.TRSSTDOUT WHERE RSPCD=10 AND RSPDATE=20141231";
    ResultSet rs = DBPortal.getResultSet(strSQL, "DB400", this.sms_ConnInfo);
    try
    {
      boolean rowExist = rs.next();
      while (rowExist)
      {
        long callID = rs.getLong("RSQID");
        int requestId = rs.getInt("RSQSEQ");
        
        int responseSeq = rs.getInt("RSPSEQ");
        boolean val1 = rs.wasNull();
        if (val1) {
          responseSeq = -1;
        }
        int messageId = rs.getInt("RSPCD");
        boolean val2 = rs.wasNull();
        if (val2) {
          messageId = -1;
        }
        int manNum = rs.getInt("RSPMAN");
        boolean val3 = rs.wasNull();
        if (val3) {
          manNum = -1;
        }
        long sdw = rs.getLong("RSPRQDT");
        boolean val4 = rs.wasNull();
        Date startDateWork = null;
        if (!val4) {
          startDateWork = DataConversion.parseWorkDate(sdw);
        }
        if (startDateWork == null) {
          val4 = false;
        }
        float stw = rs.getFloat("RSPRQST");
        boolean val5 = rs.wasNull();
        Time startTimeWork = null;
        if (!val5) {
          startTimeWork = DataConversion.parseWorkTime(stw);
        }
        if (startTimeWork == null) {
          val5 = false;
        }
        String warf = rs.getString("RSPWRF");
        boolean val6 = rs.wasNull();
        if (!val6) {
          warf = warf.trim();
        }
        int berth = rs.getInt(7);
        boolean val7 = rs.wasNull();
        if (val7) {
          berth = -1;
        }
        String ts = rs.getString("RSPSHFT");
        boolean val8 = rs.wasNull();
        int shift = -1;
        if (!val8)
        {
          ts = ts.trim();
          shift = DataConversion.parseNullableInt(ts);
        }
        if (shift == -1) {
          val8 = false;
        }
        String ship = rs.getString("RSPSHPNM");
        boolean val9 = rs.wasNull();
        if (!val9) {
          ship = ship.trim();
        }
        String wavFile = rs.getString("RSPWAVF");
        boolean val10 = rs.wasNull();
        if (!val10) {
          wavFile = wavFile.trim();
        }
        String msgText = rs.getString("RSPMSGTXT");
        boolean val11 = rs.wasNull();
        if (!val11) {
          msgText = msgText.trim();
        }
        String daySun = rs.getString("DYOFSUN");
        String dayMon = rs.getString("DYOFMON");
        String dayTue = rs.getString("DYOFTUE");
        String dayWed = rs.getString("DYOFWED");
        String dayThu = rs.getString("DYOFTHU");
        String dayFri = rs.getString("DYOFFRI");
        String daySat = rs.getString("DYOFSAT");
        
        String[] daysOff = DataConversion.daysOff(daySun, dayMon, dayTue, dayWed, dayThu, dayFri, daySat);
        
        int rst = rs.getInt("RSPTIME");
        boolean val19 = rs.wasNull();
        Time responseTime = null;
        if (!val19) {
          responseTime = DataConversion.parseLogTime(rst);
        }
        if (responseTime == null) {
          val19 = false;
        }
        Date responseDate = null;
        int rsd = rs.getInt("RSPDATE");
        boolean val20 = rs.wasNull();
        if (!val20) {
          responseDate = DataConversion.parseLogDate(rsd);
        }
        if (responseDate == null) {
          val20 = false;
        }
        String tmpDbl = rs.getString("RSPRQSD").trim();
        boolean val21 = rs.wasNull();
        char doubling = 'X';
        if ((!val21) && (tmpDbl.length() == 1)) {
          doubling = tmpDbl.charAt(0);
        } else {
          doubling = 'N';
        }
        int tmpPhone = rs.getInt("RSPPHNO");
        String pcsPhone = "";
        boolean val22 = rs.wasNull();
        if (!val22) {
          pcsPhone = Integer.toString(tmpPhone);
        } else {
          pcsPhone = "";
        }
        String smsText = rs.getString("RSPSMSTXT");
        boolean val23 = rs.wasNull();
        if (!val23) {
          smsText = smsText.trim();
        } else {
          smsText = "";
        }
        MessageBean message = null;
        if (MessageBean.isWorkMessage(messageId))
        {
          message = new WorkMessageBean(callID, requestId, 
            responseSeq, manNum, 
            startDateWork, startTimeWork, warf, 
            berth, ship, shift, doubling, pcsPhone, 
            responseDate, responseTime);
        }
        else if (MessageBean.isDayOffMessage(messageId))
        {
          message = new DayOffMessageBean(callID, requestId, responseSeq, 
            messageId, manNum, pcsPhone, responseDate, responseTime, daysOff);
        }
        else
        {
          if (RecordedMessageBean.isBuiltInMessage(messageId))
          {
            wavFile = RecordedMessageBean.getBuiltInWavFile(messageId);
            val10 = false;
            msgText = RecordedMessageBean.getBuiltInMsgText(messageId);
            val11 = false;
          }
          message = new RecordedMessageBean(callID, requestId, responseSeq, messageId, manNum, 
            pcsPhone, wavFile, msgText, smsText, responseDate, responseTime);
        }
        output = output + "\n" + message.toString();
        
        rowExist = rs.next();
      }
      rs.close();
      System.out.println("DB400 ResultSet Closed.");
    }
    catch (SQLException sqle)
    {
      sqle.printStackTrace();
    }
    return output;
  }
  
  public String getProcessedMessages()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String output = "";
    try
    {
      ResultSet rs = DBPortal.getResultSet("SELECT * FROM dbo.ProcessedMessages", this.sms_DBType, this.sms_ConnInfo);
      while (rs.next()) {
        output = 
        
          output + rs.getInt("Msg_ID") + "~~" + cleanText(rs.getString("Msg_Type")) + "~~" + cleanText(rs.getString("Msg_From")) + "~~" + cleanText(rs.getString("Msg_To")) + "~~" + cleanText(rs.getString("Msg_Title")) + "~~" + cleanText(rs.getString("Msg_Body")) + "~~" + cleanText(rs.getString("Msg_RequestedBy")) + "~~" + cleanText(sdf.format(rs.getTimestamp("Msg_RequestDateTime"))) + "~~" + cleanText(sdf.format(rs.getTimestamp("Msg_ProcessDateTime"))) + "~~" + cleanText(rs.getString("Msg_Provider")) + "~~" + cleanText(sdf.format(rs.getTimestamp("Msg_SendDateTime"))) + "~~" + cleanText(rs.getString("Msg_Confirmation")) + "~~" + cleanText(rs.getString("Msg_Status")) + "<BR>";
      }
      rs.close();
      System.out.println("getProcessedMessages: ResultSet closed.");
    }
    catch (SQLException sqle)
    {
      AppLog.addToLog("DBLog", sqle.getMessage() + "..." + sqle.getStackTrace());
    }
    return output;
  }
  
  public static String cleanText(String input)
  {
    String output = "";
    if (input != null)
    {
      output = input.replaceAll("~~", " ");
      output = output.replaceAll("<BR>", " ");
    }
    return output;
  }
}
