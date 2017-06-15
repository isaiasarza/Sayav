package SAYAV2.SAYAV2.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import SAYAV2.SAYAV2.model.NotificationMovil;

public class PostGCM {
	/**
	 * @param apiKey
	 * @param notificacion
	 */
	public static void post(String apiKey, NotificationMovil notificacion){
		try {
//			1.URL
			URL url = new URL("https://fcm.googleapis.com/fcm/send");
			System.out.println(apiKey);
			
//			2.Open Connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
//			3.Specify Post Method
			conn.setRequestMethod("POST");
			
//			4. Set the headers
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestProperty("Authorization", "key="+apiKey);
	       
	        conn.setDoOutput(true);
	        
	        // 5. Add JSON data into POST request body 
	        
            //`5.1 Use Jackson object mapper to convert Contnet object into JSON
            ObjectMapper mapper = new ObjectMapper();
 
            // 5.2 Get connection output stream
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            
            System.out.println(conn.getOutputStream());
 
            // 5.3 Copy Content "JSON" into 
            mapper.writeValue(wr, notificacion);
            System.out.println( mapper.writeValueAsString(notificacion));
 
            // 5.4 Send the request
            wr.flush();
 
            // 5.5 close
            wr.close();
 
            // 6. Get the response
            int responseCode = conn.getResponseCode();
            System.out.println("Sending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
 
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
 
            // 7. Print result
            System.out.println(response.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
