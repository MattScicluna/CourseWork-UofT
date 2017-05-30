package finalproject;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is intended to be your API
 * @author Ilir
 *
 */
public class Project {
	/**
	 * This method must add a new shopper user or administrator user.
	 * @param userID
	 * @param password
	 * @param admin -> if true, add an administrator user, otherwise add a shopper user
	 * @return -> true if operation successful, false otherwise
	 */
	public boolean addUser(String userID, String password, boolean admin) {
		boolean addedUser = true;
		try {
			String path = "UserDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			if (userDataSave.mapUser.containsKey(userID)) {
				addedUser = false;
			}
			else if (admin) {
				userDataSave.add(new Admin(userID, password , admin)); // Admin
			}
			else if (!admin){
				userDataSave.add(new Shopper(userID, password, admin)); // Shopper (only for authentication purposes)
			}
			userDataSave.saveToFile(path);
		}
		catch (Exception e) {
			addedUser = false;
		}
		return addedUser;
	}

	/**
	 * Authenticates a user an creates an active work session
	 * @param userID 
	 * @param password
	 * @return -> SessionID if authentication successful, -1 otherwise.
	 */
	public int login(String userID, String password) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		int sessionId = -1;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			RegisteredUser user = userDataSave.mapUser.get(userID); // if user not in shop user list, throws exception
			if (password.equals(user.password)) {
				if(user.getPrivileges() == false && !((Shopper)user).getCustomerList().isEmpty()) {
					((Shopper)user).getCustomerList().get(0).loginReloadCart(shopDataSave.shop);
					if (!((Shopper)user).getCustomerList().get(0).loginOutofStockProducts.isEmpty()) {
						((Shopper)user).getCustomerList().get(0).changes = true;
						((Shopper)user).getCustomerList().get(0).loginOutofStockProductsChanges = ((Shopper)user).getCustomerList().get(0).loginOutofStockProducts;
						((Shopper)user).getCustomerList().get(0).loginOutofStockProducts.clear();
					}
//					((Shopper)user).getCustomerList().get(0).loginReloadCart(shopDataSave.shop); //saves cart. unlock availQnty
					// GUI shoudld check List<Product> Customer.loginOutofStockProducts every time user logs in
					// and notifies the user of the contents of the List if the List is not Empty.
				}
				user.setActiveSession(true); // sets session to active

				if (user.sessionID == 0) {
					sessionId = user.setSessionID(); // generates new sessionIDs if first time logging in
				}
				else {
					sessionId = user.sessionID; // uses old sessionID if not first time logging in
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception thrown by Login");
			return -1;
		}
		return sessionId;
	}
	/**
	 * Makes sessionID unavailable for connection
	 * @param sessionID
	 */
	public void logout(int sessionID) {
		// your code goes here
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID) { // check for matching sessionID
					if(user.getPrivileges() == false && !((Shopper)user).getCustomerList().isEmpty()) {
						if (!((Shopper)user).getCustomerList().get(0).getCart().getShopI().isEmpty()) { //if cart not empty, update avilQnty
							((Shopper)user).getCustomerList().get(0).logoutSetQuantities(shopDataSave.shop); //saves cart. unlock availQnty
							user.setActiveSession(false);
							System.out.println("()()())()()()()()()()");
							break;
						}
					}
					user.setActiveSession(false); // sets session to inactive
					break;
				}
			} 
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during logout"); // could be exception in reading bin
		}
	}

	/**
	 * This method must add a new category in your application.
	 * @param catName -> The name of the category to be added.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> The ID of the category you created if successful, -1 if not successful.
	 */
	public int addCategory(String catName, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		int categoryID = -1;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave2 = new ShopManager2(pathShop);
			shopDataSave2.add(shopDataSave2.shop); // MAY NEED TO UNCOMMENT NOT SURE
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges()) { // check for matching sessionID & if admin
					categoryID = ((Admin)user).addCategory(catName, shopDataSave2.shop); // adds the category, returns catID
					break;
				}
			} 
			userDataSave.saveToFile(path);
			shopDataSave2.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addCategory"); // could be exception in reading bin
		}
		return categoryID;
	}

	/**
	 * Adds a distribution center to your application.
	 * If the given distribution center exists, or sesionID invalid, do nothing.
	 * @param city -> The city where distribution center must be based.
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 */
	public void addDistributionCenter(String city, int sessionID) {
		//		 Your code goes here
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges() == true) { // check for matching sessionID & if admin
					((Admin)user).addDistributionCenter(city, shopDataSave.shop);// adds the DistributionCentre
					break;
				}
			} 
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addDistributionCenter"); // could be exception in reading bin
		}
	}

	/**
	 * Adds a new Customer to your application; the customer record that belongs 
	 * to a newly added shopper user that has no customer record on the system.
	 * @param custName -> The name of the customer
	 * @param city -> The city of the customer address
	 * @param street -> The street address of the customer
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The added customer ID
	 */
	public int addCustomer(String custName, String city, String street, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		int customerID = -1;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.sessionID == sessionID && user.getPrivileges() == false && user.activeSession) { // check for matching sessionID & if shopper. only shopper can add customer
					customerID = ((Shopper)user).addCustomer(custName, city, street);// adds the Customer into Shopper's customer list
					shopDataSave.shop.cityGraph.addVertex(((Shopper)user).getCustomerList().get(0).getCity());
					if(!shopDataSave.shop.cityGraph.addVertex(((Shopper)user).getCustomerList().get(0).getCity())){
						Customer cust = ((Shopper)user).getCustomerList().get(0);
						ArrayList<City> graph = shopDataSave.shop.cityGraph.getVertices();
						for(int i=0; i < graph.size(); i++){
							if(graph.get(i).getCityName().equals(cust.getCity().getCityName())){
								cust.setCity(graph.get(i));
								break;
							}
						}
					}
				}
			} 
			// this does not take into account multiple identical customers added into the same shopper
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addCustomer"); // could be exception in reading bin
		}
		return customerID;
	}

	/**
	 * Adds a new Product to your application
	 * @param prodName -> The product name
	 * @param category -> The product category.
	 * @param price -> The product sales price
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * @return -> Product ID if successful, -1 otherwise.
	 */

	public int addProduct(String prodName, int category, double price, int sessionID, String pic) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		int productID = -1;

		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges()) { // check for matching sessionID & if admin. only admin can add product
					productID = ((Admin)user).addProduct(prodName, price, category, shopDataSave.shop, pic);
					break;
				}
			} 

			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addProduct"); // could be exception in reading bin
		}
		return productID;

	}

	/**
	 * Computes the available quantity of prodID in a specific distribution center.
	 * @param prodID
	 * @param center
	 * @return -> Available quantity or -1 if prodID or center does not exist in the database
	 */
	public int prodInquiry(int prodID, String center) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		int availableQuantity = -1;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			List<DistributionCenter> distCenter = shopDataSave.shop.distributionCenters;
			for (int i=0; i< distCenter.size() ; i++) {
				if (distCenter.get(i).getCity().getCityName().equals(center)) {
					for (int j=0; j< distCenter.get(i).getInventory().size() ; j++) {
						if (distCenter.get(i).getInventory().get(j).getProduct().getID() == prodID){
							availableQuantity = distCenter.get(i).getInventory().get(j).getQuantity();
							break;
						}
					}
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during prodInquiry"); // could be exception in reading bin
		}
		return availableQuantity;
	}

	/**
	 * Updates the stock quantity of the product identified by prodID
	 * @param prodID -> The product ID to be updated
	 * @param distCentre -> Distribution Center (in effect a city name)
	 * @param quantity -> Quantity to add to the existing quantity
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 * If currently the product 112 has quantity 100 in Toronto,
	 * after the statement updateQuantity(112, "Toronto", 51)
	 * same product must have quantity 151 in the Toronto distribution center. 
	 * @return -> true if the operation could be performed, false otherwise.
	 */
	public boolean updateQuantity(int prodID, String distCentre, int quantity, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		boolean operation = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			List<DistributionCenter> distCenter = shopDataSave.shop.distributionCenters;
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges()) { // check for matching sessionID & if admin. only admin can add product
					for (int i=0; i< distCenter.size() ; i++) {
						if (distCenter.get(i).getCity().getCityName().equals(distCentre)) {
							for (int j=0; j< distCenter.get(i).getInventory().size() ; j++) {
								if (distCenter.get(i).getInventory().get(j).getProduct().getID() == prodID){
									int curQuantity = distCenter.get(i).getInventory().get(j).getQuantity();
									distCenter.get(i).getInventory().get(j).setQuantity(quantity + curQuantity);
									operation = true;
								}
							}
						}
					}
				}
			} 
			// this does not take into account multiple identical Admin added into the same shopper.
			// assumes 1 admin. thats how our code was written
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during updateQuantity"); // could be exception in reading bin
		}
		return operation;
	}

	/**
	 * Adds two nodes cityA, cityB to the shipping graph
	 * Adds a route (an edge to the shipping graph) from cityA to cityB with length distance
	 * If the nodes or the edge (or both) exist, does nothing
	 * @param cityA 
	 * @param cityB
	 * @param distance -> distance (in km, between cityA and cityB)
	 * @param sessionID -> A session ID that belongs to an authenticated administrator
	 */
	public void addRoute(String cityA, String cityB, int distance, int sessionID) {
		// Your code goes here
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges() == true) {
					Graph<City> g = shopDataSave.shop.cityGraph;
					/**
			    	ArrayList<City> cities = g.getVertices();
			    	City A = null;
			    	City B = null;
			    	for(int i=0; i<cities.size(); i++){
			    		if(cities.get(i).getCityName().equals(cityA))
			    			A = cities.get(i);
			    		if(cities.get(i).getCityName().equals(cityB))
			    			B = cities.get(i);
			    	}
			    	if(A==null){
			    		A = new City(cityA);
			    		g.addVertex(A);
			    	}
			    	if(B==null){
			    		B = new City(cityB);
			    		g.addVertex(B);
			    	}
					 **/
					City A = new City(cityA);
					City B = new City(cityB);
					double d = Double.valueOf(Integer.toString(distance));
					((Admin)user).addRoute(A, B, d, shopDataSave.shop);	
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addRoute"); // could be exception in reading bin
		}	
	}

	/**
	 * Attempts an order in behalf of custID for quantity units of the prodID
	 * @param custID -> The customer ID
	 * @param prodID -> The product ID
	 * @param quantity -> The desired quantity
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The orderID if successful, -1 if not.
	 */
	public int placeOrder(int custID, int prodID, int quantity, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		// add to cart method to add product
		int invoiceOrderID = -1;

		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			ArrayList<Product> observers = (ArrayList<Product>) shopDataSave.shop.getObservers();
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && user.getPrivileges() == false) {//right now only shopper can place order
					for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
						if (((Shopper)user).getCustomerList().get(i).customerID == custID) {
							for (int j=0; i< observers.size() ; j++) { //size of list of products
								if (observers.get(j).getID() == prodID) { //if productID exist
									Product p = observers.get(j); //save the reference
									((Shopper)user).getCustomerList().get(i).addToCart(p, quantity, shopDataSave.shop); //add productOBject to respective custID's cart
									Invoice invoice = ((Shopper)user).getCustomerList().get(i).checkout(shopDataSave.shop); //generate invoice
									System.out.println("Invoice Total Price: "+ invoice.getPrice() + " called from API");
									//			    					invoice =null;
									if (invoice.equals(null)) {
										break;
									}
									invoiceOrderID = invoice.getID(); //return invoice/orderID
									break;
								}
							}
						}   		
					}
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during placeOrder"); // could be exception in reading bin
		}

		return invoiceOrderID;
	}

	/**
	 * Returns the best (shortest) delivery route for a given order 
	 * @param orderID -> The order ID we want the delivery route
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return -> The actual route as an array list of cities, null if not successful
	 */
	public ArrayList<String> getDeliveryRoute(int orderID, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		ArrayList<DistributionCenter> allowable = null;
		ArrayList<City> allowableCities = new ArrayList<City>();
		ArrayList<String> cityPath = new ArrayList<String>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && !user.getPrivileges()) {//right now only shopper can place order
					for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
						for (int j=0; i<((Shopper)user).getCustomerList().get(i).getInvoice().size(); j++) {
							if (((Shopper)user).getCustomerList().get(i).getInvoice().get(j).getID() == orderID){
								Customer cst = ((Shopper)user).getCustomerList().get(i);
								Product prod = cst.getInvoice().get(j).getProductList().get(0).getProduct();
								int prodQuantity = cst.getInvoice().get(j).getProductList().get(0).getQuantity();
								allowable = ((Shopper)user).getCustomerList().get(i).getCart().availableCentres(prod, prodQuantity);
								for(int l=0; l<allowable.size(); l++){
									allowableCities.add(allowable.get(l).getCity());
								}

								ArrayList<City> cities = shopDataSave.shop.cityGraph.getVertices();
								City cstCity = null;
								for(int m=0; m<cities.size(); m++){
									if(cities.get(m).getCityName().equals(cst.customerCity))
										cstCity = cities.get(m);
								}
								/**
			    				ArrayList<City> allowableCities2 = new ArrayList<City>();
			    				for(int z=0; i<allowableCities.size(); i++){
			    					for(int y=0; y<cities.size(); y++)
			    						if(allowableCities.get(z).getCityName().equals(cities.get(y).getCityName()))
			    							allowableCities2.add(cities.get(y));
			    				}
								 **/
								ArrayList<Integer> cityNodes = shopDataSave.shop.cityGraph.findShortestPair(cstCity, allowableCities);

								for(int n=0; n<cityNodes.size(); n++){
									cityPath.add(cities.get(cityNodes.get(n)).getCityName());
								}
								return cityPath;
							}
						}
					}
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getdeliveryroute"); // could be exception in reading bin
		}
		return cityPath;
	}

	/** 
	 * Computes the invoice amount for a given order.
	 * Please use the fixed price 0.01$/km to compute the shipping cost 
	 * @param orderID
	 * @param sessionID -> A valid sessionID that belongs to an authenticated shopper user.
	 * @return
	 */
	public double invoiceAmount(int orderID, int sessionID) {
		// Your code goes here.
		// Replace the statement below with the correct code.
		double invoiceTotalPrice = -1;

		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				if (user.activeSession && user.sessionID == sessionID && !user.getPrivileges()) {//right now only shopper can place order
					for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
						for (int j=0; i< ((Shopper)user).getCustomerList().get(i).getInvoice().size() ; j++) {
							if (((Shopper)user).getCustomerList().get(i).getInvoice().get(j).getID() == orderID) {
								invoiceTotalPrice = ((Shopper)user).getCustomerList().get(i).getInvoice().get(j).getPrice();
								break;
							}
						}	   		
					}
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during placeOrder"); // could be exception in reading bin
		}

		return invoiceTotalPrice;
	}

}
