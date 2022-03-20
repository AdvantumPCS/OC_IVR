package pcs.recruit.access;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
   	 //JSONArray jobs=requstWorkRESTFul();
		getAccessToken();
		JSONArray jobs=this.getMessageFromElabour();
   	 for (int i = 0; i < jobs.length(); i++) {   		
   			JSONObject job     = jobs.getJSONObject(i);
   			int messageType    =Integer.parseInt(job.getString("messageType"));   			
   			String ship        =job.getString("ship");
   			System.out.println(ship+" in side for try get  "+jobs.length());
   			int employeeNumber =0;//Integer.parseInt(job.getString("employeeNumber"));
   			Long workDate      =0l;//job.getLong("workDate");
   			int workTime       =0;//job.getInt("workTime");
   			String wharf       =job.getString("wharf");
   			int berth          =0;//Integer.parseInt(job.getString("berth"));
   			int shift          =0;//Integer.parseInt(job.getString("shift"));
   			char doubling      ='N';//job.getString("doubling").charAt(0);
   			String phoneNumber =job.getString("phone");
   			Long responseDate  =0l;//Long.parseLong(job.getString("responseDate"));
   			int responseTime   =0;//Integer.parseInt(job.getString("responseTime"));
   			System.out.println(" Ship -"+ship); 			
   			List<String> dyo = new ArrayList<String>();
			JSONArray daysOff = job.getJSONArray("dayOff");
			String daysOffArr[] = new String[2];
			String messageText=job.getString("messageText");
			
			for (int x = 0; x < daysOff.length(); x++) {
				dyo.add(daysOff.getString(x));
			}
			dyo.toArray(daysOffArr);
			
			MessageBean message = null;
			if(messageType==99 || messageType==-1) {
				message = new PlainMessageBean(1l,1,1,
                        messageType,employeeNumber,phoneNumber,DataConversion.parseWorkDate(responseDate), DataConversion.parseWorkTime(responseTime),messageText);
                msgSet.addMessage(message);
			}
			else if (MessageBean.isWorkMessage(messageType))
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
    }catch(Exception e) {System.out.println("in side for tryget error  "+e.getMessage()); e.printStackTrace();}
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
 
public  void getAccessToken()  {
	String token="";
    try {
      
        StringBuilder data = new StringBuilder();        
        data.append("grant_type=password&username=tester&password=test");
        byte[] byteArray = data.toString().getBytes("UTF-8");
        URL url = new URL("http://192.168.100.201:8080/oauth/token");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con .setDoOutput(true);
        con.setRequestProperty("Authorization",
                "Basic YXBtMi1jbGllbnQ6MVowbS1yX3V4NkNZSzRZV203TQ==");
        OutputStream postStream = con.getOutputStream();
        postStream.write(byteArray, 0, byteArray.length);
        postStream.close();

        InputStreamReader reader = new InputStreamReader(con.getInputStream());
        BufferedReader in = new BufferedReader(reader);
        String json = in.readLine();
        System.out.println("Json String = " + json);

        JSONObject obj = new JSONObject(json.toString());
        token=obj.getString("access_token");
		System.out.println("token- " + token);
			
        in.close();
        con.disconnect();
   
        
    } catch(Exception e) {
        e.printStackTrace();
    }	
    this.token= token; 
}

public  JSONArray getMessageFromElabour()  {
	JSONArray jobs = new JSONArray();
  try {	
    URL url = new URL("http://192.168.100.201:8080/api/v1.0/requisition-scheduler/ivr-request-work?workerId="+this.employeeNum);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("POST");
    con.setConnectTimeout(5000);
    con .setDoOutput(true);
    con.setRequestProperty("Authorization", "Bearer "+this.token);
    con.setRequestProperty("Content-Type","application/json");   
    con.getOutputStream().write(this.pin.getBytes("UTF-8"));

    int respCode=con.getResponseCode();
    System.out.println("Response Code = " +respCode);

    InputStreamReader reader = new InputStreamReader(con.getInputStream());
    BufferedReader in = new BufferedReader(reader);
    String json = in.readLine();
    System.out.println("Json String = " + json);

    jobs = new JSONArray(json);
	
	
    in.close();
    con.disconnect();
  }catch(Exception e) { e.printStackTrace();}
  return jobs;
}

}
