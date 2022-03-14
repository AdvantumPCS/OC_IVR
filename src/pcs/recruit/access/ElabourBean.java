package pcs.recruit.access;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import pcs.recruit.util.DataConversion;


public class ElabourBean {
 private String employeeNum;
 private String pin;
 private String token;
 private static HttpURLConnection conn;
 
 public ElabourBean(String usr,String pwd) {
	 this.employeeNum=usr;
	 this.pin=pwd;
	 this.token="t12dFg113";
 }
 


 public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}


public MessageSetBean retrieveMessages(long callID, int requestId, int maxMessages, int timeoutInterval, int sleepInterval) throws SQLException
 {

     boolean stop = false;
     long startTime = System.currentTimeMillis();
     long endTime = System.currentTimeMillis() + (timeoutInterval * 1000); 
     MessageSetBean msgSet = new MessageSetBean(maxMessages);
     
     msgSet = tryGetMessages(maxMessages); 
     
     return msgSet;

 }

private MessageSetBean tryGetMessages(int maxMessages) {
	MessageSetBean msgSet = new MessageSetBean(maxMessages);

	try { 
		System.out.println("About rest call");
   	 JSONArray jobs=requstWorkRESTFul();  	
   	 for (int i = 0; i < jobs.length(); i++) {   		
   			JSONObject job = jobs.getJSONObject(i);
   			int messageType   =job.getInt("message_type");   			
   			String ship       =job.getString("ship");
   			System.out.println(ship+" in side for try get  "+jobs.length());
   			int employeeNumber=job.getInt("employee_number");
   			Long workDate     =job.getLong("work_date");
   			int workTime      =job.getInt("work_time");
   			String wharf      =job.getString("wharf");
   			int berth         =job.getInt("berth");
   			int shift         =job.getInt("shift");
   			char doubling =job.getString("doubling").charAt(0);
   			String phoneNumber=job.getString("phone_number");
   			Long responseDate =job.getLong("response_date");
   			int responseTime =job.getInt("response_time");
   			System.out.println(" Ship -"+ship); 			
   			List<String> dyo = new ArrayList<String>();
			JSONArray daysOff = job.getJSONArray("days_off");
			String daysOffArr[] = new String[2];
			
			for (int x = 0; x < daysOff.length(); x++) {
				dyo.add(daysOff.getString(x));
			}
			dyo.toArray(daysOffArr);
			
			MessageBean message = null;
			if (MessageBean.isWorkMessage(messageType))
            {
                
                message = new WorkMessageBean(1l,1,
                        1, employeeNumber,
                        DataConversion.parseWorkDate(workDate),
            			DataConversion.parseWorkTime(workTime),wharf,
                        berth,ship,shift, doubling,phoneNumber,
                        DataConversion.parseWorkDate(responseDate), DataConversion.parseWorkTime(responseTime));
                        msgSet.addMessage(message);
            }
            else if (MessageBean.isDayOffMessage(messageType))
            {
                message = new DayOffMessageBean(1l,1,1,
                        messageType,employeeNumber,phoneNumber,DataConversion.parseWorkDate(responseDate), DataConversion.parseWorkTime(responseTime),daysOffArr);
                msgSet.addMessage(message);
            }
   		}
    }catch(Exception e) {System.out.println("in side for tryget error  "+e.getMessage());}
	return msgSet;
}

private MessageSetBean tryGetMessages2(int maxMessages) {
	MessageSetBean msgSet = new MessageSetBean(maxMessages);

	MessageBean message = null;
	message = new WorkMessageBean(1l, 1, 1, 1, DataConversion.parseWorkDate(20220313),
			DataConversion.parseWorkTime(15), "KW", 7, "ATALANTA", 2, 'N', "3583646",
			DataConversion.parseWorkDate(20220313), DataConversion.parseWorkTime(101623));
	msgSet.addMessage(message);
	String[] daysOff = { "Monday", "Tuesday" };
	message = new DayOffMessageBean(1l, 1, 1, 7, 1, "3583646", DataConversion.parseWorkDate(20220313),
			DataConversion.parseWorkTime(101623), daysOff);
	msgSet.addMessage(message);
	return msgSet;
}

public static JSONArray requstWorkRESTFul() throws Exception {
	String url = "http://localhost/work/work_data.json";
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	con.setRequestMethod("GET");
	con.setRequestProperty("User-Agent", "Mozilla/5.0");
	int responseCode = con.getResponseCode();
	StringBuffer response = new StringBuffer();
	if (responseCode == 200) {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;		
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
	}
	con.disconnect();
	JSONArray jobs = new JSONArray(response.toString());
	return jobs;
	
}


public String getEmployeeNum() {
	return employeeNum;
}
public void setEmployeeNum(String employeeNum) {
	this.employeeNum = employeeNum;
}
public String getPin() {
	return pin;
}
public void setPin(String pin) {
	this.pin = pin;
}
 
}
