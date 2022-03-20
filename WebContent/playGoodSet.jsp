<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml" 
	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*, java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
<%
	try
	{
		MessageSetBean msb = (MessageSetBean)session.getAttribute("ses_messages");
		ResultSetBean rsb = (ResultSetBean) session.getAttribute("ses_results");
		//DBAccessBean dba = (DBAccessBean) (session.getAttribute("ses_DBAccess"));


		if (msb.getStatus() != MessageSetBean.STATUS_OK )
		    throw new Exception("Error trying to play msgs: Message set bean status is" + msb.getStatus() );
		
		/* get the current message state */
		String tmpStart = request.getParameter("curMessage");
		int startMsg = 0;
		if (tmpStart != null)
		{
		    startMsg = Integer.parseInt(tmpStart);
		}
		else
		{
		    startMsg = 0;
		}

		
		if (startMsg == 0)
		{
			String introTxt = " ";
			String introWav = " ";
			if ( msb.getMessages().size() == 1)
			{
			    introTxt = "message.";
			    introWav = "voices/msgHeaderEnd.wav";
			}
			else 
			{
			    introTxt = "messages.";
			    introWav = "voices/msgPluralHeaderEnd.wav";
			}
%>
			<form id="playMsgHeader" >
				<block>
					<audio src="voices/msgHeaderBegin.wav">
						You have 
					</audio>
					<prompt>
						<%= EnglishNumberToWords.convert((long) msb.getMessages().size()) %>
					</prompt>
					<audio src="<%= introWav %>">
						<%= introTxt %>
						<break time="1000"/>
					</audio>
					<goto next="#playMsgIntro<%= startMsg %>"/>
				</block>
			</form>
<%		
		}
		
		if (msb.getStatus() != MessageSetBean.STATUS_OK )
		    throw new Exception("Error trying to play msgs: Message set bean status is" + msb.getStatus() );
		int curMessage = startMsg;
		if ( curMessage < msb.getMessages().size() )
		{
%>
  		<form id="playMsgIntro<%= curMessage %>" >
			<block>
				<audio src="voices/msgIntro.wav">
					Message
				</audio>
				<prompt>
					<%= EnglishNumberToWords.convert((long)(curMessage +1)) %>
					<break time="1000"/>
				</prompt>
				<goto next="#play<%= curMessage %>"/>
			</block>
		</form>


<%
		MessageBean msg= (MessageBean)(msb.getMessages().get(curMessage));
			if (msg.isWorkMessage() )
			{
%>
  			<form id="play<%= curMessage %>" >
				<block>
					<var name="curMessage" expr="<%= curMessage %>"/>
					<submit method="get" next="playWorkMessage.jsp" namelist="curMessage" fetchtimeout="60s" />
				</block>
			</form>
			
<%		    
			}
			else if (msg.isDayOffMessage() )
			{
		    	DayOffMessageBean dayMsg = (DayOffMessageBean)msg ;
		    	rsb.addMessage(new PlayBean(dayMsg,DoubleStates.UNKNOWN));
				//dba.logMessageSet(rsb);		    	
		    	String[] daysOff =  dayMsg.getDaysOff();
		    	String txtFirst = "";
		    	String txtSecond="";
		    	String wavFirst = "";

		    	if (daysOff == null || daysOff.length == 0)
		    	{
		    	    if ( dayMsg.isDayOffThisWeek() )
		    	    {
			    	    wavFirst = "voices/daysOffThisUnspeced.wav";
			    	    txtFirst = "Your days of for this week are unspecified.";
			    	    txtSecond = " ";
		    	    }
		    	    else /*( dayMsg.isDayOffNextWeek() ) */
		    	    {
			    	    wavFirst = "voices/daysOffNextUnspeced.wav";
			    	    txtFirst = "Your days of for next week are unspecified.";
			    	    txtSecond = " ";
		    	    }
		    	}
		    	else if (daysOff.length == 1)
		    	{
		    	    if ( dayMsg.isDayOffThisWeek() )
		    	    {
			    	    wavFirst = "voices/daysOffThisOne.wav";
			    	    txtFirst = "Your day off for this week is ";
			    	    txtSecond= daysOff[0];
		    	    }
		    	    else /*( dayMsg.isDayOffNextWeek() ) */
		    	    {
			    	    wavFirst = "voices/daysOffNextOne.wav";
			    	    txtFirst = "Your day off for next week is ";
			    	    txtSecond= daysOff[0];
		    	    }
		    	}
		    	else // days off >= 2
		    	{
		    	    if ( dayMsg.isDayOffThisWeek() )
		    	    {
			    	    wavFirst = "voices/daysOffThisMany.wav";
			    	    txtFirst = "Your days off for this week are ";
			    	    for ( int i = 0; i < daysOff.length -1; i++)
			    	    {
			    	        txtSecond = txtSecond + daysOff[i] + ", ";
			    	    }
			    	    txtSecond = txtSecond + "and " + daysOff[daysOff.length -1];
		    	    }
		    	    else /*( dayMsg.isDayOffNextWeek() ) */
		    	    {
			    	    wavFirst = "voices/daysOffNextMany.wav";
			    	    txtFirst = "Your days off for next week are ";
			    	    for ( int i = 0; i < daysOff.length -1; i++)
			    	    {
			    	        txtSecond = txtSecond + daysOff[i] + ", ";
			    	    }
			    	    txtSecond = txtSecond + "and " + daysOff[daysOff.length -1];
		    	    }
		    	}
%>
  				<form id="play<%= curMessage %>" >
					<block>
						<audio src="<%= wavFirst %>">
							<%= txtFirst %>
						</audio>
						<prompt>
							<%= txtSecond %>
							<break time="1000"/>
						</prompt>
						
						<var name="curMessage" expr="<%= curMessage %>" />
						<assign name="curMessage" expr="curMessage + 1" />
					    <submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
					</block>
				</form>
				
<%		   
			
			}
			else if (msg.isPlainTextMessage() )
			{
		    	PlainMessageBean pMsg = (PlainMessageBean)msg ;
		    	rsb.addMessage(new PlayBean(pMsg,DoubleStates.UNKNOWN));
				//dba.logMessageSet(rsb);		    	
		    	
		    	String txtFirst =pMsg.generateSMSText();
		    	String txtSecond="";
		    	String wavFirst = "";

%>
  				<form id="play<%= curMessage %>" >
					<block>
						<audio src="<%= wavFirst %>">
							<%= txtFirst %>
						</audio>
						<prompt>
							<%= txtSecond %>
							<break time="1000"/>
						</prompt>
						
						<var name="curMessage" expr="<%= curMessage %>" />
						<assign name="curMessage" expr="curMessage + 1" />
					    <submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
					</block>
				</form>
				
<%		   
			
			}
			else /* is a recorded message bean */
			{
		    	RecordedMessageBean rec = (RecordedMessageBean)msg ;
		    	rsb.addMessage(new PlayBean(msg,DoubleStates.UNKNOWN));
				//dba.logMessageSet(rsb);
%>
  			<form id="play<%= curMessage %>" >
				<block>
					<audio src="<%= rec.getWavFile() %>">
						<%= rec.getMsgText() %>
						<break time="1000"/>
					</audio>
					<var name="curMessage" expr="<%= curMessage %>" />
					<assign name="curMessage" expr="curMessage + 1" />
					<submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
				</block>
			</form>
			
<%		    
			}
		}	/* end if */
		
%>
		<menu id="doAgain" accept="excat"  scope="dialog" >
			<prompt timeout="5s">
				<audio src="voices/askPlayAgain.wav">
					To hear your messages again, press 1, otherwise press 2.
				</audio>
			</prompt>
			<choice dtmf="1" next="#replay"/>
			<choice dtmf="2" next="#done" />
			
			<catch event="nomatch noinput"  >
				<audio src="voices/invalidRepeat.wav">
						Invalid selection, please try again.
				</audio>
				<reprompt/>
			</catch>

			<catch event="nomatch noinput" count="4" >
				<audio src="voices/problemBye.wav">
					If you are having problems using the system, please contact the recruiting center.
				</audio>
				<goto next="#done" />
			</catch>
		</menu>

		<form id="done" scope="dialog">
			<block>
				<prompt>
					<audio src="voices/thanksBye.wav">
						Thank you for using the recruiting system, goodbye.
					</audio>
				</prompt>
				<var name="glo_state" expr="'playGoodSet'" />
				<var name="glo_resultLoc" expr="'done'"/>
				<var name="glo_result" expr="'ok'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>

	
		<form id="replay" scope="dialog">
			<block>
				<var name="curMessage" expr="0" />
				<submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
			</block>
		</form>
		
<%		            
//		out.println("</vxml>");
	}
	catch (Exception e)
	{
	    Logger logger = Logger.getLogger("pcs.jsp");
	    logger.logp(Level.SEVERE,"playGoodSet","codeError","jspError",e);
    	e.printStackTrace(); 
%>
	 	<form id="getError" >
			<block>
				<var name="glo_state" expr="'playGoodSet'" />
				<var name="glo_resultLoc" expr="'code'"/>
				<var name="glo_result" expr="'codeError'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>
<% 
	}
%>	
		
		

</vxml>
