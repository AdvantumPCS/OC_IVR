<?xml version="1.0" encoding ="iso-8859-1"?>
<vxml version="2.1" xmlns="http://www.w3.org/2001/vxml"
	xml:lang="en-US" application="pcsStart.jsp" >
	<%@ page contentType="text/xml; charset=iso-8859-1" pageEncoding="iso-8859-1" import="pcs.recruit.access.*,java.text.*,pcs.recruit.util.*,java.util.logging.*" %>
	<meta name="GENERATOR" content="IBM WebSphere Voice Toolkit" />
	<!-- Place Content Here --> 
<%
	try
	{
	    int curMsg = Integer.parseInt((String)request.getParameter("curMessage")) ;
	    MessageSetBean msb = (MessageSetBean)session.getAttribute("ses_messages");
	    ResultSetBean rsb = (ResultSetBean)session.getAttribute("ses_results");
		DBAccessBean dba = (DBAccessBean) (session.getAttribute("ses_DBAccess"));
	    WorkMessageBean wmb = (WorkMessageBean) (msb.getMessages().get(curMsg));
	    
	    //handle the loop back- store/write the doubling status
	    String actionVal = (String)request.getParameter("curAction");
	    String doublingVal = (String)request.getParameter("doubling");
	    
	    if (doublingVal != null )
	    {
			//update the new state in the message bean.
			char dbVal = doublingVal.charAt(0);
			wmb.setDoubling(dbVal);
			rsb.updateDoublingStatus(new PlayBean(wmb,dbVal));
			dba.logMessageSet(rsb);		    	
			
			if (actionVal.equals("playWorkMessage"))
			{
%>			    
 		 		<form id="playGoodSet2" >
					<block>
						<var name="curMessage" expr="<%= curMsg %>" />
						<assign name="curMessage" expr="curMessage + 1" />
						<submit method="get" next="playGoodSet.jsp" namelist="curMessage" fetchtimeout="60s" />
					</block>
				</form>


<%			  
			out.println("</vxml>");
			}
			else //actionVal = hangup or error
			{
%>
			 	<form id="doWrapup" >
					<block>
						<var name="glo_state" expr="'playWorkMsg'" />
						<var name="glo_resultLoc" expr="'doWrapup'"/>
						<var name="glo_result" expr="<%= actionVal %>" />
						<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
					</block>
				</form>
 
<%			
			out.println("</vxml>");
			}
			
			return;
	    }
	    
	    if (!wmb.isValidMessage() )
	    {
	        rsb.addMessage(new PlayBean(wmb,DoubleStates.UNKNOWN));
			dba.logMessageSet(rsb);	
	        char dbl = wmb.getDoubling();

%>
		<form id="playUnavail" >
				<block>
					<prompt>
						<audio src="<%= MessageId.WAV_WORK_MISSING %>">
							<%= MessageId.TEXT_WORK_MISSING %> 
						</audio>
						<break time="1000"/>
					</prompt>
					<goto next="#backToPlay" />					
				</block>
			</form>
	
<%		
	    }
	    else //valid work msg
	    {
		    StringBuffer first = new StringBuffer("");
		    StringBuffer second = new StringBuffer("");
		    StringBuffer third = new StringBuffer("");
		    first.append("You have been assigned work on, ");
		    
		    SimpleDateFormat dayFmt = new SimpleDateFormat("EEEEEEEEE");
		    SimpleDateFormat monthFmt= new SimpleDateFormat("MMMMMMMMM");
		    SimpleDateFormat dayDateFmt= new SimpleDateFormat("dd");
		    String date = "";
		    date = dayFmt.format(wmb.getStartWorkDate()) + ", " + 
		    monthFmt.format(wmb.getStartWorkDate()) + ", ";
		    String dayVal = EnglishNumberToWords.convert(Long.parseLong(dayDateFmt.format(wmb.getStartWorkDate())));
		    date = date + dayVal+ ". ";
		    first.append(date);
		    
		    if (wmb.isPlayableShipBerth() )
		    	second.append(", on vessel, "+ wmb.getShip() + ", at berth, " + wmb.getBerth() + ". ");
		    else
		        second.append(" ");
		    
		    third.append(", start time, " );    
		   
		    
		    
		    SimpleDateFormat hourFmt = new SimpleDateFormat("hh");
		    SimpleDateFormat minFmt = new SimpleDateFormat("mm");
		    SimpleDateFormat ampmFmt = new SimpleDateFormat("a");
		    String hour = hourFmt.format(wmb.getStartWorkTime());
		    String min = minFmt.format(wmb.getStartWorkTime());
		    String ampm = ampmFmt.format(wmb.getStartWorkTime());
		    String time = EnglishNumberToWords.convert(Long.parseLong(hour)) + ", ";
		    String minVal="";
		    int intMin = Integer.parseInt(min);
		    if ( intMin == 0 )
		        minVal = " ";
		    else if (intMin <= 9 )
		        minVal = "Oh, " + EnglishNumberToWords.convert(intMin);
		    else
		        minVal = EnglishNumberToWords.convert(intMin);
		    time = time + minVal;
		    String indic = "";
		    if (ampm.equals("PM"))
		       indic = ", P M, ";
		    else
		        indic = ", A M, ";
		    time = time + indic;
		    
		    third.append(time);
	    
		    third.append(" on the ");
		    
		    third.append(DataConversion.shiftInWords(wmb.getShift()) +" shift." );
		    third.append("Please report to the Terminal for duty at least one hour before the start of your shift.");

	        rsb.addMessage(new PlayBean(wmb,DoubleStates.UNKNOWN));
	        char dbl = wmb.getDoubling();
	        /* System.out.println("Did the message prep");
	        System.out.println(first.toString());
	        */
	        
		    %>
			<form id="WorkMessage" >
				<block>
					<prompt>
						<sentence> <%= first.toString() %> </sentence>
						<sentence> <%= second.toString() %></sentence>
						<sentence> <%= third.toString() %> 	</sentence>
						<break time="1000"/>
					</prompt>
					<var name="workDoub" expr="'<%= dbl %>'"/>
					<if cond="workDoub == '<%=DoubleStates.AVAILABLE %>'">
						<goto next="#playIndic" />
					<elseif cond="workDoub == '<%=DoubleStates.UNAVAILABLE %>'" />	
						<goto next="#playIndic" />
					<elseif cond="workDoub == '<%=DoubleStates.ASK_YES %>'" />	
						<goto next="#askDoubling" />	
					<else/>		
						<goto next="#backToPlay" />					
					</if>
				</block>
			</form>
	
			
<%		
		    // play or get doubling status.
		    
		    if ( dbl== DoubleStates.AVAILABLE || dbl==DoubleStates.UNAVAILABLE)
		    {
		        String wav = "";
		        String txt = "";
		        if (dbl == DoubleStates.AVAILABLE)
		        {
		            wav = MessageId.WAV_WORK_DOUBLE;
		            txt = MessageId.TEXT_WORK_DOUBLE;
		        }
		        else
		        {
		            wav = MessageId.WAV_WORK_NODOUBLE;
		            txt = MessageId.TEXT_WORK_NODOUBLE;
		        }
		        rsb.updateDoublingStatus(new PlayBean(wmb,wmb.getDoubling()));		        
%>
  				<form id="playIndic" >
					<block>
						<prompt>
							<audio src="<%= wav %>">
								<%= txt %> 
							</audio>
							<break time="1000"/>
						</prompt>
						<goto next="#backToPlay"/>
					</block>
				</form>
<%		
		    } // end if
		    else
		    {
		        if(dbl == DoubleStates.ASK_YES)
		        {
%>
  					<form id="askDoubling" scope="dialog">
  						<field name="doubling" modal="true" type="digits?length=1"> 
							<prompt timeout="7s">
								<audio src="voices/askDoubling.wav">
									Are you available for doubling?
									For yes press 1.
									For no press 2.
								</audio>
							</prompt>
							<option dtmf="1" value="A"/>
							<option dtmf="2" value="U"/>
			
							<catch event="nomatch noinput"  >
								<audio src="voices/invalidDoubling.wav">
									That input was not correct. Please try again.
								</audio>
								<reprompt/>
							</catch>
			
							<catch event="nomatch noinput" count="4" >
								<assign name="doubling" expr="'X'"/>
								<audio src="noSelectDouble.wav">
									We have recorded no selection.
									Continuing.
									<break time="1000"/>
								</audio>
							</catch>
						</field>

						<catch event="error">
							<if cond="doubling == undefined">
								<assign name="doubling" expr="'X'"/>
							</if>
							<var name="curAction" expr="'error'" />
							<var name="curMessage" expr="<%= curMsg %>" />
							<submit namelist="doubling curMessage curAction" next="playWorkMessage.jsp" fetchtimeout="60s" />	
						</catch>

						<catch event="connection.disconnect.hangup">
							<if cond="doubling == undefined">
								<assign name="doubling" expr="'X'"/>
							</if>
							<var name="curAction" expr="'hangup'" />
							<var name="curMessage" expr="<%= curMsg %>" />
							<submit namelist="doubling curMessage curAction" next="playWorkMessage.jsp" fetchtimeout="60s" />	
						</catch>
		
						<filled>
							<if cond="doubling == undefined">
								<assign name="doubling" expr="'X'"/>
							</if>
							<var name="curAction" expr="'playWorkMessage'" />
							<var name="curMessage" expr="<%= curMsg %>" />
							<submit namelist="doubling curMessage curAction" next="playWorkMessage.jsp" fetchtimeout="60s" />	
						</filled>
		
					</form>
					
<%		            
		        } // end if ASK_YES
		    }
	    } // end isValidMessage
	    
%>
	 	<form id="backToPlay" >
			<block>
				<var name="curMessage" expr="<%= curMsg %>" />
				<assign name="curMessage" expr="curMessage +1"/>
				<submit namelist="curMessage" next="playGoodSet.jsp" fetchtimeout="60s" />	
			</block>
		</form>
		
<% 
	}
	catch (Exception e)
	{

	    Logger logger = Logger.getLogger("pcs.jsp");
	    logger.logp(Level.SEVERE,"playWorkMessage","codeError","jspError",e);
   
    	e.printStackTrace(); 
%>
  	 	<form id="getError" >
			<block>
				<var name="glo_state" expr="'playWorkMsg'" />
				<var name="glo_resultLoc" expr="'error'"/>
				<var name="glo_result" expr="'codeError'" />
				<submit method="get" next="wrapup.jsp" namelist="glo_state glo_resultLoc glo_result" fetchtimeout="60s" />
			</block>
		</form>
		
<% 
	}
%>	


</vxml>
