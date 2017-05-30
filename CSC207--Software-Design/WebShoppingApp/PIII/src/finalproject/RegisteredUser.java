package finalproject;

import java.io.Serializable;

public abstract class RegisteredUser implements Serializable{
	/**
	 * Registered Users may be either admins or shoppers (customers)
	 * Admins are responsible for adding and updating products, categories, distribution centers and cities.
	 * Customers make purchases by managing a shopping cart.
	 * 
	 * @author Conrad
	 * @version 1.0
	 */
	private static final long serialVersionUID = 7203185497542551022L;

	protected boolean privileges;
	
	static int globalID = 0; /* incremented by Subclass when they login to generate sessionID */
	int sessionID;
	boolean activeSession = false;
	String userID;
	String password;
	int globalIDin = 0;
	
	public RegisteredUser(String userID, String password, boolean privileges) {
		this.userID = userID;
		this.password = password;
		this.privileges = privileges;

	}
	
	/**
	 * Called by Customer and Admin to generate their own sessionIDs.
	 * If registeredUser is a Customer puts a 2 in front of their sessionID
	 * If registeredUser is a Admin puts a 1 in front of their sessionID
	 */
	public static int incrementSessionID() {
		
		RegisteredUser.globalID += 1;
		return globalID;
	}
	
	/**
	 * subclasses must set their own sessionId using incrementSessionID()
	 */
	protected abstract int setSessionID();
	
	public boolean getPrivileges() {
		return privileges;
	}
	
	public void setPrivileges(boolean privileges) {
		this.privileges = privileges;
	}
	
	public boolean isActiveSession() {
		return activeSession;
	}
	
	public void setActiveSession(boolean activeSession) {
		this.activeSession = activeSession;
	}


	public String getUserID() {
		return this.userID;
	}

	
	
}