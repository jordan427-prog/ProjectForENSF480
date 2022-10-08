package entities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import control.RenterController;
import control.SubscriptionController;


public class RegisteredRenter extends RegularRenter {
	
	private String name;
	private String email;
	private String password;
	private Property searchCriteria;
	private RenterController window;
	private SubscriptionController sc;
	
	
	public RegisteredRenter(String n, String e, String p, Property sc) {
		name = n;
		email = e;
		password = p;
		searchCriteria = sc;
	}
	
	public RegisteredRenter(String email) {
		this.email = email;
		
		
	}

	/**
	 * Opens GUI for registered renter (homepage)
	 */
	public void openGUI () {
		window = new RenterController(email);
	}
	
	public void subscribe () {
		sc.subscribe(email);
	}
	
	public void unsubscribe () {
		sc.unsubscribe(email);
	}
	
	public Property getSearchCriteria () {
		return searchCriteria;
	}
	
	public void setEmail(String e) {
		email = e;
	}
	public String getName () {
		return name;
	}
	
	public String getEmail () {
		return email;
	}
	


}
