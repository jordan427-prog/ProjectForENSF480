package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import GUI.SubscriptionGUI.MyButtonListener;
import GUI.SubscriptionGUI.MyListListener;
import control.EmailController;
import entities.Property;

public class BrowseGUI  extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// attributes
	private volatile boolean searchcriteria_flag = false;
	private Property selectedProperty = null;

	private Property searchProperty = new Property();
	
	
	// for Jlist
	private ArrayList<String> mylist;
	private ArrayList<Property> propertyList;
	private JList<String> list;
	
	// frame that displays property information
	private JFrame propertyDisp;
	private JButton closeWindow;
	private JButton contactLandlord;
	
	// for search frame
	private JFrame search = new JFrame();

	private JButton submitCriteria = new JButton("search criteria");
	private MyListener listener;
	private MyListListener listlistener;
	Property p;
	JTextField tf1;
	JTextField tf2;
	JTextField tf3;
	JTextField tf4;
	JTextField tf5;
	
	public BrowseGUI () {
	}
	/**
	 * filters on user specified criteria (user must fill out all text fields)
	 */
	public void searchCriteria () {
		p = new Property();
		listener = new MyListener();
		search.setSize(500, 300);
		JLabel l = new JLabel ("Specify criteria below (please fill out all textfields)");
		
		JPanel rp1 = new JPanel (); // default flow layout
		JLabel l1 = new JLabel ("Type (ex. Apartment): ");
		tf1 = new JTextField(25);
		rp1.add(l1);
		rp1.add(tf1);
		
		JPanel rp2 = new JPanel ();
		JLabel l2 = new JLabel ("Bedroom(s): ");
		tf2 = new JTextField(25);
		rp2.add(l2);
		rp2.add(tf2);
		
		JPanel rp3 = new JPanel();
		JLabel l3 = new JLabel ("Bathroom(s): ");
		tf3 = new JTextField(25);
		rp3.add(l3);
		rp3.add(tf3);
		
		JPanel rp4 = new JPanel();
		JLabel l4 = new JLabel ("Furnished (Y/N): ");
		tf4 = new JTextField(25);
		rp4.add(l4);
		rp4.add(tf4);
		
		JPanel rp5 = new JPanel();
		JLabel l5 = new JLabel ("Quadrant (ex. NW): ");
		tf5 = new JTextField(25);
		rp5.add(l5);
		rp5.add(tf5);
		
		search.setLayout( new GridLayout (8,2));
		search.add(l, BorderLayout.NORTH);
		search.add(rp1, BorderLayout.CENTER);
		search.add(rp2, BorderLayout.CENTER);
		search.add(rp3, BorderLayout.CENTER);
		search.add(rp4, BorderLayout.CENTER);
		search.add(rp5, BorderLayout.CENTER);
		search.add( new JLabel ());
		search.add(submitCriteria, BorderLayout.SOUTH);
		
		
		search.setVisible(true);
		submitCriteria.addActionListener(listener);
		
	}
	
	// show available properties
	public void displayAvailableProperties (ArrayList<String> postal, ArrayList<Property> p) {
		
		mylist = postal;
		propertyList = p;

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
		listener = new MyListener();
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
        closeWindow.addActionListener(listener);
        contactLandlord.addActionListener(listener);
		
		propertyDisp.setVisible(true);
		
	}
	public Property getSearchCriteria () {
		return searchProperty;
	}
	
	public boolean getSearchCriteriaFlag () {
		return searchcriteria_flag;
	}
	private String isFurnishedYN (boolean f) {
		if (f == true)
			return "Yes";
		else // f == false
			return "No";
	}
	
	private boolean isFurnishedBool (String s) {
		if (s.equals("Y"))
			return true;
		else // "N"
			return false;
	}
	
	public static void noMatchesError () {
		JOptionPane.showMessageDialog (null, "No matches found", "Error", JOptionPane.ERROR_MESSAGE);
	}
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// if submit criteria button is pushed
			if (e.getSource() == submitCriteria) {
				System.out.println("search criteria pressed");
				
				searchProperty.setType(tf1.getText()); 
				searchProperty.setBeds(Integer.valueOf(tf2.getText()));
				searchProperty.setBaths(Double.valueOf(tf3.getText()));
				searchProperty.setFurnished(isFurnishedBool(tf4.getText()));
				searchProperty.setQuadrant(tf5.getText());
				searchcriteria_flag = true;
				search.dispose(); // close criteria window
			}
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
			
		}
		
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
}

