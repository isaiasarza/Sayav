package SAYAV2.SAYAV2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification implements Serializable{
	
	
	/**
	 * 
	 */

	private List<String> registration_ids;
	
	private Map<String,String> data;
	
	
	/**
	 * 
	 * @param registration_ids : The tokens to send the notification
	 * @param title : The title of the notification	
	 * @param message : The message of the notification
	 */
	public Notification(List<String> registration_ids,String title, String message) {
		super();
		this.data = new HashMap<String,String>();
		this.registration_ids = registration_ids;
		this.createData(title, message);
	}

	public Notification() {
		super();
		this.registration_ids = new ArrayList<String>();
		this.data = new HashMap<String,String>();
	}
	
	public void addId(String registrationId){
		this.registration_ids.add(registrationId);
	}
	
	public void createData(String title, String message){
		this.data.put("title", title);
		this.data.put("message", message);
	}

	public List<String> getRegistration_ids() {
		return this.registration_ids;
	}

	public void setRegistrationIds(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Notification [registration_ids=" + registration_ids + ", data=" + data + "]";
	}
	
	
}
