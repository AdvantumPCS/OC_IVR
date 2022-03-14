<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*,java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->

<% 
try
{
    AuthorizeBean auth = (AuthorizeBean)session.getAttribute("ses_authorize");
    int val = 0;
    if (auth == null)
    	val = 223;
    else
    	val = 5;
%>

	 	<form id="startAuthen1" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'startup1'"/>
				<assign name="glo_result" expr="'ok'" />
				<assign name="glo_man" expr="0" />
				<assign name="glo_pin" expr="0"/>
				<assign name="glo_requestSeq" expr="<%= val %>" />
				
 			<audio src="voices/Welcome7.wav">
				Hello this is test 7. Going to end. <value expr="glo_requestSeq" />
			</audio>
			</block>
		</form>
<%    	
}    
catch (Exception e)
{

    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"pcsAuthen2","codeError","jspError",e);

}
%>	
</vxml>


