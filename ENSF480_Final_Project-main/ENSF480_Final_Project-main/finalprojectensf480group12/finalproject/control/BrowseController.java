package control;

import java.util.ArrayList;

import GUI.BrowseGUI;
import GUI.SubscriptionGUI;
import database_management.DatabaseClass;
import entities.Property;

public class BrowseController {
	
	public void openBrowsing () {
		BrowseGUI test = new BrowseGUI();
		test.searchCriteria();
		// wait until there is a message available
        while (!test.getSearchCriteriaFlag()) {
        	
        }
        System.out.println("hello I am here");
        DatabaseClass myDatabaseConn = new DatabaseClass("jdbc:mysql://localhost:3306/rentalsystem", "root", "password");
        myDatabaseConn.initializeConnection();
        
        // look in data base for properties that match search criteria
        // and save into ArrayList
        ArrayList <String> availableProperties = myDatabaseConn.getAvailableProperties();
        ArrayList <String> matchesCriteria = new ArrayList <String> ();
        
        int i = 0;
        System.out.println(availableProperties.size());
        while (i < availableProperties.size()) {
        	
        	// if property matches criteria 
        	//System.out.println(availableProperties.get(i));
        	if (BrowseController.doesMatch (test.getSearchCriteria(), myDatabaseConn.getPropertyInfo(availableProperties.get(i)))) {
        		System.out.println("add to matched results: " + availableProperties.get(i));
        		matchesCriteria.add(availableProperties.get(i));
        	}
        	i++;
        }
        
        
        // view these properties
        ArrayList <Property> p = new ArrayList<Property> ();
        
        if (matchesCriteria.isEmpty()) {
        	System.out.println("NO MATCHES");
        	// have no notifications pop-up
        	BrowseGUI.noMatchesError();
        	
        }
        else {
        	i = 0;
	        while (i < matchesCriteria.size()) {
	        	p.add(myDatabaseConn.getPropertyInfo(matchesCriteria.get(i)));
	        	i++;
			}
	        
	        BrowseGUI window = new BrowseGUI ();
	        window.displayAvailableProperties(matchesCriteria, p);
	       
	        
	        // check if user was interested in a property
	        
        }
        myDatabaseConn.close();
	}
	/**
	 * Returns true if criteria and property have the same criteria
	 * false otherwise (note: ALL criteria must match to be true)
	 * @param criteria
	 * @param property
	 * @return
	 */
	private static boolean doesMatch (Property criteria, Property property) {
		//System.out.println(property.getPostalCode());
		// if no criteria everything matches
		if (criteria == null) 
			return true;
		boolean match = true;
		// if a critieria not match
		if (!criteria.getType().equals(property.getType())) {
			//System.out.println(criteria.getType() + "\t" + property.getType());
			match = false;
		}
		else if (criteria.getBedrooms() != property.getBedrooms()) {
			//System.out.println(criteria.getBedrooms() + "\t" + property.getBedrooms());
			match = false;
		}
		else if (criteria.getBathrooms() != property.getBathrooms()) {
			//System.out.println(criteria.getBathrooms() + "\t" + property.getBathrooms());
			match = false;
		}
		else if (criteria.isFurnished() != property.isFurnished()) {
			//System.out.println(criteria.isFurnished() + "\t" + property.isFurnished());
			match = false;
		}
		else if (!criteria.getQuadrant().equals(property.getQuadrant())) {
			//System.out.println(criteria.getQuadrant() + "\t" + property.getQuadrant());
			match = false;
		}
		//System.out.println(match);
		return match;
	}
}

