<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US">
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here -->
	<form id="manPin" scope="dialog">
  		<field name="manNum" modal="true" type="digits?length=4"> 
			<prompt timeout="10s">
				<audio src="manNum.wav">
					Please enter your four digit man number.
				</audio>
			</prompt>
			<catch event="nomatch noinput"  >
				<audio src="manNum4Digits.wav">
					Your man number is four digits. Please try again.
				</audio>
				<reprompt/>
			</catch>
			
			<catch event="nomatch noinput" count="4" >
				<audio src="ProblemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<goto next="wrapup.vxml"/>
			</catch>
			
			<filled>
				<var name="application.glo_state" expr="'manNumber'"/>
				<var name="application.glo_man" expr="manNum" />
				<goto nextitem="pinNum"/>
			</filled>
		</field>
		
		<field name="pinNum" modal="true" type="digits?length=4"> 
			<prompt timeout="10s">
				<audio src="pinNum.wav">
					Please enter your four digit PIN number.
				</audio>
			</prompt>
			<catch event="nomatch noinput"  >
				<audio src="pinNum4Digits.wav">
					Your PIN number is four digits. Please try again.
				</audio>
				<reprompt/>
			</catch>
			
			<catch event="nomatch noinput" count="4" >
				<audio src="problemContactRecruit.wav">
					Please contact the recruiting center for assistance. Thank-you, goodbye.
				</audio>
				<goto next="wrapup.vxml"/>
			</catch>
			
			<filled>
				<assign name="application.glo_state" expr="'pinNumber'"/>
				<assign name="application.glo_pin" expr="pinNum" />
				<assign name="application.glo_requestSeq" expr="application.glo_requestSeq +1" />
				<submit method="get" next="getData.jsp" namelist="glo_state glo_man glo_pin glo_requestSeq"/>
			</filled>
		</field>  	
	</form>


</vxml>
