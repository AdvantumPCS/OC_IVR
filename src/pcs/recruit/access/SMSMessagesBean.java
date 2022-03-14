package pcs.recruit.access;

import com.advantumpcs.shannonb.AppData;
import com.frameworks.apptools.AppLog;
import com.frameworks.apptools.Utils;
import com.frameworks.datatools.DBPortal;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMSMessagesBean
{
  public static final int INITIALIZED = 1;
  public static final int GENERATED = 2;
  public static final int SENT = 3;
  public static final int ERROR = 4;
  private int state = 4;
  private String dbType = "";
  private String dbUsr = "";
  private String dbPwd = "";
  private String dbName = "";
  private String dbHostName = "";
  private String userName = "";
  private String password = "";
  boolean workMessagesOnly = true;
  boolean smsOn = true;
  private MessageSetBean messages = null;
  private ResultSetBean results = null;
  private LinkedList outBox = null;
  
  public SMSMessagesBean(
		  String dbType,
		  String dbUsr,
		  String dbPwd,
		  String dbName,
		  String dbHostName,
		  String userName, 
		  String password,
		  boolean workMessagesOnly,
		  boolean smsOn, 
		  MessageSetBean messages, 
		  ResultSetBean results)
    throws Exception
  {
    this.dbType = dbType;
    this.dbUsr = dbUsr;
    this.dbPwd = dbPwd;
    this.dbName = dbName;
    this.dbHostName = dbHostName;    
	this.userName = userName;
    this.password = password;
    this.workMessagesOnly = workMessagesOnly;
    this.smsOn = smsOn;
    this.messages = messages;
    this.results = results;
    
    this.outBox = new LinkedList();
    this.state = 1;
  }
  
  public void generateMessages()
  {
    if (this.smsOn) {
      if (this.messages.isOkStatus()) {
        generateOkSet();
      }
    }
  }
  
  private int sendMessage(SMSMessage sms)
  {
    int output = 3;
    
    String saveStatus = save_NewMessage(sms);
    if (saveStatus.equals("Message Saved.")) {
      output = 2;
    }
    return output;
  }
  
  public String save_NewMessage(SMSMessage msg)
  {
    AppData data = new AppData(this.dbType, this.dbHostName, this.dbUsr, this.dbPwd, this.dbName);
    
    String msgType = "";
    String msgFrom = "";
    String msgTo = "";
    String msgTitle = "";
    String msgBody = "";
    String msgRequestedBy = "";
    String msgRequestDateTime = "";
    String msgStatus = "";
    String msgRef1 = "";
    String msgRef2 = "";
    
    String output = "";
    
    String strSQL = "INSERT INTO dbo.NewMessages(Msg_To, Msg_Title, Msg_Body, Msg_RequestDateTime, Msg_Ref1, Msg_Ref2) VALUES";
    
    SMSMessage sms = msg;
    msgTo = sms.getPhoneNumber();
    msgTitle = "IVR";
    msgBody = sms.getMessage();
    msgRequestDateTime = Utils.formatDate(Utils.currentDateTime(), "dd-MMM-yyyy HH:mm:ss");
    
    strSQL = strSQL + "('" + msgTo + "','" + msgTitle + "','" + msgBody + "','" + msgRequestDateTime + "','" + msgRef1 + "','" + msgRef2 + "');";
    
    AppLog.addToLog("New Message - Date/Time: " + msgRequestDateTime + " " + " Tel #" + msgTo + ";");
    output = strSQL;
    String saveStatus = DBPortal.RunCommand(strSQL, "MSSQL", data.getConnInfo("MSSQL"));
    if (saveStatus.startsWith("OK")) {
      output = "Message Saved.";
    }
    return output;
  }
  
  public String save_NewMessage(MessageBean mb)
  {
    AppData data = new AppData(this.dbType, this.dbHostName, this.dbUsr, this.dbPwd, this.dbName);
    
    String msgType = "";
    String msgFrom = "";
    String msgTo = "";
    String msgTitle = "";
    String msgBody = "";
    String msgRequestedBy = "";
    String msgRequestDateTime = "";
    String msgStatus = "";
    String msgRef1 = "";
    String msgRef2 = "";
    
    String output = "";
    
    String strSQL = "INSERT INTO dbo.NewMessages(Msg_To, Msg_Title, Msg_Body, Msg_RequestDateTime, Msg_Ref1, Msg_Ref2) VALUES";
    
    SMSMessage sms = mb.generateSMSMsg();
    msgTo = sms.getPhoneNumber();
    msgTitle = "MN " + Utils.pad(mb.getManNumber(), "0000");
    msgBody = sms.getMessage();
    msgRequestDateTime = Utils.formatDate(mb.getResponseDate(), "dd-MMM-yyyy") + " " + Utils.formatDate(mb.getResponseTime(), "HH:mm:ss");
    msgRef1 = "" + mb.getMessageId();
    msgRef2 = mb.getCallSequence() + "-" + Utils.pad(mb.getRequestSequence(), "00") + "-" + Utils.pad(mb.getResponseSequence(), "00");
    
    strSQL = strSQL + "('" + msgTo + "','" + msgTitle + "','" + msgBody + "','" + msgRequestDateTime + "','" + msgRef1 + "','" + msgRef2 + "');";
    
    AppLog.addToLog("New Message: Ref #: " + 
      msgRef2 + " Date/Time: " + msgRequestDateTime + " " + 
      "Man #: " + mb.getManNumber() + " Tel #" + mb.getPhoneNum() + ";");
    
    output = strSQL;
    String saveStatus = DBPortal.RunCommand(strSQL, "MSSQL", data.getConnInfo("MSSQL"));
    if (saveStatus.startsWith("OK")) {
      output = 
      
        "Message # " + mb.getCallSequence() + "-" + mb.getRequestSequence() + "-" + mb.getResponseSequence() + " saved successfully.";
    }
    return output;
  }
  
  public void generateAndSend()
  {
    generateMessages();
  }
  
  public void sendMessages()
  {
    boolean successful = true;
    if (this.smsOn)
    {
      for (int i = 0; i < this.outBox.size(); i++) {
        if (sendMessage((SMSMessage)this.outBox.get(i)) != 2)
        {
          successful = false;
          Logger logger = Logger.getLogger("pcs.code");
          logger.logp(Level.SEVERE, "SMSMessagesBean", "sendMessages", "Problem Sending message:" + (SMSMessage)this.outBox.get(i));
        }
      }
      if (successful) {
        this.state = 3;
      } else {
        this.state = 4;
      }
    }
  }
  
  private void addToOutBox(SMSMessage newMsg)
  {
    if (this.outBox.size() == 0)
    {
      this.outBox.add(newMsg);
    }
    else
    {
      SMSMessage last = (SMSMessage)this.outBox.getLast();
      if (newMsg.getPhoneNumber().equals(last.getPhoneNumber()))
      {
        if (!last.tryAppendMessage(newMsg.getMessage())) {
          this.outBox.add(newMsg);
        }
      }
      else {
        this.outBox.add(newMsg);
      }
    }
  }
  
  private void generateOkSet()
  {
    try
    {
      for (int i = 0; i < this.messages.getMessages().size(); i++)
      {
        MessageBean msg = (MessageBean)this.messages.getMessages().get(i);
        if(this.workMessagesOnly)
        {
	        if (MessageBean.isWorkMessage(msg.messageId))
	        {
	          String saveStatus = save_NewMessage(msg);
	          System.out.println("Msg Save Status: " + saveStatus);
	        }
	        else
	        {
	          String msgID = msg.callSequence + "-" + msg.requestSequence + "-" + msg.responseSequence;
	          System.out.println("Msg # " + msgID + " ignored.");
	        }
        }
        else
        {
	          String saveStatus = save_NewMessage(msg);
	          System.out.println("Msg Save Status: " + saveStatus);
        }
      }
      this.state = 2;
    }
    catch (Exception e)
    {
      Logger logger = Logger.getLogger("pcs.code");
      logger.logp(Level.SEVERE, "SMSMessagesBean", "generateBadSet", "code Error", e);
    }
  }
}
