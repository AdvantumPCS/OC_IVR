<?xml version="1.0" encoding ="iso-8859-1"?>


<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US" application="pcsBegin.vxml">
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
	<!-- Try to log the varibales in root -->
	<form id="log_data">
		<block>
			<log expr="'*******Root Variables****************'"/>
			<log expr="application.cur_state + ' ' + application.cur_man"/>
			
			<log expr="'*******request Variables****************'"/>
			<log expr= " <%= request.getParameter("cur_man")  %> "/>
		</block>
	</form>

</vxml>
