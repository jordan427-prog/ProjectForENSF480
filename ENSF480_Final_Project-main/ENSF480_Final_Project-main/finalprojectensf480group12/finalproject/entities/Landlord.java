package entities;
import java.util.ArrayList;
import java.util.HashMap;
import control.SubscriptionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is the Landlord entity class. Contains name, property list and email
 */
public class Landlord {

   /* public static void main(String[] args) throws Exception {
        Landlord landlordTester = new Landlord();
        Manager managertest = new Manager();


    }*/


    private ArrayList<Property> properties;
    private String name;
    private String email;
    MyFlowLayout fl_landlord;

/**
 * Default constructor for landlord entity class
 * Initiates properties arraylist
 */
public Landlord() {
    properties = new ArrayList<Property>();
    fl_landlord = new MyFlowLayout(this);
}

public Landlord(String name, String email, String n) {
    properties = new ArrayList<Property>();
    this.name = name;
    this.email = email;
    
}


/**
 * Landlord constructor that accepts email and name
 * @param name name of landlord argument
 * @param email email of landlord string argument
 */
public Landlord(String name, String email) {
    properties = new ArrayList<Property>();
    this.name = name;
    this.email = email;
    fl_landlord = new MyFlowLayout(this);
}

/**
 * Landlord pay fee method. Allows for landlord to pay their fee
 * @param amount amount to pay
 */
public void payFee(double amount) {

}

/**
 * Landlord pay fee method. Searches through properties and calls property pay Fee for matching postal code
 * @param postal postal code to be searched
 */
public void payFee(String postal) {
   
    for(int i = 0; i<properties.size(); i++) {
        if(properties.get(i).getPostalCode().equals(postal)) {
            properties.get(i).payFee(properties.get(i));
        }
    }
}

/**
 * Getter for LandlordName
 * @return landlord's name
 */
public String getLandlordName() {
    return this.name;
}
/**
 * Getter for landlord email
 * @return landlord's email
 */
public String getLandlordEmail() {
    return this.email;
}

/**
 * Method to set name and email credentials of Landlord via user input from showInputDialog GUI
 */
public void setCredentials(){
    String in = (String)(JOptionPane.showInputDialog("Enter name"));
   
    this.name = in;
    in = (String)(JOptionPane.showInputDialog("Enter email"));
    this.email = in;
    //this.email = new JTextField(10).getText();

    System.out.println("Landlord registered!" + "\nYour credentials: " + this.name +"," +this.email);
}

/**
 * Adds a property with given arguments to properties
 * @param type Housing type, eg. apartment
 * @param bedrooms number of beds
 * @param bathrooms number of baths
 * @param furnished furnished or not
 * @param quadrant eg.NE, SW
 * @param postalCode postal code
 * @param state active, cancelled, rented or suspended
 */
public void addProperty(String type, int bedrooms, double bathrooms, String furnished, String quadrant, String postalCode, String state) {
    boolean furnishedBool;
    
        if(furnished.equals("yes")||furnished.equals("Yes") || furnished.equals("Furnished")){
            furnishedBool = true;
        }
        else {
            furnishedBool = false;
        }
        
        properties.add(new Property(type,bedrooms,bathrooms,furnishedBool,quadrant,postalCode,this, false, state));
        
    }

/**
 * removes a listing from database by its postal code
 * @param postal postal code to remove
 */
public void removeListing(String postal) {
    System.out.println("Searching for property to cancel");
    for(int i = 0; i<properties.size(); i++) {
        if(properties.get(i).getPostalCode().equals(postal)){
            System.out.println("Found listing to cancel");
            properties.get(i).setPostStatus(false);
           // properties.remove(properties.get(i));
            properties.get(i).removeListing(postal);
            break;
        }
    }
}


/**
 * Changes the state (Active, rented, cancelled, suspended) of a listing by its postal code
 * @param postal postal code of property to change
 */
public void changeState(String postal) {
    Property p = new Property();
    p.changeState(postal);
}

}






/*class Property{

    public Property(String type, double bedrooms, double bathrooms, String furnished, String quadrant, String postalCode, Landlord landlord){
        System.out.println("Registered property. Please pay fee to post this property!");
    }

}*/

/*
class landlordInitGUI {
    private MyFlowLayout fl;
    
    public landlordInitGUI() {
        fl = new MyFlowLayout();
    }

   
}
*/



//GUI self-made classes

/**
 * A FlowLayout class extending JFrame
 */
class MyFlowLayout extends JFrame /*implements ActionListener*/{
    private JFrame f;
    private JPanel p;
    public JButton payFeeButton;
    public JButton registerButton;
    public JButton newLandLordButton;
    private JButton existLandlordButton;
    JButton changeListingButton;
    JButton changeStateButton;
    myListener listener;
    Landlord landlordreference;

    /**
     * MyFlowLayout constructor. Adds buttons onto a frame + panel
     * @param landlord_in Landlord to reference for methods
     */
    public MyFlowLayout(Landlord landlord_in) {
        FlowLayout L = new FlowLayout();
        f = new JFrame("Welcome Landlord");
        p = new JPanel();
        payFeeButton = new JButton("Pay Fee");
        registerButton = new JButton("Register Property");
        newLandLordButton = new JButton ("I'm a new landlord");
        existLandlordButton = new JButton("I'm an existing landlord");
        changeListingButton = new JButton("Change listing");
        changeStateButton = new JButton("Change State");
        listener = new myListener(this);
        landlordreference = landlord_in;
        changeStateButton.addActionListener(listener);
        newLandLordButton.addActionListener(listener);
        registerButton.addActionListener(listener);
        changeListingButton.addActionListener(listener);

//ActionListener myListener = new ActionListener();

        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                JFrame frame = (JFrame)e.getSource();
                int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if(result==JOptionPane.YES_OPTION) {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            }
        });
        p.setLayout(L);
        p.add(registerButton);
       // p.add(payFeeButton);
        p.add(newLandLordButton);
        p.add(changeListingButton);
        p.add(changeStateButton);
       // newLandLordButton.addActionListener(this);
       // p.add(existLandlordButton);
        f.setContentPane(p);
        f.setVisible(true);
    }

   /* @Override
    public void actionPerformed(ActionEvent e) {
        this.getContentPane().setBackground(Color.pink);
        
    }*/
}

/**
 * MyListener class implementing ActionListener for event handling of buttons for myFlowLayout
 */
class myListener implements ActionListener{
    private MyFlowLayout frame;
/**
 * Constructor for myListener. Sets a myflowlayout reference to myflowlayout argument in
 * @param jf myflowlayout to be referenced
 */
    public myListener(MyFlowLayout jf) {
        frame = jf;
    }

 //overwritten actionPerformed method
    @Override
    public void actionPerformed(ActionEvent e) {
        //if newLandlord button pressed, call set credentials of referenced landlord to set name+email
        if(e.getSource()==frame.newLandLordButton) {
            System.out.println("Welcome new Landlord");
            frame.landlordreference.setCredentials();
        }
        //if register button pressed, use JOptionPane.showInputDialog to get user inputted criteria for property
        //this criteria is then stored into a HashMap corresponding to the specific criteria
        if(e.getSource()==frame.registerButton) {
            System.out.println("Time to register a property");
           // String[] propertyInfo = {"Type", "Bedrooms", "Bathrooms", "Furnished", "Quadrant", "Postal Code"};
            HashMap<String, String> propertyInfo = new HashMap<String, String>();
            try{
            propertyInfo.put("Type", (String)(JOptionPane.showInputDialog("Enter property type (eg. apartment)")));
            propertyInfo.put("Number of Bedrooms", (String)(JOptionPane.showInputDialog("Enter number of bedrooms")));
            propertyInfo.put("Number of Bathrooms", (String)(JOptionPane.showInputDialog("Enter number of bathrooms")));
            propertyInfo.put("Furnished/Unfurnished", (String)(JOptionPane.showInputDialog("Furnished/Unfurnished? (Type Furnished or yes for furnished, Type unfurnished or no for unfurnished")));
            propertyInfo.put("City Quadrant", (String)(JOptionPane.showInputDialog("Enter city quadrant(eg. SW for southwest)")));
            propertyInfo.put("Status", (String)(JOptionPane.showInputDialog("Enter property status (Active, rented, suspended, cancelled)")));
            propertyInfo.put("Postal Code", (String)(JOptionPane.showInputDialog("Enter postal code")));
            }catch(Exception j) {
                System.out.println("Invalid entry. Prompting re-entry to property fields");
                propertyInfo.put("Type", (String)(JOptionPane.showInputDialog("Enter property type (eg. apartment)")));
                propertyInfo.put("Number of Bedrooms", (String)(JOptionPane.showInputDialog("Enter number of bedrooms")));
                propertyInfo.put("Number of Bathrooms", (String)(JOptionPane.showInputDialog("Enter number of bathrooms")));
                propertyInfo.put("Furnished/Unfurnished", (String)(JOptionPane.showInputDialog("Furnished/Unfurnished? (Type Furnished or yes for furnished, Type unfurnished or no fro unfurnished")));
                propertyInfo.put("City Quadrant", (String)(JOptionPane.showInputDialog("Enter city quadrant(eg. SW for southwest")));
                propertyInfo.put("Status", (String)(JOptionPane.showInputDialog("Enter property status (Active, rented, suspended, cancelled)")));
                propertyInfo.put("Postal Code", (String)(JOptionPane.showInputDialog("Enter postal code")));
            }
            //call landlord reference's add property, with arguments from the above hashmap
           frame.landlordreference.addProperty(propertyInfo.get("Type"), Integer.parseInt(propertyInfo.get("Number of Bedrooms")), Double.parseDouble(propertyInfo.get("Number of Bathrooms")), propertyInfo.get("Furnished/Unfurnished"), propertyInfo.get("City Quadrant"), propertyInfo.get("Postal Code"), propertyInfo.get("Status"));
           //give the user option to pay fee and post the property
           int result = JOptionPane.showConfirmDialog(frame, "Registered property. Would you like to pay fee to post this property?\n***NOTE:PAYING FEE OVERWRITES STATUS TO ACTIVE!", "Pay Fee", JOptionPane.YES_NO_OPTION);
       // boolean paidFee = false;
            if(result==JOptionPane.NO_OPTION) { //previously CANCEL_OPTION
              System.out.println("Cancelled");
          }
          if(result==JOptionPane.YES_OPTION) {
              System.out.println("Posted!!!");
              //paidFee = true;
              frame.landlordreference.payFee(propertyInfo.get("Postal Code"));
          }
          
            // System.out.println(result);
             /* if(result==JOptionPane.YES_OPTION) {
                System.out.println("Paid fee. Property will be posted now");
               // frame.landlordreference.payFee(propertyInfo.get("Postal code"));
            }
            else{
                System.out.println("No fee paid. Property will not be posted");
            }*/
           
        }
        //if payFee button pressed, print statement
        if(e.getSource()==frame.payFeeButton) {
            System.out.println("Time to pay fee");
            
        }
        //if change listing button hit, prompt user for postal code to change and then call remove listing
        if(e.getSource()==frame.changeListingButton){
            
            String postalCancel = (String)(JOptionPane.showInputDialog("Enter postal code of Listing to cancel"));
            frame.landlordreference.removeListing(postalCancel);

        }
        //if changelisting button hit, prompt user to enter postal code for property to change and then call changestate function
        if(e.getSource()==frame.changeStateButton) {
            String postalCancel = (String)(JOptionPane.showInputDialog("Enter postal code of Listing to change"));

            frame.landlordreference.changeState(postalCancel);
        }
    }

}
