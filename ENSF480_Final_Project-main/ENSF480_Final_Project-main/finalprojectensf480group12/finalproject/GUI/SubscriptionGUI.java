package GUI;

import entities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import GUI.EmailGUI.MyListener;
import control.EmailController;
import control.SubscriptionController;

public class SubscriptionGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Property selectedProperty = null;
	private String email = null;

	// listeners
	private MyListListener listlistener;
	private MyButtonListener buttonlistener;
	
	// for Jlist
	private ArrayList<String> mylist;
	private ArrayList<Property> propertyList;
	private JList<String> list;
	
	// frame that displays property information
	private JFrame propertyDisp;
	private JButton closeWindow;
	private JButton contactLandlord;
	
	// buttons to sub / unsub
	private JButton subButton;
	private JButton unsubButton;
	
	public SubscriptionGUI(ArrayList<String> postal, ArrayList<Property> p, String email) {
		super();
		mylist = postal;
		propertyList = p;
		this.email = email;
		
		// setup for main jframe
		listlistener = new MyListListener();
		setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		setTitle ("Notifications");
		setSize (500, 300);
		setLayout ( new BorderLayout () );
		
		// label
		JLabel label = new JLabel ("New Listings that match your criteria: ");
		add (label, BorderLayout.NORTH);
		
		// panel with list of properties
		JPanel panel = new JPanel (new BorderLayout());
		list = new JList<String>(mylist.toArray(new String[mylist.size()]));
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(list);
		
		list.setLayoutOrientation(JList.VERTICAL);
		list.addListSelectionListener(listlistener); // listener for list
		panel.add(scroll);
		add (panel);
		
		// sub/unsub buttons at the bottom panel
		subButton = new JButton ("subscribe");
		unsubButton = new JButton ("unsubscribe");
		buttonlistener = new MyButtonListener();
		JPanel panel2 = new JPanel (new FlowLayout(FlowLayout.CENTER));
		
		subButton.addActionListener(buttonlistener);
		unsubButton.addActionListener(buttonlistener);
		panel2.add(subButton);
		panel2.add(unsubButton);
		add (panel2, BorderLayout.SOUTH);
		
		
		setVisible(true);
	}
	

	
	/**
	 * Displays property info in a new window box
	 * @param property
	 */
	private void displayProperty (Property p) {
		selectedProperty = p;
		
		// JFrame setup
		propertyDisp = new JFrame ("property information");
		closeWindow = new JButton ("close");
		contactLandlord = new JButton ("contact landlord");
		buttonlistener = new MyButtonListener();
		propertyDisp.setSize(350, 300);
		propertyDisp.setLayout (new BorderLayout() );
		
		
		// button setup
		JPanel buttonCenter = new JPanel( new FlowLayout(FlowLayout.CENTER) );
		buttonCenter.add(closeWindow);
		buttonCenter.add(contactLandlord);
        propertyDisp.add(buttonCenter, BorderLayout.SOUTH);
        
        // text 
        JLabel text = new JLabel();
        String html = "<html><body>"
        		+ "<h1>Property Information</h1>"
        		+ "<p>postal code: " + p.getPostalCode()
        		+ "<br>type: " + p.getType()
        		+ "<br>bedroom(s): " + p.getBed() 
        		+ "<br>bathroom(s): " + p.getBath()
        		+ "<br>furnished: " + isFurnishedYN (p.isFurnished())
        		+ "<br>quadrant: " + p.getQuadrant()
        		+ "</p></body></html>";
        text.setText(html);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        
        propertyDisp.add(text);
        closeWindow.addActionListener(buttonlistener);
        contactLandlord.addActionListener(buttonlistener);
		
		propertyDisp.setVisible(true);
		
	}
	private String isFurnishedYN (boolean f) {
		if (f == true)
			return "Yes";
		else // f == false
			return "No";
	}
	
	
	/**
	 * Information pane that show "no notifications"
	 */
	public void noNotifications () {
		JOptionPane.showMessageDialog(null, "No notifications!", "notifications", JOptionPane.INFORMATION_MESSAGE);
	}

	
	class MyListListener implements ListSelectionListener {

		/**
		 *  listener for selecting item from Jlist
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				//System.out.println(list.getSelectedValue());
				displayProperty (propertyList.get(list.getSelectedIndex()));
			}	
		}		
	}
	class MyButtonListener implements ActionListener {

		/**
		 * listener for buttons in property info window
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// if user wants to close window
			if (e.getSource() == closeWindow) {
				propertyDisp.dispose();
			}
			
			// if user wants to contact landlord
			if (e.getSource() == contactLandlord) {
				// start on a new thread so it is running on a different thread the the subscription UI
				Thread t = new Thread (new Runnable() {
					@Override
					public void run () {
						EmailController hello = new EmailController ();
						hello.createEmail(selectedProperty.getLandlordName());
					}
				});
				t.start();
			}
			
			// if user wants to subscribe
			if (e.getSource() == subButton) {
				System.out.println("sub pressed");
				SubscriptionController s = new SubscriptionController();
				s.subscribe(email);
				JOptionPane.showMessageDialog(null, "You have subscribed and will get notified of new listings.", "Subscription service", JOptionPane.PLAIN_MESSAGE);
			}
			
			// if user wants to unsubscribe
			if (e.getSource() == unsubButton) {
				System.out.println("unsub pressed");
				SubscriptionController s = new SubscriptionController();
				s.unsubscribe(email);
				JOptionPane.showMessageDialog(null, "You have unsubscribed and no longer will get notifications.", "Subscription service", JOptionPane.PLAIN_MESSAGE);
			}
			
		}		
	}	
}
