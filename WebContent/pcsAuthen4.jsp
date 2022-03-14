<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*,java.util.logging.*,java.util.*,java.text.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />

	<!-- Place Content Here -->

<var name = "glo_SessionID" />
<assign name="glo_SessionID" expr="<%=session.getId()%>" />

<%

String currDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date());

try
{
    AuthorizeBean auth = (AuthorizeBean)session.getAttribute("ses_authorize");
    if (auth == null)
    {
%>
	 	<form id="startAuthen1" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen4'" />
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
	 	<form id="startAuthen4" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen4'" />
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
  		<!-- **************** MAN NUMBER ****************** -->
  		<!--  First Digit of Man Number -->
  		<field name="manNum1" modal="true" type="digits?length=1">
			<prompt timeout="5s">
				<!-- PCS Authentication Page 4. -->
		  		<audio src="voices/manNum.wav">
					Please enter your four digit man number.
				</audio>  					
			</prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumFirstDigit.wav">
					You have not started entering your Man number.
				</audio>
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - ' + _event +' - user has not started entering man number.'" />
				<goto nextitem="manNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on Man number First Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
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
					<!-- You entered the number <value expr="manNum1" />. -->					
					Please start over.
				</audio>
				<goto nextitem="manNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on Man number Second Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
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
					<!-- You entered <value expr="manNum1" /> <value expr="manNum2" />. -->					
					Please start over.
				</audio>
				<goto nextitem="manNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on Man number Third Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'manNumber'"/>
				<assign name="glo_man3" expr="manNum3" />
				<goto nextitem="manNum4"/>
			</filled>
		</field>
		<!--  Fourth Digit of Man Number -->
  		<field name="manNum4" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/manNumFourthDigit.wav">
					You have only entered the first three digits of your Man number.
				<!--You entered 
						<value expr="manNum1" />
						<value expr="manNum2" />
						<value expr="manNum3" />. -->
					Please start over.
				</audio>
				<goto nextitem="manNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on Man number Fourth Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
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
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - ' + _event +' - user has not started entering PIN number.'" />
				<goto nextitem="pinNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on PIN number First Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>				
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
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
					You have only entered one digit for your PIN number.
					<!-- You entered the number <value expr="pinNum1" />. -->					
					Please start over.
				</audio>
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - ' + _event +' - PIN Number: user only entered ' + pinNum1 + '.'" />
				<goto nextitem="pinNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many errors on PIN number Second Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
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
					<!-- You entered <value expr="pinNum1" /> <value expr="pinNum2" />. -->					
					Please start over.
				</audio>
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - ' + _event +' - PIN Number: user only entered ' + pinNum1 + '-' + pinNum2 + '.'" />
				<goto nextitem="pinNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on PIN number Third Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
			</catch>
			<filled>
				<assign name="glo_resultLoc" expr="'pinNumber'"/>
				<assign name="glo_pin3" expr="pinNum3" />
				<goto nextitem="pinNum4"/>
			</filled>
		</field>
		<!--  Fourth Digit of PIN Number -->
  		<field name="pinNum4" modal="true" type="digits?length=1">
			<prompt timeout="5s"></prompt>
			<catch event="nomatch noinput">
				<audio src="voices/pinNumFourthDigit.wav">
					You have only entered the first three digits of your PIN number.
			   <!-- You entered 
						<value expr="pinNum1" />
						<value expr="pinNum2" />
						<value expr="pinNum3" />. --> 
					Please start over.
				</audio>
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - ' + _event +' - PIN Number: user only entered ' + pinNum1 + '-' + pinNum2 + '-' + pinNum3 + '.'" />
				<goto nextitem="pinNum1"/>
			</catch>
			<catch event="nomatch noinput" count="4" >
				<audio src="voices/ProblemContactRecruit.wav">
					<!-- Too many error on PIN number Fourth Digit. -->
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'manPin'"/>
				<assign name="glo_result" expr="'exceedTries'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
			</catch>
			<filled>
				<audio>
					Thank you.
				<!--You entered Man number 
						<value expr="manNum1" />
						<value expr="manNum2" />
						<value expr="manNum3" />
						<value expr="manNum4" />
					and PIN number
						<value expr="pinNum1" />
						<value expr="pinNum2" />
						<value expr="pinNum3" />
						<value expr="pinNum4" />. --><!-- 
					Please hold while we access your messages. -->
				</audio>
				<log expr="'***** AppLog [Caller ID:' + session.callerid + ']  [<%=currDate %>] - Man # ' + manNum1 + '-' + manNum2 + '-' + manNum3 + '-' + manNum4 + ' and	Pin # ' + pinNum1 + '-' + pinNum2 + '-' + pinNum3 + '-' + pinNum4  + ' successfully captured.'" />
				<assign name="application.glo_resultLoc" expr="'pinNumber'"/>
				<assign name="application.glo_pin4" expr="pinNum4" />
				<assign name="application.glo_requestSeq" expr="application.glo_requestSeq +1" />
				<!--<submit method="get" next="getData.jsp" namelist="glo_state glo_man glo_pin glo_requestSeq" fetchtimeout="90s" fetchaudio="voices/pleaseHold.wav" />-->
				<submit method="get" 
						next="getData2.jsp" 
						namelist="glo_state glo_man1 glo_man2 glo_man3 glo_man4 glo_pin1 glo_pin2 glo_pin3 glo_pin4 glo_requestSeq glo_SessionID" 
						fetchtimeout="90s"
						fetchaudio="voices/placeHolder.wav" 
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
    logger.logp(Level.SEVERE,"pcsAuthen4","codeError","jspError",e);

     e.printStackTrace(); 
%>
	 	<form id="getError" >
			<block>
				<assign name="glo_state" expr="'pcsAuthen4'" />
				<assign name="glo_resultLoc" expr="'setup'"/>
				<assign name="glo_result" expr="'codeError'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result glo_SessionID" />
			</block>
		</form>
<% 
}
%>


</vxml>
