package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import finalproject.ProjectV1.ShoppingCartItem;

public class Customer implements Serializable {
	/**
	 * The customer class customer is the entity that makes purchases. Customer may exist in a List in Shopper, although
	 * in the current implementation there is a one to one correspondence between shoppers and customers. 
	 * The shopper is the entity that takes care of authentication.
	 * 
	 * <p>
	 * Customers make purchases on the web site. They have the authentication information inherited from the 
	 * RegisteredUser and shopper classes and also contain address information, a shopping cart and a list 
	 * of previous invoices for billing purposes.
	 * 
	 * @author Vincent
	 * @version 1.0
	 * @see Shopper#customerList
	 */
	private static final long serialVersionUID = 705368960342937846L;
	private Cart cart;
	private ArrayList<Invoice> invoice;
	private City city;
	String customerName;
	String customerCity;
	String customerStreet;
	static int customerIdGenerator = 0;
	int customerID;
	List<Product> loginOutofStockProducts = new ArrayList<Product>();
	boolean changes = false;
	List<Product> loginOutofStockProductsChanges = new ArrayList<Product>();

	Customer(String custName, String city, String street) {
		this.cart = new Cart(this);
		this.invoice = new ArrayList<Invoice>();
		this.city = new City(city);
		this.customerName = custName;
		this.customerCity = city;
		this.customerStreet = street;
		
		customerIdGenerator++;
		this.customerID = customerIdGenerator; // to be used by API
	}
	
	/**
	 * Clears the cart and puts the quantities back into each product's respective available quantities.
	 * @param shop -> Shop that holds the products.
	 * @return
	 */
	protected boolean cancelCart(Shop shop){
		boolean cancelled = false;
		logoutSetQuantities(shop); //recover AvailableQuantities
		this.cart.clearCart();
		if (this.cart.getShopI().isEmpty()) {
			cancelled = true;
		}
		return cancelled;
	}
	
	/**
	 * Sets available quantities back to normal when user logs out. 
	 * This allows other customers to access the quantities that this cart had reserved.
	 * 
	 * @param shop
	 */
	protected void logoutSetQuantities(Shop shop) {

		
		for(int i=0; i < cart.getShopI().size(); i++){
			Product product = cart.getShopI().get(i).getProduct();
			int quantity = cart.getShopI().get(i).getQuantity();
			
			for (int j=0; j < shop.getObservers().size(); j++) {
				if (shop.getObservers().get(j).getID() == product.getID()) {
					System.out.println("availqty before logout: " + shop.getObservers().get(j).getAvailQuantity());
					shop.getObservers().get(j).setAvailQuantity(shop.getObservers().get(j).getAvailQuantity() + quantity);
					System.out.println("availqty in loop b4 logout: " + shop.getObservers().get(j).getAvailQuantity());
				}
			}
		}
	}
	
	/**
	 * Updates the cart, checks if items saved at logout are not sold out, and creates a new cart with 
	 * items that are not sold out.
	 * 
	 * @param shop
	 */

	protected void loginReloadCart(Shop shop){
		// updates the cart, checks if items saved at logout are not sold out, and 
		// creates a new cart with items that are not sold out.
		Cart oldCart = this.cart;
		Cart cartNew = new Cart(this);
		setCart(cartNew);
		
		for(int i=0; i < oldCart.getShopI().size(); i++){
			ShopItem item = oldCart.getShopI().get(i);
			boolean successful = addToCart(item.getProduct(), item.getQuantity(), shop);
			if (!successful) {
				this.loginOutofStockProducts.add(item.getProduct());
			}
		}
	}

	public int getCustomerID() {
		return customerID;
	}
	
	/**
	 * Gets the shipping cost for a certain path in the graph in shop.
	 * @param path -> The path from city to city.
	 * @param shop -> The shop that contains the graph.
	 * @return -> The shipping cost.
	 */
	public double getShippingCost(ArrayList<Integer> path, Shop shop){
		double distance = shop.cityGraph.getPathDistance(path);
		System.out.println("distance is: " + distance);
		return distance*0.01 * shop.SHIPPINGFACTOR;
	}
	
	/**
	 * Checks out the cart. This involves computing the appropriate shipping cost,
	 * generating an invoice, and finally reseting the cart.
	 * <p>
	 * Customers check out their purchases once they are done shopping. If the customer
	 * is unable to do so null will be returned.
	 * 
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @return -> Invoice if the purchases went through or null otherwise
	 * @throws VertexExistsException 
	 * @see Cart#getCart()
	 * @see Cart#clearCart()
	 */
	public Invoice checkout(Shop shop){ 
		Invoice curInvoice = new Invoice(this);
		for(int i=0; i < cart.getShopI().size(); i++){
			ShopItem item = cart.getShopI().get(i);
			for(int j=0; j < shop.getObservers().size(); j++) {
				if (shop.getObservers().get(j).getID() == item.getProduct().getID()) {
					Product product = shop.getObservers().get(j);
					ShoppingCartItem scItem = cart.getShippingCenters(city, product, item.getQuantity(), shop);
					System.out.println(scItem);
					cart.checkoutItem(scItem, shop);
					curInvoice.addToInvoice(item);
				}
			}
		}
		ArrayList<City> allowable = new ArrayList<City>();
		for(int k=0; k < shop.distributionCenters.size(); k++){
			allowable.add(shop.distributionCenters.get(k).getCity());
		}
		ArrayList<Integer> path = shop.cityGraph.findShortestPair(city, allowable);
		curInvoice.setPrice(curInvoice.getPrice() + getShippingCost(path, shop)); //getShippingCost(path, shop)
		this.invoice.add(curInvoice);
		this.cart.clearCart();
		return curInvoice;
	}

	/**
	 * Adds an item to the customers cart.
	 *
	 * @param product -> The product to be added to the cart.
	 * @param quantity -> The quantity to be added.
	 * @see Cart#add(ShopItem)
	 */
	public boolean addToCart(Product product, int quantity, Shop shop){
		for(int i=0; i < shop.getObservers().size(); i++) {
			if(shop.getObservers().get(i).getID() == product.getID()){ 
				if(quantity <= shop.getObservers().get(i).getAvailQuantity()){ 
					if(cart.findShopItem(product.getID()) != -1){
						int oldQty = cart.getShopI().get(cart.findShopItem(product.getID())).getQuantity();
						removeFromCart(product, shop);
						ShopItem upItem = new ShopItem(shop.getObservers().get(i), quantity + oldQty , shop.getObservers().get(i).getPrice());
						shop.getObservers().get(i).setAvailQuantity(shop.getObservers().get(i).getAvailQuantity() - upItem.getQuantity());
						cart.add(upItem);
						return true;
					} else {
						ShopItem item = new ShopItem(shop.getObservers().get(i), quantity, shop.getObservers().get(i).getPrice());
						cart.add(item);
						shop.getObservers().get(i).setAvailQuantity(shop.getObservers().get(i).getAvailQuantity() - quantity);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes a product from the cart.
	 * @param product -> Product you wish to remove.
	 * @param shop -> Shop that holds the products.
	 * @return
	 */
	public boolean removeFromCart(Product product, Shop shop){
		for(int i=0; i < shop.getObservers().size(); i++) {
			Product prod = shop.getObservers().get(i);
			if(prod.getID() == product.getID()){ 
				ShopItem cartProd = null;
				boolean flag = false;
				for(int j=0; j<cart.getShopI().size(); j++){
					if(cart.getShopI().get(j).getProduct().getID()==prod.getID()){
						cartProd = cart.getShopI().get(j);
						flag = true;
					}
				}
				if(flag){
					cart.remove(cartProd);
					int newQnt = prod.getAvailQuantity()+cartProd.getQuantity();
					prod.setAvailQuantity(newQnt);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Update the cart quantity of a product that is already inside the shopping cart.
	 * @param prodID -> ID of the product you wish to update.
	 * @param quantity -> New quantity of the product after the update.
	 * @param shop -> Shop that holds the products.
	 * @return
	 */
	public boolean updateCartQuantity(int prodID, int quantity, Shop shop){
		if(cart.findShopItem(prodID) != -1){
			ShopItem item = cart.getShopI().get(cart.findShopItem(prodID));
			if(item.getQuantity() > quantity){
				removeFromCart(item.getProduct(), shop);
				addToCart(item.getProduct(), quantity, shop);
				return true;
			} else if(item.getQuantity() < quantity){
				addToCart(item.getProduct(), quantity - item.getQuantity(), shop);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Invoice> getInvoice() {
		return invoice;
	}

	public Cart getCart() {
		return cart;
	}

	public static int getCustomerIdGenerator() {
		return customerIdGenerator;
	}

	public static void setCustomerIdGenerator(int customerIdGenerator) {
		Customer.customerIdGenerator = customerIdGenerator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public City getCity() {
		return city;
	}
	
	public void setCity(City c){
		this.city = c;
	}

	public List<Product> getLoginOutofStock() {
		return loginOutofStockProducts;
	}

	public void setLoginOutofStock(List<Product> loginOutofStock) {
		this.loginOutofStockProducts = loginOutofStock;
	}
	
	public void setCart(Cart c){
		this.cart = c;
	}
}
