package database_management;
import java.sql.*;
import java.util.*;

import entities.Property;
import entities.RegisteredRenter;

public class DatabaseClass {
	public final String DBURL; //store the database url information
    public final String USERNAME; //store the user's account username
    public final String PASSWORD; //store the user's account password
    
    private Connection dbConnect;
    private ResultSet results;
    
    public DatabaseClass (String url, String user, String pass){
    	this.DBURL = url;
    	this.USERNAME = user;
    	this.PASSWORD = pass;
    }
    public void print() {
    	System.out.println("DBURL: " + DBURL);
    	System.out.println("USERNAME: " + USERNAME);
    	System.out.println("PASSWORD: " + PASSWORD);
    }
    /**
     *  Connects to the database (needs Registration object to be initialized first).
     */
    public Connection initializeConnection () {
    	try {
            dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
    	}
    	catch (SQLException e) {
            System.err.println("Error: could not connect to data base.");
            e.printStackTrace();
    	}
        
        return dbConnect;
    }
    
    /**
     * Closes connections to the database.
     */
    public void closedbConnect() {
        try {
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
    	try {
    		dbConnect.close();
            results.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves email message for landlord into the database (max message size is 1250 characters)
     * @param landlordName
     * @param message
     */
    public void saveEmailMessage (String landlordName, String message) {
    	if (message == null) {
    		return; 
    	}
        try {
            String query = "INSERT INTO LANDLORDINBOX (Name, Message) VALUES (?, ?)";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, landlordName);
            myStmt.setString(2, message);
            myStmt.executeUpdate();
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            
    }
    
    /**
     * Adds renter to the subscription list
     * @param emailAddr
     */
    public void subscribeRenter (String emailAddr) {
    	if (emailAddr == null) {
    		return;
    	}
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		myStmt.executeUpdate ("UPDATE RENTER SET subscriber = TRUE WHERE Email = "
    				+ "'" + emailAddr + "'");
    		myStmt.close();
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Removes renter from subscription list
     * @param emailAddr
     */
    public void unsubscribeRenter (String emailAddr) {
    	if (emailAddr == null) {
    		return;
    	}
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		myStmt.executeUpdate ("UPDATE RENTER SET subscriber = FALSE WHERE Email = "
    				+ "'" + emailAddr + "'");
    		myStmt.close();
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	
    	}
    }
    
    /**
     * Returns the an ArrayList of all the renters' email
     * who are subscribed
     * @return
     */
    public ArrayList<RegisteredRenter> getSubscribers () {
    	ArrayList<RegisteredRenter> subs = new ArrayList<RegisteredRenter>();
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		results = myStmt.executeQuery("SELECT * FROM renter WHERE Subscriber = TRUE");
    		
    		while (results.next()) {
    			Property p = new Property ();

    			p.setType(results.getString("Type"));
    			p.setBeds(results.getInt("Bedrooms"));
    			p.setBaths(results.getDouble("Bathrooms"));
    			p.setFurnished(results.getBoolean("Furnished"));
    			p.setQuadrant(results.getString("Quadrant"));
    			
    			String name = results.getString("Name");
    			String email = results.getString("Email");
    			String pass = results.getString("Password");
    			
    			RegisteredRenter r = new RegisteredRenter (name, email, pass, p);
    			p.print();
    			subs.add(r);
    		}
    		
    		myStmt.close();
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return subs;
    }
    
    /**
     * Returns an ArrayList of properties' postal code in the renter's notification
     * @param renterEmail
     * @return
     */
    public ArrayList<String> getNotifications (String renterEmail) {

    	ArrayList<String> postalcode = new ArrayList<String>();
    	if (renterEmail == null ) {
    		return postalcode;
    	}
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		results = myStmt.executeQuery("SELECT * FROM subnoti WHERE Email = "
    				+ "'" + renterEmail + "'");
    		
    		while (results.next()) {
    			postalcode.add(results.getString("Listing"));
    		}
    		
    		myStmt.close();
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    	
    	return postalcode;
    }
    
    /**
     * Adds postal code of new to corresponding email 
     * @param postal
     */
    public void addNewListingNoti (String email, String postal) {
    	if (email == null || postal == null) {
    		return; 
    	}
        try {
            String query = "INSERT INTO SUBNOTI (Email, Listing) VALUES (?, ?)";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, email);
            myStmt.setString(2, postal);
            myStmt.executeUpdate();
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Removes notifcations for properties that are no longer available
     * @param postal
     */
    public void removeNotifications (String postal) {
    	if (postal == null) {
    		return;
    	}
    	
        try {
        	System.out.println("deletes any notifications with this postalcode: " + postal);
            String query = "DELETE FROM subnoti WHERE Listing = ?";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, postal);
            myStmt.executeUpdate();
            myStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    /** 
     * Returns a String ArrayList of postal codes of properties that are not available
     * @return ArrayList<String>
     */
    public ArrayList<String> getUnavailableProperties () {
    	ArrayList<String> unavailable = new ArrayList<String>();
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		results = myStmt.executeQuery("SELECT * FROM properties WHERE State != 'Active'");
    		while (results.next()) {
    			unavailable.add(results.getString("PostalCode"));
    		}
    		myStmt.close();
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return unavailable;
    }
    

    /** 
     * Returns a list of postal codes of available properties
     * @return ArrayList<String>
     */
    public ArrayList<String> getAvailableProperties () {
    	ArrayList<String> available = new ArrayList<String>();
    	try {
    		Statement myStmt = dbConnect.createStatement();
    		results = myStmt.executeQuery("SELECT * FROM properties WHERE State = 'Active'");
    		while (results.next()) {
    			available.add(results.getString("PostalCode"));
    		}
    		myStmt.close();
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return available;
    }
    
    /**
     * Returns property information of the specified postal code
     * @param postalcode
     * @return
     */
    public Property getPropertyInfo (String postalcode) {
    	Property p = new Property();
    	if (postalcode == null) {
    		return p;
    	}
    	try {

    		Statement myStmt = dbConnect.createStatement();
    		results = myStmt.executeQuery("SELECT * FROM properties WHERE PostalCode = "
    				+ "'" + postalcode + "'");
    		if (results.next()) { // there are notifications
    			p.setPostalCode(results.getString("PostalCode"));
    			p.setType(results.getString("Type"));
    			p.setBeds(results.getInt("Bedrooms"));
    			p.setBaths(results.getDouble("Bathrooms"));
    			p.setFurnished(results.getBoolean("Furnished"));
    			p.setQuadrant(results.getString("Quadrant"));
    			p.setLandlord(results.getString("Landlord"));
    			p.setStatus(results.getBoolean("Status"));
    			p.setState (results.getString("State"));
    		}
    		
    		
    		
    		myStmt.close();
    	}
    	catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return p;
    }
}

