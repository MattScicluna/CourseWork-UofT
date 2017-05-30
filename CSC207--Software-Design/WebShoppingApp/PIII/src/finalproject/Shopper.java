package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shopper extends RegisteredUser implements Serializable{
	/**
	 * The shopper is the entity that takes care of authentication.
	 * Customer may exist in a List in Shopper, although
	 * in the current implementation there is a one to one correspondence between shoppers and customers.
	 * Currently shopper only has the UserID, password and a boolean representing if it has Admin privileges 
	 */
	private static final long serialVersionUID = -1071847871851422L;
	protected List<Customer> customerList;
	final int sessionCode = 2;
	
	public Shopper(String userID, String password, boolean privileges) {
		super(userID, password, privileges);
		customerList = new ArrayList<Customer>();
	}
	
	@Override
	protected int setSessionID() {
		int sessionNum = RegisteredUser.incrementSessionID();
		String sessionString = Integer.toString(this.getSessionCode()) + Integer.toString(sessionNum);
		this.sessionID = Integer.valueOf(sessionString);
		return this.sessionID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
		
	@Override
	public String toString() {
		return "Shopper [userID=" + userID + ", privileges=" + this.getPrivileges() + ", password=" + password + "]";
	}
	public int getSessionCode() {
		return sessionCode;
	}
	/**
	 * adds a customer to the shoppers' list of associated customers.
	 * @param custName -> the name of the customer.
	 * @param city -> the city the customer lives in.
	 * @param street -> the street the customer lives on.
	 * @return -> customer ID if customer added successfully.
	 * */
	public int addCustomer(String custName, String city, String street) {
		Customer customer = new Customer(custName, city, street);
		this.customerList.add(customer);
		return customer.getCustomerID();
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}
	
}
