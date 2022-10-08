/**
 * Runner class. Contains the main method and the logic to test/demonstrate the application. Also is where the login is instantiated and used
 */

package entities;
import java.awt.Window;
import java.awt.Frame;

import control.SubscriptionController;

// make recurring login + test regular renter now!
/**
 * Runner class containing main
 */
public class Run {
    /**
     * main method. Creates login and calls necessary constructors after password check depending on the user selected user in login
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
       
        // do{
        Login a = new Login();
        String user = a.getUser();
      
        if(user=="Manager") {
            Manager managerTest = new Manager();
           
            a.passwordCheck("Manager");
 
        }
        else if (user=="Landlord") {
            Landlord landlordtester = new Landlord();
            
        }
        else if (user=="Registered Renter" ) {
            
            
            a.passwordCheck("Registered Renter");
            
            while(a.frame.isActive()) {}
            
            RegisteredRenter test = new RegisteredRenter(a.getEmail());
            
            test.openGUI();
           
            
       
    }
    else if (user=="Regular Renter"){
        RegularRenter test = new RegularRenter();
        test.searchData();
    }
    


   }
}

