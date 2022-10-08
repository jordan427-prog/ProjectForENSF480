/**
 * Property entity class complete with control and boundary classes for GUI + Database
 */
package entities;

import java.sql.*;

import javax.management.Query;
import javax.swing.JOptionPane;
import control.SubscriptionController;

/**
 * entity property class, creates a property
 * Jordan Lundy
 */
public class Property {
    private String type;
    private int bedrooms;
    private double bathrooms;
    private Boolean furnished;
    private String quadrant;
    private Landlord landlord;
    private Boolean postStatus;
    private String postalCode;
    private String state;

    /**
     * default constructor
     */
    public Property()
    {
        this.bedrooms=0;
        this.bathrooms=0;
        this.furnished=false;
        this.quadrant="NULL";
        //landlord
        this.postStatus=false;
        this.postalCode="NULL";

    }
    /**
     * Property constructor with arguments, setting the private variables
     * @param postalcode postal code
     * @param type housing type
     * @param bedrooms beds
     * @param bathrooms bathrooms
     * @param furnished is it furnished?
     * @param quadrant city quadrant
     * @param landlord landlord overseeing
     * @param status posted or not
     * @param state active, rented, cancelled or suspended
     */
    public Property (String postalcode, String type, int bedrooms, double bathrooms, boolean furnished,
    String quadrant, String landlord, boolean status, String state) {
this.postalCode = postalcode;
this.type = type;
this.bedrooms = bedrooms;
this.bathrooms = bathrooms;
this.furnished = furnished;
this.quadrant = quadrant;
this.landlord = new Landlord(landlord, landlord+"@email.com");
this.postStatus = status;
this.state = state;
}

    /**
     * constructor bringing in all parameters
     * @param bedrooms-number of property bathrooms as a double
     * @param bathrooms-number of property bathrooms as a double
     * @param furnished-boolean indicating if the property is furnished
     * @param quadrant-city quadrant listed as a String
     * @param landlord-property's respective landlord
     * @param postStatus-boolean indicating if property is listed or not
     * @param postalCode-String for property's postal code
     */
    public Property(String type,int bedrooms, double bathrooms, Boolean furnished, String quadrant, String postalCode, Landlord landlord,Boolean postStatus, String state)
    {
        System.out.println("Registered property. Please pay fee to post this property!");
       this.type = type;
        this.bedrooms=bedrooms;
        this.bathrooms=bathrooms;
        this.furnished=furnished;
        this.quadrant=quadrant;
        this.landlord=landlord;
        this.postStatus=postStatus;
        this.postalCode=postalCode;
        this.state = state;
    }

//The following methods are getters and setters for the private Property variables:

    public String getPostalCode() {
        return this.postalCode;
    }
    public String getType() {
        return this.type;
    }
    public int getBedrooms()
    {
        return this.bedrooms;
    }
    public double getBathrooms()
    {
        return this.bathrooms;
    }
    public Boolean getFurnished()
    {
        return this.furnished;
    }
    public String getQuadrant()
    {
        return this.quadrant;
    }
    public Landlord getLandlord()
    {
        return this.landlord;
    }
    public Boolean getPostStautus()
    {
        return this.postStatus;
    }
    public String getLandlordName() {
        return this.landlord.getLandlordName();
    }
    public String getLandlordEmail() {
        return this.landlord.getLandlordEmail();
    }
    public void setPostStatus(boolean in) {
        this.postStatus = in;
    }
    public void setState(String psotal, String state) {
        this.state = state;
        //postingDatabase.changeState(psotal, state);
    }
    public String getState(){
        return this.state;

    }

    
	//more getters and setters:
	
	
	public int getBed () {
		return bedrooms;
	}
	
	public double getBath () {
		return bathrooms;
	}
	
	public boolean isFurnished () {
		return furnished;
	}

    public void setPostalCode (String p) {
		postalCode = p;
	}
	
	public void setType (String t) {
		type = t;
	}
	
	public void setBeds (int b) {
		bedrooms = b;
	}
	
	public void setBaths (double b) {
		bathrooms = b;
	}
	
	public void setFurnished (boolean f) {
		furnished = f;
	}
	
	public void setQuadrant (String q) {
		quadrant = q;
	}
	
	public void setLandlord (String l) {
		landlord = new Landlord(l, l+"@email.com", "nop");
	}
	
	public void setStatus (boolean s) {
		postStatus = s;
	}
	public void setState (String s) {
		state = s;
	}

    /**
     * Print property's fields
     */
    public void print() {
		System.out.println("type: " + type
				+ "bedrooms: " + bedrooms
				+ "bathrooms: " + bathrooms
				+ "furnished: " + furnished
				+ "quadrant: " + quadrant
				+ "state: " + state);
	}

	
	

    
    
/**
 * Removal of listing method. Calls postingController's removeListing method
 * @param postal postal code to remove
 */
public void removeListing(String postal) {
    System.out.println("Postal Code to remove: " + postal);
    postingController.removeListing(postal);
}

/**
 * Change state of property method. Uses a GUI dropdown menu to get a new state to be changed to
 * @param postal postal code where change is occurring
 */
public void changeState(String postal) {
  
   // String newState = (String)(JOptionPane.showInputDialog("Enter new state (Active, Rented, Cancelled or Suspended):"));
   String[] options = {"Active", "Rented", "Suspended", "Cancelled"};
  String newState = (String) JOptionPane.showInputDialog(null, "Choose new state: ", "Choose state", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
   postingController.changeState(postal, newState);
}

/**
 * Pay fee method to change state to active for paid property. 
 * Calls postingcontroller to post the listing
 * Adds property to the Notification update for notification of renters
 * @param toPost
 */
    public void payFee(Property toPost) {
        this.postStatus = true;
        this.setState(toPost.getPostalCode(),"Active");
        System.out.println("Paid fee. System now posted and active!");
        postingController.postListing(toPost);
        SubscriptionController s = new SubscriptionController();
        s.newListingNotiUpdate(toPost);
    }



/**
 * Posting control class
 */
    static class postingController{
        /**
         * post listing control method. created mySQL connection then upon successfuil connect, calls postingdatabase's postlisting method
         * @param toPost property to post
         */
        public static void postListing(Property toPost) {
            postingDatabase.createConnection();
            if(postingDatabase.dbConnected==true) {
                postingDatabase.postListing(toPost);
            }
        }

        /**
         * remove a listing from mySQL. Gets SQL connection then calls posting database boundary method to removal listing based on postal code argument
         * @param postal postal code to remove
         */
        public static void removeListing(String postal){
            postingDatabase.createConnection();
            if(postingDatabase.dbConnected==true) {
                postingDatabase.removeListing(postal);
            }
        }
        /**
         * change state of a property. First gets database connection, then calls posting Database class's changeState mrethod
         * @param postal postal code to change state of
         * @param newState new state to change to
         */
        public static void changeState(String postal, String newState) {
            postingDatabase.createConnection();
            if(postingDatabase.dbConnected==true) {
                postingDatabase.changeState(postal, newState);
            }
        }
    }

    /**
     * postingDatabase boundary class. Connects and executes updates plus retrieves porperty info from mySQL database
     */
    static class postingDatabase{
        static boolean dbConnected = false;
        static Connection dbConnect;
        /**
         * Create connection to database methofd
         */
    public static void createConnection() {
        try{
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
            dbConnected = true;
            System.out.println("Connected to database successfully!");
        } catch(SQLException e) {
            dbConnected = false;
           // e.printStackTrace();
            System.out.println("Unable to connect to database. Reverting to non-database version...");
        }
    }

    /**
     * Posts listing supplied to function to the properties table of database
     * @param tProperty property to enter into database
     */
    public static void postListing(Property tProperty) {
        try{
            Statement mySt = dbConnect.createStatement();
            String query = "INSERT INTO properties (Type, Bedrooms, Bathrooms, Furnished, Quadrant, Landlord, Status, PostalCode) VALUES ('"+tProperty.getType()+"', "+"'"+tProperty.getBedrooms()+"', '" + tProperty.getBathrooms() + "', '" + tProperty.getFurnished()+"', '" + tProperty.getQuadrant()+"', '" + tProperty.getPostalCode() + "');";
            String query2 = "INSERT IGNORE INTO landlord (Name, Email) VALUES ('"+tProperty.getLandlordName()+"', '"+tProperty.getLandlordEmail()+"');";
            mySt.executeUpdate(query2);
            query = "INSERT INTO properties (Type, Bedrooms, Bathrooms, Furnished, Quadrant, Landlord, Status, State, PostalCode) VALUES ('"+ tProperty.getType() + "', '" + tProperty.getBedrooms()+ "', '"+tProperty.getBathrooms()+"', "+tProperty.getFurnished()+", '"+tProperty.getQuadrant()+"', '"+tProperty.getLandlordName()+"', "+tProperty.getPostStautus()+", '"+tProperty.getState()+"', '"+tProperty.getPostalCode()+ "');";
            mySt.executeUpdate(query);
            mySt.close();
           // ResultSet results = mySt.executeQuery("SELECT * FROM PROPERTIES");
           // PreparedStatement std = dbConnect.prepareStatement("INSERT INTO PROPERTIES (PostalCode) VALUES (" + tProperty.getPostalCode());
           //std.executeUpdate();
           // std.close();
            //results.close();
            System.out.println("Updated database");
        }catch (SQLException E) {
            E.printStackTrace();
            System.out.println("Didn't update database");
        }
    }

    /**
     * Removes a property from properties table of rentalsystem mySQL database
     * @param postalCode postal code of property to remove
     */
    public static void removeListing(String postalCode) {
        try{
            String query = "delete from properties " + "where postalCode='"+postalCode + "';";
            Statement mySt = dbConnect.createStatement();
            mySt.executeUpdate(query);
            mySt.close();
            System.out.println("Updated database");
        }catch(SQLException e) {
            System.out.println("Didn't update database");
        }
    }

    /**
     * Change satte method. Changes the state of a property in the database based on supplied postal code and state string
     * @param postal postal code to change state of
     * @param newState new state to change to
     */
    public static void changeState(String postal, String newState) {
        try{
            String query = "UPDATE properties SET State='"+newState+"' "+"where PostalCode='"+postal+"';";
            Statement mySt = dbConnect.createStatement();
            mySt.executeUpdate(query);
            mySt.close();
            System.out.println("Updated database");
            dbConnect.close();
        }catch (SQLException e) {
            
        }
    }

    }

}




