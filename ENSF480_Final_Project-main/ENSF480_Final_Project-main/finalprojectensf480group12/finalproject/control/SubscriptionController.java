/**
 * @author Thessalonika Magadia 
 */

package control;

import java.util.ArrayList;

import GUI.SubscriptionGUI;
import database_management.DatabaseClass;
import entities.Property;
import entities.RegisteredRenter;


public class SubscriptionController {

	/**
	 * updates the table for notifications if new listing matches
	 * registered renter's search criteria and renter is subscribed
	 * 
	 * Note: should be called after a new listing has been added
	 */
	public void newListingNotiUpdate (Property p) {
		// get renter and see if they're subscribed and if any search criteria match
		// (renter may not have specific criteria for all criteria)
		DatabaseClass myDatabase = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabase.initializeConnection();
		ArrayList<RegisteredRenter> subbed = myDatabase.getSubscribers();
		int i = 0;
		while (i < subbed.size()) {
			// from the subbed find those who has a match in search criteria
			//System.out.println(subbed.get(i).getName());
			//subbed.get(i).getSearchCriteria().print();
			
			// if there is any matching criteria add notification to user
			if (comparePropertyCriteria(subbed.get(i).getSearchCriteria(), p)) {
				myDatabase.addNewListingNoti(subbed.get(i).getEmail(), p.getPostalCode());
			}
			i++;
		}
	}
	
	/**
	 * Returns true if property a and property b have at least 1 criteria in common,
	 * returns false otherwise
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean comparePropertyCriteria (Property a, Property b) {
		if (a.getBed() == b.getBed() || a.getBath() == b.getBath() || a.getType() == b.getType()
				|| a.getQuadrant() == b.getQuadrant() || a.isFurnished() == b.isFurnished()) {
			return true;
		} 
		return false;
	}
	
	/**
	 * updates the SUBNOTI table in the database and gets rid of unavailable properties 
	 * from notifications
	 */
	private void removeUnavailable () {
		ArrayList<String> unavailable = new ArrayList<>();
		
		DatabaseClass myDatabase = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabase.initializeConnection();
		unavailable = myDatabase.getUnavailableProperties();
		
		int i = 0;
		while (i < unavailable.size()) {
			myDatabase.removeNotifications(unavailable.get(i));
			i++;
		}
		myDatabase.closedbConnect();
	}
	
	/**
	 * Makes sure renter is on subscription (receives notifications)
	 * @param email
	 */
	public void subscribe (String email) {
		DatabaseClass myDatabase = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabase.initializeConnection();
        
        myDatabase.subscribeRenter(email);
        
        myDatabase.closedbConnect();
	}
	
	/**
	 * Removes renter from notifications
	 * @param email
	 */
	public void unsubscribe (String email) {
		DatabaseClass myDatabase = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabase.initializeConnection();
        
        myDatabase.unsubscribeRenter(email);
        
        myDatabase.closedbConnect();
	}
	
	/**
	 * Opens a GUI that lets registered renters check for notifications
	 * @param email
	 */
	public void checkNotifications (String email) {
		removeUnavailable();
		
        // get the notifications from data base and save into an List
        DatabaseClass myDatabaseConn = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabaseConn.initializeConnection();
        
        // saves all the notifications for properties (gets postal code) for specified user in an ArrayList
        ArrayList <String> postalcodes = myDatabaseConn.getNotifications(email); // use an actual email!!!!!!!
        ArrayList <Property> p = new ArrayList<Property> ();
        
        if (postalcodes.isEmpty()) {
        	System.out.println("no notifications");
        	// have no notifications pop-up
        	SubscriptionGUI notiWindow = new SubscriptionGUI(postalcodes, p, email);
        	notiWindow.noNotifications();
        	myDatabaseConn.closedbConnect(); // closes connection
        }
        else {
        	int i = 0;
	        while (i < postalcodes.size()) {
	        	p.add(myDatabaseConn.getPropertyInfo(postalcodes.get(i)));
	        	i++;
			}
	        
	        myDatabaseConn.close(); // closes connection
	        SubscriptionGUI notiWindow = new SubscriptionGUI(postalcodes, p, email); // creates a new notfication window
	        
	        // check if user was interested in a property
	        
        }
	}
}
