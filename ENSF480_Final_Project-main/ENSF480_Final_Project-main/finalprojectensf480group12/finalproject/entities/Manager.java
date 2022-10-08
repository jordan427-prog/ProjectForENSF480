/**
 * Manager entity class complete with GUI and Database boundaries and controls
 */

package entities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


/**
 * entity Manager class
 * Jordan Lundy
 */
public class Manager {
    private String name;
    private String email;
    private String password;
    static MyPanel pl;

    /**
     * default constructor for Manager
     */
    public Manager()
    {
        this.name="NULL";
        this.email="NULL";
        this.password="NULL";
        pl = new MyPanel(this);
    }
    /**
     * constructor bringing in all parameters
     * @param name-Managers name
     * @param email-managers email
     * @param password-managers password
     */
    public Manager(String name,String email,String password)
    {
        this.name=name;
        this.email=email;
        this.password=password;
        pl = new MyPanel(this);
    }

    /**
     * Method that requests a manager periodical summary report
     * @param period period to start from
     */
    public void periodicalSummaryReport(int period) {

    }

    /**
     * calls propery's removeListing function with supplied postalcode argument
     * @param postal postal code to remove
     */
    public void removeListing(String postal) {
        //System.out.println(postal);
       //listingControl.removeListing(postal);
       Property p = new Property();
       
       p.removeListing(postal);
    }

    /**
     * Method to change the state of a property -> calls property's changeState method
     * @param postal postal code to change state of
     */
    public void changeState(String postal) {
        Property p = new Property();
        p.changeState(postal);
    }

    /**
     * Method to change the state of a listing
     * @param state state to be changed
     */
    public void changeListingState(String state) {

    }
    
    /**
     * Method for a manager to change/set the fee via its period and/or amount
     * @param period period of fees to be set
     * @param amount amount for fees to be set
     */
    public void setFees(int period, double amount){

    }
    
    /**
     * Method for manager to only set amount of fees
     * @param amount amount fees should be now set to
     */
    public void setFeeAmount() {
        feeController.setFeeAmount();
    }
    
    /**
     * Method to set a new period for the fees
     * @param period new period for fees to be set to
     */
    public void setFeePeriod() {
        feeController.setFeePeriod();
    }
    
    /**
     * Method to extract renter's information
     * @return all info on renters
     */
    public void getRenterInfo() {
        GetInfo.getRenterInfo();
    }
    
    /**
     * Method to extract prtoperty's information
     * @return all info on properties
     */
    public void getPropertyInfo() {
        GetInfo.getPropertyInfo();
    }
    
    /**
     * Method to extract landlord's information
     * @return all info on landlords
     */
    public void getLandlordInfo() {
        GetInfo.getLandlordInfo();
    }

    /**
     * calls getPeriodical() in periodicalcontroller subclass. A request for periodical summary
     */
    public void getPeriodical() {
        PeriodicalController.getPeriodical();
    }

    /**
     * Changes an existing panel's visibility
     * @param bool to change visibiity to true or false
     */
    public void setVisible(boolean bool) {
        pl.setVisible(bool);
    }
/**
 * GUI class to prompt periodical database entry and display
 */
    static class PeriodicalGUI extends JFrame{
        static JFrame f;
        static JLabel l;
        static JLabel l2;
        static JLabel l3;
        static JLabel l4;
        /**
         * periodical summary request function Sets up labels and GUI aspects to print to GUI the report
         * @param toPrint String array of what to print in the report
         */
        public static void getPeriodical(String[] toPrint) {
            f = new JFrame("Periodical Summary");
           // l = new JLabel(toPrint[0]+"\n" + toPrint[1]+"\n"+toPrint[2]+"\n"+toPrint[3]);
           l = new JLabel(toPrint[0]);
           l2 = new JLabel(toPrint[1]); 
           l3 = new JLabel(toPrint[2]);
           l4 = new JLabel(toPrint[3]);
           JPanel p = new JPanel();
            p.add(l);
            p.add(l2);
            p.add(l3);
            p.add(l4);
            f.add(p);
            f.setSize(600, 600);
            f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            f.setVisible(true);

        }
    }
    /**
     * Periodical controller class
     */
    static class PeriodicalController{
        
        /**
         * getPeriodical control method. Gets database info and saves into an array. Then passes that array into the GUI class's periodical function to print to GUI
         */
        public static void getPeriodical() {
            String[] toPrint = periodicalDatabase.getPeriodical();
            PeriodicalGUI.getPeriodical(toPrint);
        }
    }
/**
 * periodical database class to extract database info for periodical report
 */
    static class periodicalDatabase {
        static boolean dbConnected = false;
        static Connection dbConnect;
        public static String[] getPeriodical() {
            String[] toPrint = new String[4];

            try{
                dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
                dbConnected = true;
                System.out.println("Connected to database successfully!");

                Statement mySt = dbConnect.createStatement();
                
                String query = "select count(*) from properties";
      //Executing the query
      ResultSet rs = mySt.executeQuery(query);
      //Retrieving the result
      rs.next();
      int count = rs.getInt(1);
      toPrint[0] = "Total Listings: " + String.valueOf(count);
      System.out.println(toPrint[0]);
      query = "select count(*) from properties where state='Rented'";
      rs = mySt.executeQuery(query);
      rs.next();
      count = rs.getInt(1);
      toPrint[1] = "Total Rented: " + String.valueOf(count);
      System.out.println(toPrint[1]);
      query = "select count(*) from properties where state='Active'";
      rs = mySt.executeQuery(query);
      rs.next();
      count = rs.getInt(1);
      toPrint[2] = "Total Active: " + String.valueOf(count);
      System.out.println(toPrint[2]);
      String x = "Printing all rented properties:    \n";
      query = "select * from properties where state='rented'";
      rs = mySt.executeQuery(query);
      while(rs.next()) {
          x+=" Landlord: " +rs.getString(6) + "Postal Code: " + rs.getString(9)+"\n";
      }
      toPrint[3] = x;
      System.out.println(toPrint[3]);


                mySt.close();
                rs.close();
      dbConnect.close();
               // ResultSet rs = mySt.executeQuery("select * from properties");
            }catch (SQLException e) {

            }


            return toPrint;
        }
    }

    /**
     * Listing control class to call remove listing
     */
    static class listingControl{
        public static void removeListing(String postal) {
           // listingDatabase.removeListing(postal);
        }
    }

    /**
     * listingDatabase control class
     */
    static class listingDatabase{
        //public static 
    }

    /**
     * Fee GUI class. Prompts entry from user to set fee period and amount
     */
    static class FeeGUI{
        /**
         * Gets user entry to set fee amount via GUI
         * @return user entered fee amount
         */
        public static double setFeeAmount() {
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount"));
            return amount;
        }
        /**
         * prompts user for fee period via GUI
         * @return user entered fee period
         */
        public static int setFeePeriod() {
            int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter amount"));
            return amount;
        }
    }

    /**
     * Fee database boundary class. 
     */
    static class FeeDatabase{
        public static void setFeePeriod(int period) {

        }
        public static void setFeeAmount(double amount){

        }
    }

    /**
     * fee control class. calls fee gui setters to get user entered amounts, then calls the dfatabase fee setters with those values
     */
    static class feeController{
        /**
         * Calls GUI to get entered fee amount
         */
        public static void setFeeAmount() {
            double amount = FeeGUI.setFeeAmount();
            FeeDatabase.setFeeAmount(amount);
            
        }
        /**
         * calls GUI to get fee period from user
         */
        public static void setFeePeriod() {
            int period = FeeGUI.setFeePeriod();
            FeeDatabase.setFeePeriod(period);
        }
    }

    /**
     * Get info control class for manager to retrieve information
     */
    static class GetInfo{
        /**
         * get property info database calling method
         */
        public static void getPropertyInfo() {
            getInfoDatabase.getProperty();
        }
        /**
         * get landlord info database calling method
         */
        public static void getLandlordInfo() {
            getInfoDatabase.getLandlord();
        }
        /**
         * get renter info database calling method
         */
        public static void getRenterInfo() {
            getInfoDatabase.getRenter();
        }
    }

    /**
     * getInfoDatabase class. Retrieves desired information stored in database
     */
    static class getInfoDatabase{
        static boolean dbConnected = false;
        static Connection dbConnect;

        /**
         * Method connects to database and retrieves all stored information on all listed properties using resultset
         */
        public static void getProperty(){
           
            try{
                dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
                dbConnected = true;
                System.out.println("Connected to database successfully!");

                Statement mySt = dbConnect.createStatement();
                ResultSet rs = mySt.executeQuery("select * from properties");

                ResultSetMetaData rsmeta = rs.getMetaData();
int column = rsmeta.getColumnCount();
String deliver = "Type,Bedrooms,Bathrooms,Furnished,Quadrant,Landlord,postStatus,State,postalCode\n";
deliver += "________________________________________________________________\n";
while (rs.next()) {
    for(int i = 1; i <= column; i++){
        deliver +=rs.getString(i) ;
        deliver += " ";
    }
    deliver +="\n";
     JOptionPane.showMessageDialog(pl.getFrame(), deliver);
        //System.out.print(rs.getString(i) + " ");
    //System.out.println();
}
System.out.println(deliver);

mySt.close();
rs.close();
dbConnect.close();
            } catch(SQLException e) {
                dbConnected = false;
               // e.printStackTrace();
                System.out.println("Unable to connect to database. Reverting to non-database version...");
            }

            
                
            
            
        }

        /**
         * Method that retrieves all database stored landlord info by connecting to the database and retrieving a resultset from the applicable columns. 
         * Extracts the info from the resultset into a usable string
         */
        public static void getLandlord(){
           
            try{
                dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
                dbConnected = true;
                System.out.println("Connected to database successfully!");

                Statement mySt = dbConnect.createStatement();
                ResultSet rs = mySt.executeQuery("select * from Landlord");

                ResultSetMetaData rsmeta = rs.getMetaData();
int column = rsmeta.getColumnCount();
String deliver = "Name, Email \n";
deliver += "________________________________________________________________\n";
while (rs.next()) {
    for(int i = 1; i <= column; i++){
        deliver +=rs.getString(i) ;
        deliver += " ";
    }
    deliver +="\n";
     JOptionPane.showMessageDialog(pl.getFrame(), deliver);
        //System.out.print(rs.getString(i) + " ");
    //System.out.println();
}
System.out.println(deliver);

mySt.close();
rs.close();
dbConnect.close();
            } catch(SQLException e) {
                dbConnected = false;
               // e.printStackTrace();
                System.out.println("Unable to connect to database. Reverting to non-database version...");
            }

            
                
            
            
        }

/**
 * Get renter info from database method. Converts resultset of renter info into string
 */
        public static void getRenter(){
           
            try{
                dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
                dbConnected = true;
                System.out.println("Connected to database successfully!");

                Statement mySt = dbConnect.createStatement();
                ResultSet rs = mySt.executeQuery("select * from Renter");

                ResultSetMetaData rsmeta = rs.getMetaData();
int column = rsmeta.getColumnCount();
String deliver = "Name, Email, Password, Bedrooms, Bathrooms,Status, Quadrant \n";
deliver += "________________________________________________________________\n";
while (rs.next()) {
    for(int i = 1; i <= column; i++){
        deliver +=rs.getString(i) ;
        deliver += " ";
    }
    deliver +="\n";
     JOptionPane.showMessageDialog(pl.getFrame(), deliver);
        //System.out.print(rs.getString(i) + " ");
    //System.out.println();
}
System.out.println(deliver);

mySt.close();
rs.close();
dbConnect.close();
            } catch(SQLException e) {
                dbConnected = false;
               // e.printStackTrace();
                System.out.println("Unable to connect to database. Reverting to non-database version...");
            }

            
                
            
            
        }


    }
}



    /**
     * MyPanel class for manager dashboard
     */
    class MyPanel extends JFrame{
        private JFrame manage;
        private JPanel panel;
        public JButton setFeeAmountButton;
        public JButton setFeePeriodButton;
        public JButton changeListingButton;
        public JButton getPeriodicalButton;
        public JButton getRenterButton;
        public JButton getPropertyButton;
        public JButton getLandlordButton;
        public JButton changeStateButton;
        myManagerListener listener;
        Manager maangerref;

        /**
         * Get reference to this frame
         * @return this JFrame
         */
        public JFrame getFrame() {
            return this.manage;
        }

        /**
         * Flowlayout for manager with buttons set and with added actionlisteners
         * Sets manager dashboard visible
         * @param ma manager reference
         */
        public MyPanel(Manager ma) {
            maangerref = ma;
            FlowLayout fl = new FlowLayout();
            manage = new JFrame("Welcome manager");
            panel = new JPanel();
           // setFeeButton = new JButton ("Set fee");
            setFeeAmountButton = new JButton("Set fee amount");
            setFeePeriodButton = new JButton("Set fee period");
            changeListingButton = new JButton ("Change a listing");
            getPeriodicalButton = new JButton("Get periodical summary");
            getRenterButton = new JButton("Get renter information");
            getPropertyButton = new JButton("Get property information");
            getLandlordButton = new JButton ("Get landlord information");
            changeStateButton = new JButton("Change ListingState");
            listener = new myManagerListener(this);
            changeStateButton.addActionListener(listener);
            setFeeAmountButton.addActionListener(listener);
            setFeePeriodButton.addActionListener(listener);
            changeListingButton.addActionListener(listener);
            getPeriodicalButton.addActionListener(listener);
            getRenterButton.addActionListener(listener);
            getPropertyButton.addActionListener(listener);
            getLandlordButton.addActionListener(listener);

            manage.setSize(500, 500);
            manage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            panel.setLayout(fl);
            panel.add(setFeeAmountButton);
            panel.add(setFeePeriodButton);
            panel.add(changeListingButton);
            panel.add(getPeriodicalButton);
            panel.add(getRenterButton);
            panel.add(getPropertyButton);
            panel.add(getLandlordButton);
            panel.add(changeStateButton);
            manage.setContentPane(panel);
            manage.setVisible(true);
        }
    }

    /**
     * Actionlistener class for manager dashbaord
     */
    class myManagerListener implements ActionListener{
        private MyPanel frame;

        /**
         * set frame to a mypanel
         * @param j mypanel to reference
         */
        public myManagerListener(MyPanel j) {
            frame = j;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==frame.setFeeAmountButton){
                frame.maangerref.setFeeAmount();
            }
            if(e.getSource()==frame.setFeePeriodButton) {
                frame.maangerref.setFeePeriod();
            }
            if(e.getSource()==frame.getPropertyButton) {
                frame.maangerref.getPropertyInfo();
            }
            if(e.getSource()==frame.getLandlordButton){
                frame.maangerref.getLandlordInfo();
            }
            if(e.getSource()==frame.getRenterButton){
                frame.maangerref.getRenterInfo();
            }
            if(e.getSource()==frame.changeListingButton){
                String postalCancel = (String)(JOptionPane.showInputDialog("Enter postal code of Listing to cancel"));
            frame.maangerref.removeListing(postalCancel);
            }
            if(e.getSource()==frame.changeStateButton){
                String postalCancel = (String)(JOptionPane.showInputDialog("Enter postal code of Listing to change"));
            frame.maangerref.changeState(postalCancel);
            }
            if(e.getSource()==frame.getPeriodicalButton) {
                frame.maangerref.getPeriodical();
            }
        }
    }
    


