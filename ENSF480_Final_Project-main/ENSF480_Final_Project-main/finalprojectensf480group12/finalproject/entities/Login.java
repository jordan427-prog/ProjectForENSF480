/**
 * Login class for users to select their role and for registered renters and managers to login with name and password
 */

package entities;
import control.SubscriptionController;

import java.awt.Window;

import javax.swing.*;
import java.sql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login class
 */
public class Login {
    private String userType;
    private String email;
    Window[] winArr;
   public LoginFrame frame;
   /**
    * Login constructor. Gives drop down menu for user to input their user type. If null, exit program
    */
    public Login() {
      // winArr =  Window.getWindows();
      //  if(winArr.length>0) {winArr[0].dispose();}
    String[] options = {"Manager", "Landlord", "Registered Renter", "Regular Renter"};
    userType = (String) JOptionPane.showInputDialog(null, "Choose User Type: ", "Choose User", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    if(userType==null) {System.out.println("Now exiting program!");System.exit(0);}
    else{
       // winArr = Window.getWindows();
       // winArr[1].dispose();
    }
    }

    /**
     * Getter for user type (eg. manager)
     * @return user type
     */
    public String getUser() {
        return this.userType;
    }
/**
 * Returns how many windows are open
 * @return windows open
 */
    public int getWindowSize() {
        return winArr.length;
    }

    /**
     * Password check login GUI. Sets up login frame with appropriate boundaries
     * @param user user definition. Eg. manager or landlord
     */
    public void passwordCheck(String user) {
         frame = new LoginFrame(user, this);
        frame.setTitle("Login");
        frame.setVisible(true);
        frame.toFront();
        frame.setBounds(10, 10, 370, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        
    }

    /**
     * Email getter method
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Email setter
     * @param toSet email to set
     */
    public void setemail(String toSet) {
        this.email = toSet;
    }

    

}

/**
 * LoginFrame class. INcludes container, labels and text fields as well as the actionlistener
 */
class LoginFrame extends JFrame implements ActionListener {
 
    Container container = getContentPane();
    JLabel userLabel = new JLabel("NAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    String user;
    Login logRef;
 
    
 
 /**
  * Constructor. Sets size+location, adds labels to container and adds actionlisteners to buttons
  * @param user user type
  */
    LoginFrame(String user, Login logged) {
        this.user = user;
        this.logRef = logged;
        setLocation();
        add();
        addAction();
 
    }
 
    
 /**
  * Sets boundaries and locations for text fields
  */
    public void setLocation() {
        container.setLayout(null);
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        
        loginButton.setBounds(50, 300, 100, 30);
        
 
 
    }
 
    /**
     * adds all labels and fields to the container
     */
    public void add() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        
        container.add(loginButton);
        
    }
 
    /**
     * Adds actionlistener to login button
     */
    public void addAction() {
        loginButton.addActionListener(this);
        
        
    }

    
 
 
    @Override
    public void actionPerformed(ActionEvent e) {
       //if login button pressed, now we must go into dB to see if password matches name
        if (e.getSource() == loginButton) {
            String userText;
            char[] pwdText;
            
            userText = userTextField.getText();
            pwdText = passwordField.getPassword();
            String pass = String.valueOf(pwdText);
String correctPassword = "";
//password search for a manager -> Find password stored under entered manager name (if exists), then match passwords
if(user.equals("Manager")){
            try{
                Connection dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
               // dbConnected = true;
                System.out.println("Connected to database successfully!");
                System.out.println(userText);

                Statement mySt = dbConnect.createStatement();
                ResultSet rs = mySt.executeQuery("select * from Manager where name = '"+ userText+"';");

                
if(rs.next()){
String correct = rs.getString(3);
//correctPassword = rs.getString(3);

correctPassword = correct;
}
   



mySt.close();
rs.close();
dbConnect.close();
            } catch(SQLException e2) {
               // dbConnected = false;
                e2.printStackTrace();
                System.out.println("Unable to connect to database. Reverting to non-database version...");
            }

        }

        else if (user.equals("Registered Renter")){
//password match for a registered renter -> searches for entered name and matches stored password with entered
            try{
                Connection dbConnect = DriverManager.getConnection("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
               // dbConnected = true;
                System.out.println("Connected to database successfully!");
                System.out.println(userText);

                Statement mySt = dbConnect.createStatement();
                ResultSet rs = mySt.executeQuery("select * from Renter where name = '"+ userText+"';");

           
if(rs.next()){
String correct = rs.getString(3);
logRef.setemail(rs.getString(1));
//correctPassword = rs.getString(3);

correctPassword = correct;
}
   



mySt.close();
rs.close();
dbConnect.close();
            } catch(SQLException e2) {
               // dbConnected = false;
                e2.printStackTrace();
                System.out.println("Unable to connect to database. Reverting to non-database version...");
            }




        }
                
            

        //checks if entered password matches database password. If yes, dispose login screen and continue. If not, persist login request
            if (pass.equals(correctPassword)) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password. Try Again!");
            }
 
        }
       
     
    }
 
}
 
