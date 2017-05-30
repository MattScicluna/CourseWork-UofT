package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import finalproject.ProjectV1.ShoppingCartItem;

/**
 * @author Conrad
 *
 */
public class Cart implements Serializable{
	
	/**
	 * The cart holds the customers purchases in the form of a list of products and the quantity purchased. Items put in
	 * the cart are flagged, but this has only been utilized within the class. It may be utilized outside of this class in the next implementation.
	 * 
	 * <p>
	 * The purchasing information for each item is represented by the class shopItem, 
	 * each of which are added by {@link #add(ShopItem)}
	 * 
	 * @author Conrad
	 * @version 1.0
	 */
	private static final long serialVersionUID = -798353081039696004L;
	private Customer customer;
	private List<ShopItem>  ShopI;
	
	Cart(Customer customer){
		this.setCustomer(customer);
		this.ShopI = new ArrayList<ShopItem>();
	}
	
	/**
	 * Adds a shop item to the cart. Uses the flag variable to notify the shop if there is not enough inventory available 
	 * to perform the order.
	 * @param shopItem -> the shop item to be added to the cart
	 */
	public void add(ShopItem shopItem){
		ShopI.add(shopItem);
	}
	
	/**
	 * Removes a shop item from the cart.
	 * @param shopItem -> the shop item to be removed from the cart
	 */
	public void remove(ShopItem shopItem){
		Product product = shopItem.getProduct();
		for(int i=0; i<ShopI.size(); i++){
			if(ShopI.get(i).getProduct().equals(product)){
				ShopI.remove(i);
			}
		}
	}
	
	/**
	 * This will clear the cart. Intended to be called by the shopper. 
	 * */
	protected void clearCart() {
		this.ShopI.clear();
	}
	
	/**
	 * Gets the customer of the cart.
	 * @return -> Customer.
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * Sets the customer of the cart.
	 * @param customer -> Customer to be set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
/**
 * Checks which inventories contain sufficient numbers of product. Makes use of getInventory method of product class
 * and getDistributionCenter method of Inventory class.
 * @param product -> the product whose availability is to be checked
 * @param quantity -> the quantity specified
 * @return -> An ArrayList of distribution centers for which the product is available at the quantity specified
 * @see Product#getInventory()
 * @see Inventory#getDistributionCenter()
 */
	public ArrayList<DistributionCenter> availableCentres(Product product, int quantity){
			ArrayList<DistributionCenter> allowable = new ArrayList<DistributionCenter>();
		for(int i=0; i<product.getInventory().size(); i++){
			Inventory inv = product.getInventory().get(i);
			if(inv.getQuantity() >= quantity){
				allowable.add(inv.getDistributionCenter());
			}
		}
		return allowable;
	}
	
	/**
	 * Generates an invoice object and adds it to a shoppers list of invoices.
	 * @return -> Invoice generated for the Cart object
	 * @see Invoice#addToInvoice(ShopItem)
	 */
	public Invoice generateInvoice(){
		Invoice invoice = new Invoice(customer);
		for(int i=0; i < ShopI.size(); i++){
			invoice.addToInvoice(ShopI.get(i));
		}
		return invoice;
	}
	
	/** Returns a ShoppingCartItem containing an itemID, a list of distributionCenters (identified by cities)
	 * and the amount of product taken from each distribution center. Provided there is sufficient
	 * quantity of the product available, the method will choose the closest nonempty distribution
	 * center first, and then continues until the order has been satisfied.
	 * This is meant to keep track of the internal shipping of products between factories.
	 * Suppose the stock of product 1 is 40, distributed so that 3 are in Toronto, 7 in Ottawa, and 30 in Taipei.
	 * Suppose the customer lives in Montreal and wants to order 11 items.
	 * The closest cities to Montreal are Toronto, Ottawa, and Taipei, in increasing order of distance.
	 * Then method will return 
	 * 1, [Toronto, Ottawa, Taipei], [3, 7, 1].
	 * 
	 * @param cstCity - The city to which we will ship.
	 * @param prod - The product we are shipping.
	 * @param qnt - The total amount of product to be shipped.
	 * @param shop - The store.
	 * @return Returns a ShoppingCartItem of the form
	 * <Integer> Item ID, <String>[City1, City2, City3], <Integer>[5, 10, 15].
	 */
	public ShoppingCartItem getShippingCenters(City cstCity, Product prod, int qnt, Shop shop){
		ArrayList<String> c = new ArrayList<String>();
		ArrayList<Integer> quantities = new ArrayList<Integer>();
		int runningqnt = qnt;
		Graph<City> g = shop.cityGraph;
		ArrayList<City> cities = shop.cityGraph.getVertices();
		ArrayList<DistributionCenter> d = (ArrayList<DistributionCenter>)shop.distributionCenters;
		ArrayList<Integer> davail = new ArrayList<Integer>();
		for(int i=0; i<d.size(); i++){
			davail.add(prod.getAmount(d.get(i)));
		}
		ArrayList<City> allowable = new ArrayList<City>();
		for(int i=0; i<d.size(); i++){
			if(davail.get(i)>0)
				allowable.add(d.get(i).getCity());
		}
		while(runningqnt>0){
			ArrayList<Integer> v = g.findShortestPair(cstCity, allowable);
			City one = cities.get(v.get(0));
			DistributionCenter dOne = null;
			for(int z = 0; z<d.size(); z++){
				if(d.get(z).getCity().equals(one))
					dOne = d.get(z);
			}
			int r = prod.getAmount(dOne);
			if(r>runningqnt)
				r = runningqnt;
			
			c.add(one.getCityName());
			quantities.add(r);
			allowable.remove(one);
			
			int indx = d.indexOf(dOne);
			int updqnt = davail.get(indx) - r;
			davail.set(indx, updqnt);
			runningqnt -= r;
		}
		ProjectV1 pV1 = new ProjectV1(); // accessing anonymous class
		ProjectV1.ShoppingCartItem result = (pV1).new ShoppingCartItem(prod.getID(), c, quantities);
		return result;
		
		
	}
	
	/**
	 * Checkouts an item so the quantity of the item's product is decreased and subsequently
	 * the correct quantities are decreased from the corresponding distribution centers based on the
	 * customer's location.
	 * @param item -> A ShoppingCartItem in the cart.
	 * @param shop -> The shop that holds the distribution centers.
	 */
	public void checkoutItem(ShoppingCartItem item, Shop shop){
		ArrayList<String> centers = (ArrayList<String>) item.getCenter();
		ArrayList<Integer> quantities = (ArrayList<Integer>) item.getQuantity();
		for(int i=0; i < centers.size(); i++){
			for(int j=0; j < shop.distributionCenters.size(); j++){
				if(shop.distributionCenters.get(j).getCity().getCityName().equals(centers.get(i))){
					shop.distributionCenters.get(j).purchaseItem(item.getProdID(), quantities.get(i));
				}
			}
		}
	}
	
	/**
	 * Gets the list of ShopItems in the cart.
	 * @return -> List of ShopItems.
	 */
	public List<ShopItem> getShopI() {
		return ShopI;
	}
	
	/**
	 * Finds the index of the product in the shopping cart by its' product ID.
	 * @param prodID -> product ID of the product that wants to be found.
	 * @return -> The index of the item in the shopping cart.
	 */
	public int findShopItem(int prodID){
		for(int i=0; i < ShopI.size(); i++){
			if(ShopI.get(i).getProduct().getID() == prodID){
				return i;
			}
		}
		return -1;
	}
}
