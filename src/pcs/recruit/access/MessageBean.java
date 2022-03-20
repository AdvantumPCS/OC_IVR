package pcs.recruit.access;

import java.sql.Date;
import java.sql.Time;

public abstract class MessageBean
  implements MessageId
{
  protected long callSequence = -1L;
  protected int requestSequence = -1;
  protected int responseSequence = -1;
  protected int messageId = -1;
  protected int manNumber = -1;
  protected String phoneNum = "";
  protected Date responseDate = null;
  protected Time responseTime = null;
  
  public String toString()
  {
    String ret = " callSeq=" + this.callSequence + " requestSeq =" + this.requestSequence + 
      " responseSeq=" + this.responseSequence + " MessageId=" + this.messageId + 
      " manNumber=" + this.manNumber + "phoneNum=" + this.phoneNum + " responseDate=" + this.responseDate + 
      " responseTime=" + this.responseTime;
    return ret;
  }
  
  public MessageBean(long callSequence, int requestSequence, int responseSequence, int messageId, int manNumber, String phoneNum, Date responseDate, Time responseTime)
  {
    this.callSequence = callSequence;
    this.requestSequence = requestSequence;
    this.responseSequence = responseSequence;
    this.messageId = messageId;
    this.manNumber = manNumber;
    this.phoneNum = phoneNum;
    this.responseDate = responseDate;
    this.responseTime = responseTime;
  }
  
  public int getMessageId()
  {
    return this.messageId;
  }
  
  public int getRequestSequence()
  {
    return this.requestSequence;
  }
  
  public Date getResponseDate()
  {
    return this.responseDate;
  }
  
  public int getResponseSequence()
  {
    return this.responseSequence;
  }
  
  public Time getResponseTime()
  {
    return this.responseTime;
  }
  
  protected void setCallSequence(long callSequence)
  {
    this.callSequence = callSequence;
  }
  
  protected void setManNumber(int manNumber)
  {
    this.manNumber = manNumber;
  }
  
  protected void setMessageId(int messageId)
  {
    this.messageId = messageId;
  }
  
  protected void setRequestSequence(int requestSequence)
  {
    this.requestSequence = requestSequence;
  }
  
  protected void setResponseDate(Date responseDate)
  {
    this.responseDate = responseDate;
  }
  
  protected void setResponseSequence(int responseSequence)
  {
    this.responseSequence = responseSequence;
  }
  
  protected void setResponseTime(Time responseTime)
  {
    this.responseTime = responseTime;
  }
  
  public long getCallSequence()
  {
    return this.callSequence;
  }
  
  public int getManNumber()
  {
    return this.manNumber;
  }
  
  public boolean isValidMessage()
  {
    if ((this.callSequence > 0L) && (this.requestSequence > 0) && 
      (this.responseSequence > 0) && (this.messageId > 0) && (this.manNumber > 0) && 
      (this.responseDate != null) && (this.responseTime != null)) {
      return true;
    }
    return false;
  }
  
  public static boolean isWorkMessage(int messageId)
  {
    if (messageId == 10) {
      return true;
    }
    return false;
  }
  
  public boolean isWorkMessage()
  {
    return isWorkMessage(this.messageId);
  }
  
  public static boolean isDayOffMessage(int messageId)
  {
    if ((messageId == 8) || 
      (messageId == 7)) {
      return true;
    }
    return false;
  }
  public  boolean isPlainTextMessage()
  {
    if (this.messageId == 99) {
      return true;
    }
    return false;
  }
  public  boolean isErrorMessage()
  {
    if (this.messageId == -1) {
      return true;
    }
    return false;
  }
  public boolean isDayOffMessage()
  {
    return isDayOffMessage(this.messageId);
  }
  
  public static boolean isRecordedMessage(int messageId)
  {
    if ((isWorkMessage(messageId)) || (isDayOffMessage(messageId))) {
      return false;
    }
    return true;
  }
  
  public boolean isRecordedMessage()
  {
    return isRecordedMessage(this.messageId);
  }
  
  public static boolean isBuiltInMessage(int messageId)
  {
    boolean retVal = false;
    switch (messageId)
    {
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 6: 
    case 7: 
    case 8: 
    case 10: 
    case 11: 
      retVal = true;
      break;
    case 5: 
    case 9: 
    default: 
      retVal = false;
    }
    return retVal;
  }
  
  public boolean isBuiltInMessage()
  {
    return isBuiltInMessage(this.messageId);
  }
  
  public void setPhoneNum(String phone)
  {
    this.phoneNum = phone;
  }
  
  public String getPhoneNum()
  {
    return this.phoneNum;
  }
  
  public abstract SMSMessage generateSMSMsg();
}
