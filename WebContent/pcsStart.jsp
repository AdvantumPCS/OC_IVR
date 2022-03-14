<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US" >
	<!-- contentType="application/voicexml+xml; charset=iso-8859-1" -->
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*, java.util.logging.* " %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
	<property name="inputmodes" value="dtmf"/>
<%
try
{
	session.removeAttribute("ses_messages");
	session.removeAttribute("ses_authorize");
	session.removeAttribute("ses_params");
	session.removeAttribute("ses_DBAccess");
	session.removeAttribute("ses_results");
}
catch (Exception e)
{
    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"DBAccessBean","getData","codeError",e);
    e.printStackTrace();
%>

	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'pcsStart'" />
				<assign name="glo_resultLoc" expr="'clearSession'"/>
				<assign name="glo_result" expr="'codeError'" />
				<prompt>
					Error occured.
				</prompt>
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>

<%
	out.println("</vxml>");
}
%>

	<var name= "glo_retries" expr="3" />
	<var name= "glo_state" expr="'begin'"/>
	<var name= "glo_resultLoc" expr="'none'"/>
	<var name= "glo_result" expr="'none'" />
	<var name= "glo_man" expr="0" />
	<var name= "glo_pin" expr="0" />
	<var name= "glo_requestSeq" expr="0" />
	
	<catch event="connection.disconnect">
		<assign name="glo_state" expr="'pcsStart'" />
		<assign name="glo_resultLoc" expr="'disconnect'"/>
		<assign name="glo_result" expr="'hangup'" />
		<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
	</catch>

	<catch event="maxspeechtimeout">
		<assign name="glo_state" expr="'pcsStart'" />
		<assign name="glo_resultLoc" expr="'maxspeech'"/>
		<assign name="glo_result" expr="'maxtimeout'" />
		<audio src="voices/procError.wav">
			We are unable to process your request. 
			Please contact the recruiting center for further instructions.
		</audio>
		<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
	</catch>

	<catch event="error" count="1">
		<audio src="voices/SystemError.wav">
			We are experiencing technical difficulties. 
			Please try again later or contact the recruiting center for further instructions.
		</audio>
		<assign name="glo_state" expr="'pcsStart'" />
		<assign name="glo_resultLoc" expr="'globalError1'"/>
		<assign name="glo_result" expr="'globalerror1'" />
		<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
	</catch>

	<catch event="error" >
		<assign name="glo_state" expr="'pcsStart'" />
		<assign name="glo_resultLoc" expr="'globalError2'"/>
		<assign name="glo_result" expr="'globalerror2'" />
		<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
	</catch>

	<form id="Welcome" >
		<block>
 			<audio src="voices/Welcome5.wav">
				Welcome to Oniel Charles Shipping Association's recruiting system.
			</audio>
				<!--<submit method="get" next="pcsAuthen2.jsp" fetchtimeout="60s" />-->
				<submit method="get" next="pcsAuthen4.jsp" fetchtimeout="60s" />
		</block>
	</form>
</vxml>
