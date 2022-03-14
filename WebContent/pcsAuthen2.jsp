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
				<goto next="#manPin"/>
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
				<assign name="glo_result" expr="'ok'" />
				<assign name="glo_man" expr="0" />
				<assign name="glo_pin" expr="0"/>
				<assign name="glo_requestSeq" expr="<%=auth.getRequestSequence() %>"/>
				<goto next="#manPin"/>
			</block>
		</form>
<%        
    }
%>	
	<form id="manPin" scope="dialog">
  		<field name="manNum" modal="true" type="digits?length=4">
  			<audio>
  				You will now be asked to enter your Man number.
  			</audio> 
			<prompt timeout="10s">
				<audio src="voices/manNum.wav">
					Please enter your four digit man number.
				</audio>
			</prompt>
			<catch event="nomatch">
				<audio>You entered Man number <value expr="manNum" /></audio>
				<audio src="voices/manNum4Digits.wav">
					Test: You did not enter four digits for your Man number. Please try again.
				</audio>
				<reprompt/>
			</catch>
			<catch event="noinput">
				<audio src="voices/manNum4Digits.wav">
					Test: You have not entered anything for your Man number. Please try again.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" />
			</catch>
			
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man" expr="manNum" />
				<goto nextitem="pinNum"/>
			</filled>
		</field>
		
		<field name="pinNum" modal="true" type="digits?length=4">
			<audio>
				TEST: You will now be asked to enter your PIN.
			</audio> 
			<prompt timeout="10s">
				<audio src="voices/pinNum.wav">
					Please enter your four digit PIN number.
				</audio>
			</prompt>
			<catch event="nomatch"  >
				<audio>You entered PIN number <value expr="pinNum" /></audio>
				<audio src="voices/pinNum4Digits.wav">
					Test: you have not entered four digits for your PIN number. Please try again.
				</audio>
				<reprompt/>
			</catch>
			<catch event="noinput"  >
				<audio src="voices/pinNum4Digits.wav">
					Test: you have not entered any digits for your PIN number. Please try again.
				</audio>
				<reprompt/>
			</catch>
			
			<catch event="nomatch noinput" count="4" >
				<audio src="problemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				
				<assign name="glo_state" expr="'pcsAuthen2'" />
				<assign name="glo_resultLoc" expr="'pinNum'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s"/>
			</catch>
			
			<filled>
				<audio>You entered Man number <value expr="manNum" /> and PIN number<value expr="pinNum" /></audio>
				<audio>Please hold while we access your messages.</audio>
				<assign name="application.glo_resultLoc" expr="'pinNumber'"/>
				<assign name="application.glo_pin" expr="pinNum" />
				<assign name="application.glo_requestSeq" expr="application.glo_requestSeq +1" />
				<!--<submit method="get" next="getData.jsp" namelist="glo_state glo_man glo_pin glo_requestSeq" fetchtimeout="90s" fetchaudio="voices/pleaseHold.wav" />-->
				<submit method="get" 
						next="getData.jsp" 
						namelist="glo_state glo_man glo_pin glo_requestSeq" 
						fetchtimeout="90s" 
						>
				</submit>
				
			</filled>
		</field>  	
	</form>	
<% 	    
	
}
catch (Exception e)
{

    Logger logger = Logger.getLogger("pcs.jsp");
    logger.logp(Level.SEVERE,"pcsAuthen2","codeError","jspError",e);

     e.printStackTrace(); 
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
