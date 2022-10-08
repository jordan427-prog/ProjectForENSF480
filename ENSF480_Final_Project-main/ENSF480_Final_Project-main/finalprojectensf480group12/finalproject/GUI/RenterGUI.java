
package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.EmailGUI.MyListener;
import control.BrowseController;
import control.EmailController;
import control.SubscriptionController;

public class RenterGUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String emailAddress;
	
	private JButton browse;
	private JButton notifications;
	
	private MyListener listener;
	
	public RenterGUI(String email) {
		super();
		emailAddress = email;
		listener = new MyListener();
		
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		setTitle ("Registered Renter");
		setSize (600, 300);
		setLayout ( new BorderLayout () );
		
		// label
		JLabel label = new JLabel ("Welcome Renter! ( " + email + " ) ");
		add (label, BorderLayout.NORTH);
		JPanel p = new JPanel();
		browse = new JButton ("browse properties");
		notifications = new JButton ("check notifications");
		 
		p.add(browse);
		p.add(notifications);
        browse.addActionListener(listener);
        notifications.addActionListener(listener);
		
        add(p, BorderLayout.CENTER);
		setVisible(true);
	}
	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == notifications) {
				System.out.println("user checks notifications");
				SubscriptionController sc = new SubscriptionController();
				sc.checkNotifications(emailAddress);
			}
			if (e.getSource() == browse) {
				System.out.println("user wants to browse");
				// start on a new thread so it is running on a different thread the the search criteria UI
				Thread t = new Thread (new Runnable() {
					@Override
					public void run () {
						BrowseController b = new BrowseController();
						b.openBrowsing();
					}
				});
				t.start();
				
				
			}
			
		}
		
	}
}
