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
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'startup1'"/>
				<assign name="glo_result glo_sessionID" expr="'ok'" />
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
	 	<form id="startAuthen3" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'startup2'"/>
				<assign name="glo_result glo_sessionID" expr="'ok'" />
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
  		<!-- **************** MAN NUMBER ****************** -->
  		<!--  First Digit of Man Number -->
  		<field name="manNum1" modal="true" type="digits?length=1">
			<prompt timeout="5s">
				<audio>PCS Authentication Page 3</audio>
		  		<audio src="voices/manNum.wav">
					Please enter your four digit man number.
				</audio>  					
			</prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumFirstDigit.wav">
					You have not started entering your Man number.
				</audio>
				<assign name="MyEvent" expr="_event" />
				<log expr="'***** TRAPPED A' + _event +'EVENT *****'" />
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man1" expr="manNum1" />
				<goto nextitem="manNum2"/>
			</filled>
		</field>
		<!--  Second Digit of Man Number -->
  		<field name="manNum2" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumSecondDigit.wav">
					You have only entered one digit for your Man number.
					You entered the number <value expr="manNum1" />.					
					Please enter the remaining three digits.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man2" expr="manNum2" />
				<goto nextitem="manNum3"/>
			</filled>
		</field>		
		<!--  Third Digit of Man Number -->
  		<field name="manNum3" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumThirdDigit.wav">
					You have only entered the first two digits of your Man number.
					You entered <value expr="manNum1" /> <value expr="manNum2" />.					
					Please enter the remaining two digits.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man3" expr="manNum3" />
				<goto nextitem="manNum4"/>
			</filled>
		</field>
		<!--  Second Digit of Man Number -->
  		<field name="manNum4" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumFourthDigit.wav">
					You have only entered the first three digits of your Man number.
					You entered 
						<value expr="manNum1" />
						<value expr="manNum2" />
						<value expr="manNum3" />.
					Please enter the last digit.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man4" expr="manNum4" />
				<audio>Thank you.</audio>
				<goto nextitem="pinNum1"/>
			</filled>
		</field>
			
		<!-- **************** PIN NUMBER ****************** -->
		
  		<!--  First Digit of PIN Number -->
  		<field name="pinNum1" modal="true" type="digits?length=1">
			<prompt timeout="5s">
		  		<audio src="voices/pinNum.wav">
					Please enter your four digit PIN number.
				</audio>  					
			</prompt>
			<catch event="nomatch noinput">
				<audio src="voices/pinNumFirstDigit.wav">
					You have not started entering your PIN number.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'pinNumber'"/>
				<assign name="glo_pin1" expr="pinNum1" />
				<goto nextitem="pinNum2"/>
			</filled>
		</field>
		<!--  Second Digit of PIN Number -->
  		<field name="pinNum2" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/pinNumSecondDigit.wav">
					You have only entered one digit for your PINnumber.
					You entered the number <value expr="pinNum1" />.					
					Please enter the remaining three digits.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'pinNumber'"/>
				<assign name="glo_pin2" expr="pinNum2" />
				<goto nextitem="pinNum3"/>
			</filled>
		</field>		
		<!--  Third Digit of PIN Number -->
  		<field name="pinNum3" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/pinNumThirdDigit.wav">
					You have only entered the first two digits of your PIN number.
					You entered <value expr="pinNum1" /> <value expr="pinNum2" />.					
					Please enter the remaining two digits.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'pinNumber'"/>
				<assign name="glo_pin3" expr="pinNum3" />
				<goto nextitem="pinNum4"/>
			</filled>
		</field>
		<!--  Second Digit of PIN Number -->
  		<field name="pinNum4" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/pinNumFourthDigit.wav">
					You have only entered the first three digits of your PIN number.
					You entered 
						<value expr="pinNum1" />,
						<value expr="pinNum2" />,
						<value expr="pinNum3" />.
					Please enter the last digit.
				</audio>
				<reprompt/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</catch>
			<filled>
				<audio>
					You entered Man number 
						<value expr="manNum1" />
						<value expr="manNum2" />
						<value expr="manNum3" />
						<value expr="manNum4" />
					and PIN number
						<value expr="pinNum1" />
						<value expr="pinNum2" />
						<value expr="pinNum3" />
						<value expr="pinNum4" />.
				</audio>
				
				<assign name="application.glo_resultLoc" expr="'pinNumber'"/>
				<assign name="application.glo_pin4" expr="pinNum4" />
				<assign name="application.glo_requestSeq" expr="application.glo_requestSeq +1" />
				<!--<submit method="get" next="getData.jsp" namelist="glo_state glo_man glo_pin glo_requestSeq" fetchtimeout="90s" fetchaudio="voices/pleaseHold.wav" />-->
				<submit method="get" 
						next="getData2.jsp" 
						namelist="glo_state glo_man1 glo_man2 glo_man3 glo_man4 glo_pin1 glo_pin2 glo_pin3 glo_pin4 glo_requestSeq" 
						fetchtimeout="90s"
						fetchaudio="voices/pleaseHold.wav" 
						>
				</submit>
			</filled>
		</field>		
		
				
		<field name="pinNum" modal="true" type="digits?length=4">
			<audio>
				Thank you.
			</audio> 
			<prompt timeout="10s">
				<audio src="voices/pinNum.wav">
					Please enter your four digit PIN number.
				</audio>
			</prompt>
			<catch event="nomatch"  >
				<audio>You entered PIN number <value expr="pinNum" /></audio>
				<audio src="voices/pinNum4Digits.wav">
					You have not entered four digits for your PIN number. Please try again.
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
				
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'pinNum'"/>
				<assign name="glo_result glo_sessionID" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" fetchtimeout="60s"/>
			</catch>
			
			<filled>
				<audio>
					You entered Man number 
						<value expr="manNum1" />
						<value expr="manNum2" />
						<value expr="manNum3" />
						<value expr="manNum4" />
					and PIN number
						<value expr="pinNum" />.
				</audio>
				<audio>Please hold while we access your messages.</audio>
				<assign name="application.glo_resultLoc" expr="'pinNumber'"/>
				<assign name="application.glo_pin" expr="pinNum" />
				<assign name="application.glo_requestSeq" expr="application.glo_requestSeq +1" />
				<!--<submit method="get" next="getData.jsp" namelist="glo_state glo_man glo_pin glo_requestSeq" fetchtimeout="90s" fetchaudio="voices/pleaseHold.wav" />-->
				<submit method="get" 
						next="getData2.jsp" 
						namelist="glo_state glo_man1 glo_man2 glo_man3 glo_man4 glo_pin glo_requestSeq" 
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
    logger.logp(Level.SEVERE,"pcsAuthen3","codeError","jspError",e);

     e.printStackTrace(); 
%>
	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen3'" />
				<assign name="glo_resultLoc" expr="'setup'"/>
				<assign name="glo_result glo_sessionID" expr="'codeError'" />
				<assign name="glo_result glo_sessionID" expr="session.id" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_sessionID" />
			</block>
		</form>
<% 
}
%>


</vxml>
