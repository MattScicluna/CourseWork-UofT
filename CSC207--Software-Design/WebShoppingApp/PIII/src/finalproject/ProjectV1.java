package finalproject;

import java.util.ArrayList;
import java.util.List;

public class ProjectV1 extends Project {

	/** 
	 * As a general purchase procedure:
	 *	1) A shopper logs in
	 *	2) Shopper adds items to the cart
	 *	3) If available, items get added accordingly
	 *	4) Checkout (method in API, or button in GUI) generates a new order (and automatically computes 
	 *     shipping and generates a new invoice).
     *
	 *	Now - a word on inventory maintenance - explained via an example.
     *
	 *	Say shopper s adds product p in his cart; the desired quantity comes from warehouse A and warehouse B because none 
	 *  of them has enough.
	 *	Please observe at this stage the shopper has not checked out yet; the shopper is still logged in to the system. 
	 *	To make it more practical, say warehouse A has 10 pieces, warehouse B has 7 pieces, the shopper has added to cart 15 pieces.
	 *	As long as the shopper is logged in, other shoppers should be able to see only 2 pieces available.
     *
	 *	At this point, we have two cases:
	 *	1) Shopper hits checkout. The system should do the following:
	 *	-reduce the inventory (permanently) to 2 pieces.
	 *	-compute the nearest warehouse to the shopper's city. Let this warehouse be the warehouse in city C.
	 *	-the shipping has to be computed from C.
	 *	-So here what should happen is: the company has to move the merchandise from warehouse A (10 units) and from warehouse B 
	 *   (5 units) [or, if you desire so, 7 units from warehouse B and 8 units from warehouse A] to the warehouse C. 
	 *   This operation is "invisible" for us (that is you can assume the merchandise moves using procedures to be programmed in the future)
     *
	 *	2) Shopper hits logout WITHOUT doing a checkout. Please store the shopping cart and recover the inventory. 
	 *     So to make it clear, the shopping cart contains 15 units from product p, the shopper is logged out, other shoppers 
	 *     see 17 available (in total).
     *
	 *	3) The shopper logs in again. If in the mean time the merchandise sold out (in full or in part) the product in question 
	 *     must be removed from shopping cart (if you are in GUI, the user should get a notification message). If the merchandise is 
	 *     still there go to point 1 above.
	 */
	
	
	
	
	
	/**
	 * This method attempts to add quantity units from the product identified by prodID to the cart.
	 * Returns true if the operation succeeds, false, if then operation fails.
	 * 
	 * @param productID
	 * @param quantity
	 * @param sessionID
	 * @param custID
	 * @return
	 */
	public boolean addToShoppingCart(int productID, int quantity, int sessionID, int custID) {
		// your code goes here
		boolean operationSuccess = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			
			ArrayList<Product> observers = (ArrayList<Product>) shopDataSave.shop.getObservers();
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && user.sessionID == sessionID && !user.getPrivileges()) {//right now only shopper can place order
			    	for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
			    		if (((Shopper)user).getCustomerList().get(i).customerID == custID) {
			    			
			    			for (int j=0; j< observers.size() ; j++) { //size of list of products
			    				if (observers.get(j).getID() == productID) { //if productID exist
			    					Product p = observers.get(j); //save the reference
			    					System.out.println("available quantity before adding to cart: "+ p.getAvailQuantity());
			    					operationSuccess = ((Shopper)user).getCustomerList().get(i).addToCart(p, quantity, shopDataSave.shop); //add productOBject to respective custID's cart
			    					
			    					System.out.println("available quantity after adding to cart: "+ p.getAvailQuantity());
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
			System.out.println("exception encountered during addToShoppingCart"); // could be exception in reading bin
		}
		return operationSuccess;
	}
	
	/**
	 * Cancels the Customer's cart.
	 * 
	 * @param userName 
	 * @return a boolean if the cart was cancelled.
	 */
	public boolean cancelCart(String userName) {
		boolean cancelled = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for (RegisteredUser user : userDataSave.mapUser.values()) {
				if(!user.getPrivileges()) {
					if (((Shopper)user).getUserID().equals(userName)) {
						if (!((Shopper)user).getCustomerList().isEmpty() && !((Shopper)user).getCustomerList().get(0).getCart().getShopI().isEmpty()) {
							cancelled = ((Shopper)user).getCustomerList().get(0).cancelCart(shopDataSave.shop);
							break;
						}
					}
				}
			}
			
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addToShoppingCart"); // could be exception in reading bin
		}
		
		return cancelled;
	}
	
	/**
	 * Removes a product from the customer's shopping cart.
	 * 
	 * @param productID
	 * @param sessionID
	 * @param custID
	 * @return a boolean if the product was successfully removed.
	 */
	public boolean removeFromShoppingCart(int productID, int sessionID, int custID) {
		boolean operationSuccess = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			
			ArrayList<Product> observers = (ArrayList<Product>) shopDataSave.shop.getObservers();
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && user.sessionID == sessionID && !user.getPrivileges()) {//right now only shopper can place order
			    	for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
			    		if (((Shopper)user).getCustomerList().get(i).customerID == custID) {
			    			
			    			for (int j=0; j< observers.size() ; j++) { //size of list of products
			    				if (observers.get(j).getID() == productID) { //if productID exist
			    					Product p = observers.get(j); //save the reference
			    					System.out.println("available quantity before removing from cart: "+ p.getAvailQuantity());
			    					operationSuccess = ((Shopper)user).getCustomerList().get(i).removeFromCart(p, shopDataSave.shop); //remove productOBject from respective custID's cart
			    					
			    					System.out.println("available quantity after removing from cart: "+ p.getAvailQuantity());
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
			System.out.println("exception encountered during addToShoppingCart"); // could be exception in reading bin
		}
		return operationSuccess;
	}
	
	/**
	 * Updates the quantities of a product in the Customer's cart.
	 * 
	 * @param productID
	 * @param quantity
	 * @param sessionID
	 * @param custID
	 * @return a boolean if the operation was successful.
	 */
	public boolean updateCartQuantity(int productID, int quantity, int sessionID, int custID) {
		boolean operationSuccess = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			
			ArrayList<Product> observers = (ArrayList<Product>) shopDataSave.shop.getObservers();
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && user.sessionID == sessionID && !user.getPrivileges()) {//right now only shopper can place order
			    	for (int i=0; i< ((Shopper)user).getCustomerList().size() ; i++) {
			    		if (((Shopper)user).getCustomerList().get(i).customerID == custID) {
			    			
			    			for (int j=0; j< observers.size() ; j++) { //size of list of products
			    				if (observers.get(j).getID() == productID) { //if productID exist
			    					Product p = observers.get(j); //save the reference
			    					System.out.println("available quantity before updating cart: "+ p.getAvailQuantity());
			    					operationSuccess = ((Shopper)user).getCustomerList().get(i).updateCartQuantity(productID, quantity, shopDataSave.shop);
			    					System.out.println("available quantity after updating cart: "+ p.getAvailQuantity());
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
			System.out.println("exception encountered during addToShoppingCart"); // could be exception in reading bin
		}
		return operationSuccess;
	}
	
	/**
	 * Returns the available quantity of a product
	 * 
	 * @param prodID
	 * @param sessionID
	 * @return The quantity of a searched product.
	 */
	public int availQ(int prodID) {
		int num = -1;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			
			ArrayList<Product> observers = (ArrayList<Product>) shopDataSave.shop.getObservers();
			for (int j=0; j< observers.size() ; j++) { //size of list of products
				if (observers.get(j).getID() == prodID) { //if productID exist
//					System.out.println("worked");
					Product p = observers.get(j); //save the reference	
					num = p.getAvailQuantity();
					break;
				}
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during addToShoppingCart"); // could be exception in reading bin
		}
		return num;
	}
	
	/** 
	 * Returns the shopping cart content of the shopper userName.
	 * 
	 * An item in the shopping cart must look like this sample:
	 * prodID: 111
	 * center: (An arraylist with components) "Toronto","Barrie"
	 * quantity: (An arraylist with components) 2,3
	 * 
	 * The sample means this particular item on the shopping cart is the product with product ID=111,
	 * the total quantity in the cart is 5, this quantity is taken 2 pieces from the warehouse located in Toronto
	 * and 3 pieces from the warehouse located in Barrie.
	 *  
	 * @param userName
	 * @return
	 */
	public List<ShoppingCartItem> getShoppingCart(String userName) { //String userName --->custID
		// your code goes here
		List<ShoppingCartItem> cartList = new ArrayList<ShoppingCartItem>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && !user.getPrivileges() && user.userID.equals(userName)) { // 1:1 Shoppper:customer. NEW
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
				    	List<ShopItem> shoppingCart = ((Shopper)user).getCustomerList().get(0).getCart().getShopI();
				    	for(int i=0; i < shoppingCart.size(); i++){
				    		Product product = shoppingCart.get(i).getProduct();
				    		int quantity = shoppingCart.get(i).getQuantity();
				    		for(int j=0; j < shopDataSave.shop.getObservers().size(); j++){
				    			if (shopDataSave.shop.getObservers().get(j).getID() == product.getID()) {
						    		ShoppingCartItem item = ((Shopper)user).getCustomerList().get(0).getCart().getShippingCenters(((Shopper)user).getCustomerList().get(0).getCity(),shopDataSave.shop.getObservers().get(j), quantity, shopDataSave.shop);
						    		cartList.add(item);

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
			System.out.println("exception encountered during getShoppingCart"); // could be exception in reading bin
		}
		return cartList;
	}
	
	/**
	 * Return a list of products that have been sold out from the shopper's cart after the shopper 
	 * logs back in.
	 * 
	 * @param sessionID
	 * @return a list of products that have been removed.
	 */
	public String getLoginOutofStockProducts(int sessionID) { //String userName --->custID
		// your code goes here
		List<Product> cartList = new ArrayList<Product>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && !user.getPrivileges() && user.sessionID == sessionID) { // 1:1 Shoppper:customer. NEW
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
				    	cartList = ((Shopper)user).getCustomerList().get(0).loginOutofStockProductsChanges;
				    	((Shopper)user).getCustomerList().get(0).changes = false;
			    	}

			    }
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getLoginOutofStockProducts"); // could be exception in reading bin
		}
		return cartList.toString();
		
	}
	
	public boolean checkForCartChanges(String username) {
		boolean result = false;
		try {
			String path = "UserDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (!user.getPrivileges() && user.getUserID().equals(username)) { // 1:1 Shoppper:customer. NEW
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
				    	result = ((Shopper)user).getCustomerList().get(0).changes;
			    	}

			    }
			}
			userDataSave.saveToFile(path);
		}
		catch (Exception e) {
			System.out.println("exception encountered during checkForCartChanges"); // could be exception in reading bin
		}
		return result;	
	}
	
	/**
	 * Clears the items that are out of stock from the user's out of stock list.
	 * 
	 * @param sessionID
	 * @return boolean if the operation was successful.
	 */
	public boolean clearLoginOutofStockProducts(int sessionID) { //String userName --->custID
		// your code goes here
		List<Product> cartList = new ArrayList<Product>();
		boolean successful = false;
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && !user.getPrivileges() && user.sessionID == sessionID) { // 1:1 Shoppper:customer. NEW
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
				    	((Shopper)user).getCustomerList().get(0).setLoginOutofStock(cartList);
				    	successful = ((Shopper)user).getCustomerList().get(0).getLoginOutofStock().isEmpty();
			    	}

			    }
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during clearLoginOutofStockProducts"); // could be exception in reading bin
		}
		return successful;
		
	}
	
	/**
	 * Returns the category ID given the category name.
	 * 
	 * @param CatName
	 * @param sessionID
	 * @return category ID
	 */
	public int getNumFromName(String CatName, int sessionID) { //String userName --->custID
		// returns the CatID given catName, and sessionID, and generates a new Category and returns its ID 
		// if the category name given is not found in the dataSave.
		int num = -1;

		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for(int i=0; i < shopDataSave.shop.category.size(); i++) {
				if (shopDataSave.shop.category.get(i).getCategoryName().equals(CatName)) {
					num = shopDataSave.shop.category.get(i).getCategoryID();
					break;
				}
			}
			if (num == -1) {
				for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
				    if (user.activeSession && user.sessionID == sessionID && user.getPrivileges()) { // check for matching sessionID & if admin
				    	num = ((Admin)user).addCategory(CatName, shopDataSave.shop);
				    }
				}
			}
		    userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getNumFromName"); // could be exception in reading bin
		}
		return num;
		
	}
	
	/**
	 * Returns the custormer's ID given customer name.
	 * 
	 * @param username
	 * @return customer ID
	 */
	public int getCustIDFromUserName(String username) { 
		//returns custID for a given username if it exist, else return -1.
		int custID = -1;

		try {
			String path = "UserDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (!user.getPrivileges() && user.getUserID().equals(username)) {
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
			    		custID = ((Shopper)user).getCustomerList().get(0).getCustomerID();
			    		break;
			    	}
			    }
			}
		    userDataSave.saveToFile(path);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getNumFromUserName"); // could be exception in reading bin
		}
		return custID;
		
	}
	
	/**
	 * Return the product ID given the product name.
	 * 
	 * @param prodName
	 * @return product ID
	 */
	public int getProdIDFromProdName(String prodName) { 
		//returns prodID given prodName if it exist, else returns -1.
		int custID = -1;

		try {
			String pathShop = "ShopDataAPI4.bin";
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for(int i=0; i < shopDataSave.shop.getObservers().size(); i++) {
				if (shopDataSave.shop.getObservers().get(i).getName().equals(prodName)) {
					custID = shopDataSave.shop.getObservers().get(i).getID();
					break;
				}
			}
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getProdIDFromProdName"); // could be exception in reading bin
		}
		return custID;
		
	}
	
	/**
	 * Return the product Name given the product ID.
	 * 
	 * @param prodID
	 * @return product name.
	 */
	public String getProdNameFromProdID(int prodID) { 
		//returns prodID given prodName if it exist, else returns -1.
		String custName = "ProductID does not exist.";

		try {
			String pathShop = "ShopDataAPI4.bin";
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			for(int i=0; i < shopDataSave.shop.getObservers().size(); i++) {
				if (shopDataSave.shop.getObservers().get(i).getID() == prodID) {
					custName = shopDataSave.shop.getObservers().get(i).getName();
					break;
				}
			}
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getProdNameFromProdID"); // could be exception in reading bin
		}
		return custName;
		
	}
	
	/**
	 * Return the sales report of all customer purchases.
	 * 
	 * @param sessionID
	 * @return List of String that represent the invoices of all customers.
	 */
	public List<String> getInvoiceStrings(int sessionID) { //String userName --->custID
		// your code goes here
		List<String> cartList = new ArrayList<String>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.sessionID == sessionID && user.activeSession && user.getPrivileges()) { // 1:1 Shoppper:customer. NEW
			    	for (RegisteredUser shopper : userDataSave.mapUser.values()) {
			    		if ((!shopper.getPrivileges())) {
			    			if (!((Shopper) shopper).getCustomerList().isEmpty()) {
			    				if (!((Shopper) shopper).getCustomerList().get(0).getInvoice().isEmpty()){
			    					for(int i=0; i < ((Shopper) shopper).getCustomerList().get(0).getInvoice().size(); i++) {
			    						cartList.add(((Shopper) shopper).getCustomerList().get(0).getInvoice().get(i).toString());
			    					}
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
			System.out.println("exception encountered during getInvoiceStrings"); // could be exception in reading bin
		}
		return cartList;
	}
	
	/**
	 * Return the sales report of all customer purchases.
	 * 
	 * @param sessionID
	 * @return List of String that represent the invoices of all customers.
	 */
	public List<String> getSalesReport(int sessionID, int catID) { //String userName --->custID
		// your code goes here
		ArrayList<String> salesReport = new ArrayList<String>();
		ArrayList<Invoice> invReport = new ArrayList<Invoice>();
		ArrayList<Product> prodReport = new ArrayList<Product>();
		ArrayList<Integer> prodQnty = new ArrayList<Integer>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.sessionID == sessionID && user.activeSession && user.getPrivileges()) { // 1:1 Shoppper:customer. NEW
			    	for (RegisteredUser shopper : userDataSave.mapUser.values()) {
			    		if ((!shopper.getPrivileges())) {
			    			if (!((Shopper) shopper).getCustomerList().isEmpty()) {
			    				if (!((Shopper) shopper).getCustomerList().get(0).getInvoice().isEmpty()){
			    					for(int i=0; i < ((Shopper) shopper).getCustomerList().get(0).getInvoice().size(); i++) {
			    						invReport.add(((Shopper) shopper).getCustomerList().get(0).getInvoice().get(i));
			    					}
			    				}
			    			}
			    		}
			    	}
			    }
			}
			
			
			for(int j=0; j<invReport.size(); j++){
				Invoice inv = invReport.get(j);
				List<ShopItem> invItem = inv.getProductList();
				for(int k=0; k<invItem.size(); k++){
					Product prod = invItem.get(k).getProduct();
					if(prod.getCategory().getCategoryID()==catID){
						boolean flag = false;
						for(int z=0; z<prodReport.size(); z++){
							if(prodReport.get(z).getID()==prod.getID()){
								int newQnty = prodQnty.get(z)+invItem.get(k).getQuantity();
								prodQnty.set(z, newQnty);
								flag = true;
							}
						}
						if(!flag){
							prodReport.add(prod);
							int newQnty = invItem.get(k).getQuantity();
							prodQnty.add(newQnty);
						}
					}
				}
			}
			
			for(int l=0; l<prodReport.size(); l++){
				String s = "<html>";
				s+=prodQnty.get(l)+" "+prodReport.get(l).getName()+"'s were bought.<br></html>";
				salesReport.add(s);
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getInvoiceStrings"); // could be exception in reading bin
		}
		return salesReport;
	}
	
	/**
	 * Returns all the invoices of a specified customer.
	 * 
	 * @param username of customer
	 * @return List of Strings representing a specified customer's invoices.
	 */
	public List<String> getInvoiceForCustomer(String username) { //String userName --->custID
		// your code goes here
		List<String> cartList = new ArrayList<String>();
		List<Invoice> invoiceList  = new ArrayList<Invoice>();
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);

			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (!user.getPrivileges() && user.userID.equals(username)) { 
			    	if (!((Shopper)user).getCustomerList().isEmpty()) {
			    		invoiceList = ((Shopper)user).getCustomerList().get(0).getInvoice();
			    		break;
			    	}
			    }
			}
			if (!invoiceList.isEmpty()) {
				for(int i=0; i < invoiceList.size(); i++) {
					cartList.add(invoiceList.get(i).toString());
				}
			}
			
			userDataSave.saveToFile(path);
		}
		catch (Exception e) {
			System.out.println("exception encountered during getInvoiceStrings"); // could be exception in reading bin
		}
		return cartList;
	}
	

	/**
	 * This method checks out the shopping cart, generates an order and returns order ID.
	 * It is the equivalent of the checkout button in your GUI screen.
	 * @param sessionID
	 * @param custID
	 * @return
	 */
	public int checkout(int custID, int sessionID) { //custID not used because of shopper:customer 1:1 
		int invoiceOrderID = -1;
		
		try {
			String path = "UserDataAPI4.bin";
			String pathShop = "ShopDataAPI4.bin";
			UserManager userDataSave = new UserManager(path);
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			
			for (RegisteredUser user : userDataSave.mapUser.values()) { // loops over HashMap's Values. //  note: HashMap<String, RegisteredUser>
			    if (user.activeSession && user.sessionID == sessionID && user.getPrivileges() == false) {
			    		if (((Shopper)user).getCustomerList().get(0).customerID == custID) {// 1:1 Shoppper:customer. NEW
			    					Invoice invoice = ((Shopper)user).getCustomerList().get(0).checkout(shopDataSave.shop); //generate invoice
			    					System.out.println("Invoice Total Price: "+ invoice.getPrice() + " called from ProjectV1.java");
			    					if (invoice.equals(null)) {
			    						break;
			    					}
			    					invoiceOrderID = invoice.getID(); //return invoice/orderID
			    					break;
			    		}	
			    }
			}
			userDataSave.saveToFile(path);
			shopDataSave.saveToFile(pathShop);
		}
		catch (Exception e) {
			System.out.println("exception encountered during checkout ProjectV1.java"); // could be exception in reading bin
		}
		return invoiceOrderID;
	}
	
	/**
	 * Returns a list of products within the price range of x and y, and a chosen method of sorting.
	 * 
	 * @param x
	 * @param y
	 * @param PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown
	 * @return a sorted list of products.
	 */
	/**
	 * @param x
	 * @param y
	 * @param PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown
	 * @return
	 */
	public List<Product> browseShop(double x, double y, String PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown) {
		
		List<Product> productList = new ArrayList<Product>();
		try {
			String pathShop = "ShopDataAPI4.bin";
			ShopManager2 shopDataSave = new ShopManager2(pathShop);
			productList = shopDataSave.shop.limitPrice(x, y, PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown);
		}
		catch (Exception e) {
			System.out.println("exception encountered during checkout ProjectV1.browseShop.java"); // could be exception in reading bin
		}
		return productList;
	}
	
	
	public class ShoppingCartItem {
		private int prodID;
		private List<String> center;
		private List<Integer> quantity;
		
		public ShoppingCartItem(int prodID, List<String> center, List<Integer> quantity) { //prodID from string to int
			this.prodID = prodID;
			this.center = center;
			this.quantity = quantity;
		}

		public int getProdID() {//prodID from string to int
			return prodID;
		}

		public void setProdID(int prodID) {//prodID from string to int
			this.prodID = prodID;
		}

		public List<String> getCenter() {
			return center;
		}

		public void setCenter(List<String> center) {
			this.center = center;
		}

		public List<Integer> getQuantity() {
			return quantity;
		}

		public void setQuantity(List<Integer> quantity) {
			this.quantity = quantity;
		}
		
		@Override
		public String toString(){
			String s = "";
			s+= prodID + " " + center + " " + quantity;
			return s;
		}
		
		
	}

}