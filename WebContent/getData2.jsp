<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" 
	  		 import="pcs.recruit.access.*, java.util.logging.*,java.util.*,java.text.*" 
	  		 %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->



<!--  
 	<form id="playStart" >
		<block>
			<prompt>
				Started.
			</prompt>
			<goto next="playLogged" />
		</block>
	</form>
--> 	    



<%

String currDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date());

try
{
	System.out.println("Started get data");
	    ResultSetBean rsb = new ResultSetBean();
	    session.setAttribute("ses_results",rsb);
	    System.out.println("Started get data two");	    
	    RecruitParamsBean rpb = (RecruitParamsBean) session.getAttribute("ses_params");
	    session.setAttribute("ses_params",rpb);
	    int reqSeq =Integer.parseInt(request.getParameter("glo_requestSeq"));
	    System.out.println("Started get data 3");
		// ShannonB 02.Sep.2015 12:18pm
		// Man Number
		String man1 = request.getParameter("glo_man1");
		String man2 = request.getParameter("glo_man2");
		String man3 = request.getParameter("glo_man3");
		String man4 = request.getParameter("glo_man4");
		int man = Integer.parseInt(man1 + man2 + man3 + man4);
		String manString=man1 + man2 + man3 + man4;					
		// Pin Number
		String pin1 = request.getParameter("glo_pin1");
		String pin2 = request.getParameter("glo_pin2");
		String pin3 = request.getParameter("glo_pin3");
		String pin4 = request.getParameter("glo_pin4");
		int pin = Integer.parseInt(pin1 + pin2 + pin3 + pin4);
		String pinString=pin1 + pin2 + pin3 + pin4;

		// ShannonB 03.Sep.2015 2:46pm - Check if Man # or Pin # evaluates to "0".
		
		if((man==0) || (pin==0))
			{
			// Shut it down! Redirect to pcsAuthen[X].jsp to try again.
			System.out.println("Invalid credentials supplied. Will not attempt to authenticate.");
			%>
		 	<form id="invalidInput" >
				<block>
					<assign name="glo_state" expr="'getData2'" />
					<assign name="glo_resultLoc" expr="'logon'"/>
					<assign name="glo_result" expr="'userError'" />
	  				<prompt>
						Your credentials were entered incorrectly. Please try again.
					</prompt>
					<log expr="'***** AppLog [Caller ID:' + session.callerid + '] [<%=currDate %>] - Login Not Attempted due credentials entered incorrectly (0000).'" />
					<submit method="get" next="pcsAuthen4.jsp" fetchtimeout="60s" />
				</block>
			</form>
			<%			
			}
		else
			{
			System.out.println("Started get data 4");
			 ElabourBean eBean = new ElabourBean(manString,pinString);
						 
			 MessageSetBean msb2=eBean.retrieveMessages(1l,1,10,30,30);
			 
			 PlainMessageBean msg= (PlainMessageBean)(msb2.getMessages().get(0));
			 if(msb2.getMessages().size()==1 && msg.isErrorMessage()){
				 %>
				 	<form id="invalidInput" >
						<block>
							<assign name="glo_state" expr="'getData2'" />
							<assign name="glo_resultLoc" expr="'logon'"/>
							<assign name="glo_result" expr="'userError'" />
			  				<prompt>
								<%=msg.generateSMSText() %>]
							</prompt>
							<log expr="'***** AppLog [Caller ID:' + session.callerid + '] [<%=currDate %>] - Login Not Attempted due credentials entered incorrectly (0000).'" />
							<submit method="get" next="pcsAuthen4.jsp" fetchtimeout="60s" />
						</block>
					</form>
					<%	
			 }
			/*
		    AuthorizeBean auth = new AuthorizeBean(rpb.getCallSequence(),reqSeq,man,pin,"");
		    session.setAttribute("ses_authorize",auth);
		    dba.sendAccess(auth);
		    MessageSetBean msb =dba.retrieveMessages(rpb.getCallSequence(),
		            			reqSeq,rpb.getMaxMessages(),
		            			rpb.getTimeout(),RecruitParamsBean.SLEEP_INTERVAL);
*/
	/*	    msb =dba.retrieveMessages((long) 91 ,
					reqSeq,rpb.getMaxMessages(),
					rpb.getTimeout(),RecruitParamsBean.SLEEP_INTERVAL);
		    msb.setMessageId(rpb.getCallSequence());
	*/	    

		    session.setAttribute("ses_messages",msb2);
	
		    /** look at return codes of the message sets */
%>
		 	<form id="playMsgs" >
				<block>
					<assign name="glo_state" expr="'getData'" />
					<assign name="glo_resultLoc" expr="'getMessages'"/>
					<assign name="glo_result" expr="'ok'" />
			<prompt>
					<!-- 	got the data. Going to submit. -->
					</prompt>
				
					<submit method="get" next="playMsgs.jsp" fetchtimeout="60s" />
				</block>
			</form>
<%
			}
}
catch (Exception e)
{
    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"DBAccessBean","getData2","codeError",e);
    e.printStackTrace();
%>

	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'getData2'" />
				<assign name="glo_resultLoc" expr="'logon'"/>
				<assign name="glo_result" expr="'codeError'" />
<!--  				<prompt>
					Error occured.
				</prompt>
-->
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>

<% 
}
%>
</vxml>
