package GUI;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;

/**
 * EmailGUI is used to create the windows the user will interact with
 * to send an email to landlord
 * @author thess
 *
 */
public class EmailGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private String message;
	private final int max_message_size = 1250;
	private volatile boolean message_available = false;
	
	// window
	private JButton sendButton = new JButton ( "send email" );
	JPanel buttonCenter;
	private JEditorPane text = new JEditorPane();
	
	// listener
	private MyListener listener;
	
	/**
	 * Constructor creates the main text editor window where email message will be written
	 */
	public EmailGUI () {
		super();
		listener = new MyListener();
		
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setTitle ("Send Email");
		setSize (600, 300);
		setLayout ( new BorderLayout () );
		
		// label
		JLabel label = new JLabel ("Type your message below: ");
		add (label, BorderLayout.NORTH);
		
		// text box
		text.setContentType("text/plain");
		add (text, BorderLayout.CENTER);
		
		
		// send button
		buttonCenter = new JPanel( new FlowLayout(FlowLayout.CENTER) );
		buttonCenter.add(sendButton);
        add(buttonCenter, BorderLayout.SOUTH);
        
        sendButton.addActionListener(listener);
		
		setVisible(true);
	}
	
	/**
	 * Returns email message written by renter
	 * @return String
	 */
	public String getMessage () {
		return message;
		
	}
	
	/**
	 * Returns true if there is a new message written by renter
	 * false if there is no message or no valid message
	 * @return boolean
	 */
	public boolean getMessageAvailable () {
		return message_available;
	}
	
	/**
	 * Creates a window to let user know that email was sent
	 */
	public void messageSentFrame () {
		JOptionPane.showMessageDialog(null, "Message successfully sent", "Email", JOptionPane.PLAIN_MESSAGE);
	}
	
	
	class MyListener implements ActionListener {
		
		/**
		 * Waits for user to interact with GUI, if they press the 'send email' button and
		 * message is valid it will continue, if not it will let user know that message is invalid
		 * and let them try again.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// send email button
			if (e.getSource()== sendButton) {
				//System.out.println(text.getText());
				message = text.getText();
				//System.out.println(message);
				if (message == null || message.length() < 1) {
					JOptionPane.showMessageDialog (null, "Message is empty. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (message.length() > max_message_size) {
					JOptionPane.showMessageDialog (null, "Message is too long, max 1250 characters. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {// valid message 
					message_available = true;
					
				}
					
			}

		} // end of actionPreformed
		
	} // end of MyListener class
	
} // end of EmailGUI class

