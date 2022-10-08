package control;

import GUI.EmailGUI;
import database_management.DatabaseClass;

public class EmailController {
	/*
	private static String landlord;
	public EmailController (String selectedPropertyLandlord) {
		landlord = selectedPropertyLandlord;
	}*/
	public void createEmail(String landlord) {
		//System.out.println("hello");
        EmailGUI window = new EmailGUI(); // creates a new email window
        
        // wait until there is a message available
        while (!window.getMessageAvailable()) {
        	
        }
        
        System.out.println("message to be sent: " + window.getMessage());
        String emailMessage = window.getMessage();
        
        // save to database
        DatabaseClass myDatabaseConn = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        
        myDatabaseConn.initializeConnection();
        myDatabaseConn.saveEmailMessage (landlord, emailMessage); // change landlord to actual name!!
        myDatabaseConn.closedbConnect(); // closes connection
        
        window.messageSentFrame(); // lets user know message was successfully sent
        
        // close window
        window.dispose();
	}
}