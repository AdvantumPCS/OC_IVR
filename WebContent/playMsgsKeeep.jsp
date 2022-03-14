<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*,java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->

<!-- 	 	<form id="startabc" >
			<block>
				<prompt>
					This is play messages.
				</prompt>				
 			</block>
		</form>
-->


<%
	try
	{
	    System.out.println("Started");
		MessageSetBean msb = (MessageSetBean)session.getAttribute("ses_messages");
/*		msb = new MessageSetBean(15);
		msb.addMessage(new RecordedMessageBean((long)183,1,1,2,123,"TestWav","TestWav text",new java.sql.Date(System.currentTimeMillis()),new java.sql.Time(System.currentTimeMillis())));
		msb.setLimitExceededStatus();
*/		
		String text = "";
		String wav = "";
		String retPoint="wrapup.jsp";
		int id = MessageId.NO_MSG_SPECEFIED;
		MessageBean mb = null;
		
		
		
		int status = msb.getStatus();
		if (status == MessageSetBean.STATUS_ERROR)
		{
		    wav = MessageId.WAV_SYSERROR;
		    text = MessageId.TEXT_SYSERROR;
		    id = MessageId.SYS_ERROR;
		    retPoint="wrapup.jsp";
		    mb = null;
		}
		else if (status == MessageSetBean.STATUS_TIMEOUT)
		{
		    wav = MessageId.WAV_UNAVAILABLE;
		    text = MessageId.TEXT_UNAVAILABLE;
		    id = MessageId.UNAVAILABLE;
		    retPoint="wrapup.jsp";
		    mb = null;
		}
		else if (status == MessageSetBean.STATUS_LIMIT_EXCEEDED)
		{
		    wav = MessageId.WAV_EXCEEDED;
		    text = MessageId.TEXT_EXCEEDED;
		    id = MessageId.LIMIT_EXCEEDED;
		    retPoint="wrapup.jsp";		    
		    mb = null;
		}
		else if (status == MessageSetBean.STATUS_NO_MESSAGES)
		{
		    wav = MessageId.WAV_SYSERROR;
		    text = MessageId.TEXT_SYSERROR;
		    id = MessageId.SYS_ERROR;
		    retPoint="wrapup.jsp";
		    mb = null;
		}
		else if (msb.isLastGoodBye() )
		{
		    wav= MessageId.WAV_LASTBYE;
		    text= MessageId.TEXT_LASTBYE;
		    mb = (MessageBean)(msb.getMessages().get(0));
		    id = mb.getMessageId();
		    retPoint="wrapup.jsp";
		}
		else if ( msb.isLastAttempt() )
		{
		    wav= MessageId.WAV_LASTATTEMPT;
		    text= MessageId.TEXT_LASTATTEMPT;
		    mb = (MessageBean)(msb.getMessages().get(0));
		    id = mb.getMessageId();
		    retPoint="pcsAuthen2.jsp";
		}
		else if ( msb.isIncorrect() )
		{
		    wav= MessageId.WAV_INCORRECT;
		    text= MessageId.TEXT_INCORRECT;
		    mb = (MessageBean)(msb.getMessages().get(0));
		    id = mb.getMessageId();
		    retPoint="pcsAuthen2.jsp";
		    System.out.println("isIncorrect");
		}
		else if (status == MessageSetBean.STATUS_OK)
		{
%>
<!-- 	 		<form id="playGoodSet" >
				<block>
					<assign name="glo_state" expr="'playMsgs'" />
					<assign name="glo_resultLoc" expr="'playGoodSet'"/>
					<assign name="glo_result" expr="'ok'" />
					<prompt> Going to play the good set</prompt>
					<var name="curMessage" expr="0" />
					<submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
				</block>
			</form>
			</vxml>
-->			
<%	
		return;
		}
		else
		    throw new Exception("Invalid State for MessageSetBean:" + status);
		System.out.println("Finished if then esle");
		if (mb == null)
		{
		    AuthorizeBean ab = (AuthorizeBean) session.getAttribute("ses_authorize");
		    
		    mb = new RecordedMessageBean( ab.getCallSequence(),(int) ab.getRequestSequence(),
		            			1, id, ab.getManNumber(), "", wav, text, "",
		            			new java.sql.Date(System.currentTimeMillis()), 
		            			new java.sql.Time(System.currentTimeMillis()));
		    /*
		    public RecordedMessageBean(long callSequence, int requestSequence,
		            int responseSequence, int messageId, int manNumber,String pcsPhone,
		            String wavFile, String msgText,String smsText, Date responseDate, Time responseTime)
		    */        
		}
		PlayBean pb = new PlayBean(mb, DoubleStates.UNKNOWN);
		ResultSetBean rsb = (ResultSetBean) (session.getAttribute("ses_results"));
		rsb.addMessage(pb);
		DBAccessBean dba = (DBAccessBean) (session.getAttribute("ses_DBAccess"));
		dba.logMessageSet(rsb);
		System.out.println("Going to generate the play Bad");
%>
<!-- 	 	<form id="playBadMsg" >
			<block>
				<prompt> Testing right here. </prompt>
				<audio src="<%= wav %>">
					<%= text %>
				</audio>
				<goto next="#badFollow"/>
			</block>
		</form>
		
-->		
<%	
		if(retPoint.equals("pcsAuthen2.jsp") )
		{
%>			
<!-- 		<form id="badFollow" >
			<block>
				<assign name="glo_state" expr="'playMsg'" />
				<assign name="glo_resultLoc" expr="'invalidPin'"/>
				<assign name="glo_result" expr="'ok'" />
				<submit method="get" next="<%= retPoint %>" fetchtimeout="60s" />
			</block>
		</form>
-->		
		
<%		   
		}
		else /* if(retPoint.equals("wrapup.jsp") ) */
		{
%>
<!-- 		<form id="badFollow" >
			<block>
				<assign name="glo_state" expr="'playMsg'" />
				<assign name="glo_resultLoc" expr="'playWrapUpExit'"/>
				<assign name="glo_result" expr="'ok'" />
				<submit method="get" next="<%= retPoint %>" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>
-->		
		
<%		
		
		}
	}
	catch (Exception e)
	{
   	 	//**log errors to the log4j  
   	    Logger logger = Logger.getLogger("pcs.jsp");
   	    logger.logp(Level.SEVERE,"playMsgs","codeError","jspError",e);
   	 	
    	e.printStackTrace();
%>
<!--  	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'playMsgs'" />
				<assign name="glo_resultLoc" expr="'error'"/>
				<assign name="glo_result" expr="'codeError'" />
				
				<prompt> Error in the Play Messages </prompt>
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>
-->		
<% 
	}
%>	
	


</vxml>
