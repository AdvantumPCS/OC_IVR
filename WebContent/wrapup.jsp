<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"	
	xml:lang="en-US" application="pcsStart.jsp">
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*, java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
<%
DBAccessBean dba = null;
ResultSetBean rsb = null;
MessageSetBean msb = null;
String sessionID = "[empty]";

try	{
	dba = (DBAccessBean) session.getAttribute("ses_DBAccess");
	rsb = (ResultSetBean) session.getAttribute("ses_results");
	msb = (MessageSetBean)session.getAttribute("ses_messages");
	String sms_dbType = application.getInitParameter("sms_dbtype");
	String sms_dbUsr = application.getInitParameter("sms_dbusr");
	String sms_dbPwd = application.getInitParameter("sms_dbpwd");
	String sms_dbHostName = application.getInitParameter("sms_dbhostname");
	String sms_dbName = application.getInitParameter("sms_dbname");
	String sms_user = application.getInitParameter("sms_username");
	String sms_password = application.getInitParameter("sms_password");
	String sms_onVal = application.getInitParameter("sms_on");
	String sms_workOnlyVal = application.getInitParameter("sms_workonly");
	// ShannonB 04.Sep.2015 2:53pm

	System.out.println("Entering wrapup.jsp...");
	
	sessionID = request.getParameter("glo_SessionID");
	
	boolean sms_on = true;
	boolean sms_workOnly = true;
	try {
		sms_on = Boolean.valueOf(sms_onVal);
		sms_workOnly = Boolean.valueOf(sms_workOnlyVal);
	}
	catch(Exception e) {
		System.out.println("Error getting SMS rules... defaulting to 'sms_on=true' and 'sms_workOnly=true'");
		System.out.println(e.getMessage());
	}
//	System.out.println("sms credentials: " + sms_user +  "|" + sms_password + ".");
	
/*
	if (sms_user != null)
	    sms_user = sms_user.trim();
	if (sms_user == null || sms_user.length() == 0 )
		{
	    sms_user = "shippingja";
		}
	if (sms_password != null)
	    sms_password = sms_password.trim();
	
	if (sms_password == null || sms_password.length() == 0 )
	    sms_password= "Digi1234";
	
	if (sms_onval != null )
	    sms_onval = sms_onval.trim();
	
	if (sms_onval.equalsIgnoreCase("false"))
	        sms_on = false;
	else
	    	sms_on = true;
*/
	SMSMessagesBean smb;
	if ( msb != null )
		{    
		/*smb= new SMSMessagesBean("shippingja","shippingja",true, msb,rsb);*/ 
		smb= new SMSMessagesBean(
				sms_dbType, sms_dbUsr, sms_dbPwd, sms_dbName,sms_dbHostName,
				sms_user,sms_password,sms_workOnly,sms_on, msb,rsb); 
		smb.generateAndSend();
		}
	
	if (dba != null && rsb != null && dba.isConnected())
		{
	    System.out.println("logged off [regular]");
		dba.logMessageSet(rsb);
		dba.logoff();
		}
	String gResult = request.getParameter("glo_result");
	if (gResult.equalsIgnoreCase("error") || gResult.equalsIgnoreCase("codeError") )
		{
	    String wav = MessageId.WAV_SYSERROR;
	    String txt = MessageId.TEXT_SYSERROR;
	    System.out.println("Session ID:" + sessionID + " " + gResult);
%>
 		<form id="playWrapError">
			<block>
				<audio src="<%= wav %>">
					<%= txt %>
				</audio>
				<goto nextitem="finishedWrap"/>
			</block>
		</form>
<%	
		}
%>
 	<form id="finishedWrap" >
		<block>
  		<!-- <prompt> That is all.</prompt> -->
			<exit/>
		</block>
	</form>
<%

	}
catch (Exception e)
	{
    e.printStackTrace(); 
    //**log errors to the log4j  
    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"wrapup","codeError","jspError", "Session " + sessionID + ": " + e);

%>
	<form id="finishedError" >
		<block>
			<exit/>
		</block>
	</form>
<% 
	}
finally
	{
	try	{
		if (dba != null && rsb != null && dba.isConnected())
			{
		    System.out.println("Session " + sessionID + " logged off [forced]");
			dba.logMessageSet(rsb);
			dba.logoff();
			dba = null;
			}
		}
	catch(Exception e) 
		{
		System.out.println("Unable to close DB400 Connection! Memory Leak...");
		}
	System.out.println("Session " + sessionID + " 'Finally' block in wrapup.jsp executed.");
	}
%>

</vxml>
