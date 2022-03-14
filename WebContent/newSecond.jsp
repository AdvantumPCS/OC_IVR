<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*,java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
<% 
try
{
    AuthorizeBean auth = (AuthorizeBean)session.getAttribute("ses_authorize");
    if (auth == null)
    {
%>
	 	<form id="startAuthen1" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'startup1'"/>
				<assign name="glo_result" expr="'ok'" />
				<assign name="glo_man" expr="0" />
				<assign name="glo_pin" expr="0"/>
				<assign name="glo_requestSeq" expr="0" />
			</block>
		</form>
<%        
    }
    else
    {
%>
	 	<form id="startAuthen2" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'startup2'"/>
				<assign name="glo_result" expr="ok" />
				<assign name="glo_man" expr="0" />
				<assign name="glo_pin" expr="0"/>
				<assign name="glo_requestSeq" expr="<%=auth.getRequestSequence() %> " />
			</block>
		</form>
<%        
    }
}
catch (Exception e)
{

    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"pcsAuthen2","codeError","jspError",e);
%>
	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'setup'"/>
				<assign name="glo_result" expr="'codeError'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
			</block>
		</form>
<% 
}
%>
</vxml>	