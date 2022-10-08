package entities;

import java.util.ArrayList;

import control.BrowseController;

public class RegularRenter {
	
	protected String name;
	protected ArrayList <Property> renting;
	protected BrowseController browseWin;
	
	public RegularRenter () {
		browseWin = new BrowseController();
		
	}
	/**
	 * allows renter to search for data with specific criteria 
	 */
	public void searchData () {
		browseWin.openBrowsing();
	}
	
	

}

