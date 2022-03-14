<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" 
	  		 import="pcs.recruit.access.*, java.util.logging.* " 
	  		 %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->



  
 	<form id="playStart" >
		<block>
			<prompt>
				Started.
			</prompt>
			<goto next="playLogged" />
		</block>
	</form>
 	    



<% 
try
{
	if( Integer.parseInt(request.getParameter("glo_requestSeq")) == 1) 
    {
	    DBAccessBean dba = new DBAccessBean();
	    
		String as_user = application.getInitParameter("as400_username");
		String as_password = application.getInitParameter("as400_password");
		String tmp_timeout = application.getInitParameter("as400_timeout");
		String as_host = application.getInitParameter(("as400_host"));
		int as_timeout = 60;
		if (as_user != null)
		    as_user = as_user.trim();
		if (as_user == null || as_user.length() == 0 )
		{
		    as_user = "pcs";
		}
		if (as_password != null)
		    as_password = as_password.trim();
		
		if (as_password == null || as_password.length() == 0 )
		    as_password= "pcs";
		
		if (as_host != null)
		    as_host = as_host.trim();
		
		if (as_host == null || as_host.length() == 0 )
		    as_host= "192.168.1.9";
		
		
		if (tmp_timeout != null )
		    tmp_timeout = tmp_timeout.trim();
		
		try
		{
		    as_timeout = Integer.parseInt(tmp_timeout);
		}
		catch (Exception e)
		{
		    as_timeout = 60;
		}
	    
	    
	    boolean isLogon = dba.logonDirect3(as_user,as_password,as_host,as_timeout);
	    if (isLogon )
	    {
		    RecruitParamsBean rpb = dba.getCallParameters((short)1);
		    session.setAttribute("ses_DBAccess",dba);
		    session.setAttribute("ses_params",rpb);
		    ResultSetBean rsb = new ResultSetBean();
		    session.setAttribute("ses_results",rsb);
	    }
	    else
	    { 
%>	       
	 	<form id="Unavailable" >
			<block>
 				<audio src="voices/sysUnavailable.wav">
					The recruiting system is currently unavailable.
					Please try again later or contact the recruiting center for further instructions.
				</audio>
				<assign name="glo_state" expr="'getData'" />
				<assign name="glo_resultLoc" expr="'logon'"/>
				<assign name="glo_result" expr="'failed'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
			</block>
		</form>
<!--  	</vxml> -->
		
<%
		out.println("</vxml>");
		return;
	    } 
    } /* end: if we need to logon*/
    
%>


 	<form id="playLogged" >
		<block>
			<prompt>
				Logged on.
			</prompt>
			
			<goto next="playMsgs" />
		</block>
	</form>
	
	
<% 	    
    

	    DBAccessBean dba = (DBAccessBean) session.getAttribute("ses_DBAccess");
	    RecruitParamsBean rpb = (RecruitParamsBean) session.getAttribute("ses_params");
	    int reqSeq =Integer.parseInt(request.getParameter("glo_requestSeq"));
	    int man = Integer.parseInt(request.getParameter("glo_man"));
	    int pin = Integer.parseInt(request.getParameter("glo_pin"));	    
	    AuthorizeBean auth = new AuthorizeBean(rpb.getCallSequence(),reqSeq,man,pin,"");
	    session.setAttribute("ses_authorize",auth);
	    dba.sendAccess(auth);
	    MessageSetBean msb =dba.retrieveMessages(rpb.getCallSequence(),
	            			reqSeq,rpb.getMaxMessages(),
	            			rpb.getTimeout(),RecruitParamsBean.SLEEP_INTERVAL);

/*	    msb =dba.retrieveMessages((long) 91 ,
				reqSeq,rpb.getMaxMessages(),
				rpb.getTimeout(),RecruitParamsBean.SLEEP_INTERVAL);
	    msb.setMessageId(rpb.getCallSequence());
*/	    

	    session.setAttribute("ses_messages",msb);
	    /** look at return codes of the message sets */
%>
	 	<form id="playMsgs" >
			<block>
				<assign name="glo_state" expr="'getData'" />
				<assign name="glo_resultLoc" expr="'getMessages'"/>
				<assign name="glo_result" expr="'ok'" />
<!--			<prompt>
					got the data. Going to submit.
				</prompt>
-->				
				<submit method="get" next="playMsgs.jsp" fetchtimeout="60s" />
			</block>
		</form>
<% 	    
}
catch (Exception e)
{
    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"DBAccessBean","getData","codeError",e);
    e.printStackTrace();
%>

	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'getData'" />
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
