package finalproject;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Admin responsible for adding and updating products, categories, distribution centers and cities.
 * 
 * <p>
 * An administrator of the web site is a registered user with special permissions to modify inventory and location information.
 * Unlike shoppers they cannot purchase and checkout objects, but can browse them. Their permissions can be found
 * by using the method {@link #getPrivileges()}
 * 
 * @author Conrad
 * @version 1.0
 */


public class Admin extends RegisteredUser implements Serializable {
	
	private static final long serialVersionUID = 733553750925808971L;
	final int sessionCode = 1;

    public Admin(String userID, String password, boolean privileges) {
        super(userID, password, privileges);
    }
    
    /**
	 * Sets session ID
	 * @return -> generated session ID
	 */
    @Override
    protected int setSessionID() {
        int sessionNum = RegisteredUser.incrementSessionID();
        System.out.println("global id is: " + sessionNum);
        String sessionString = Integer.toString(this.getSessionCode()) + Integer.toString(sessionNum);
        this.sessionID = Integer.valueOf(sessionString);
        return this.sessionID;
    }
    
    /**
	 * Sets session ID
	 * @param City -> The City in which the distribution center is located in
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @throws VertexExistsException
	 */
    protected void addCity(String cityName, Shop shop){
    	City city = new City(cityName);
		shop.cityGraph.addVertex(city);
    }
	
    /**
	 * Adds a new distribution center belonging to a city.
	 * @param City -> The City in which the distribution center is located in
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
     * @throws VertexExistsException 
	 */
	protected void addDistributionCenter(String cityName, Shop shop) throws VertexExistsException{
		City distCity = new City(cityName);
		shop.cityGraph.addVertex(distCity);
		
		DistributionCenter d = new DistributionCenter(distCity);
		if(!shop.distributionCenters.contains(d)){
			for(int i=0; i < shop.observers.size(); i++){
				Inventory inven = new Inventory(((Product)shop.observers.get(i)), d, 0);
				d.addInventory(inven);
			}
			shop.distributionCenters.add(d);
			shop.notifyObserver();
			System.out.println("DistributionCentre " + cityName + " " + shop + " added");
		}
	}
	
	/**
	 * Adds a new route from the source city to the target city with a set distance.
	 * @param source -> The starting point of the route.
	 * @param target -> The target point of the route.
	 * @param distance -> The distance from the source to the target in kilometers.
	 * @param shop -> The shop that holds the graph where the route is being added.
	 */
	protected void addRoute(City source, City target, double distance, Shop shop){
		shop.cityGraph.addVertex(source);
		shop.cityGraph.addVertex(target);
		shop.cityGraph.addEdge(source, target, distance);
	}
	
	/**
	 * Adds a new Category of product called categoryName.
	 * @param categoryName -> The name of the category
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @return -> Generated Category ID if successful, -1 if duplicate.
	 */
    protected int addCategory(String categoryName, Shop shop){
    	// add code to make it so you cannot have duplicate categories
    	for (int i=0; i< shop.category.size() ; i++) {
    		if (shop.category.get(i).getCategoryName().equals(categoryName)) {
    			return -1;
    		}
    	}
        Category cat = new Category(categoryName);
        shop.category.add(cat);
        System.out.println("size " + shop.category.size());
        System.out.println(shop.category);
        return cat.getCategoryID();
	}
    
    /**
	 * Creates a new product object called productName.
	 * @param prodName -> The product name
	 * @param desc -> Description of the product
	 * @param picture -> Image of the product
	 * @param price -> The product sales price
	 * @param category -> The product category ID.
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @return -> product ID if successful, otherwise returns -1.
	 */
	protected int addProduct(String name, double price, int category, Shop shop){
/*		System.out.println("size " + shop.category.size()); */
		for(int i=0; i < shop.category.size(); i++){
/*			System.out.println("bye"); */
			System.out.println("size " + shop.category.size());
			if(shop.category.get(i).getCategoryID() == category) {
				
				Product product = new Product(name,price, shop.category.get(i), shop);
				for(int j=0; j < shop.distributionCenters.size(); j++){
					Inventory inven = new Inventory(product, shop.distributionCenters.get(j), 0);
					shop.distributionCenters.get(j).addInventory(inven);
					product.update(shop.distributionCenters.get(j));
				}
				return product.getID();
				
			}
		}
		return -1;
		
	}
	
	/**
	 * Creates a new product object called productName.
	 * @param prodName -> The product name
	 * @param desc -> Description of the product
	 * @param picture -> Image of the product
	 * @param price -> The product sales price
	 * @param category -> The product category ID.
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @param file -> the location of picture to be used when displaying product in the GUI.
	 * @return -> product ID if successful, otherwise returns -1.
	 */
	protected int addProduct(String name, double price, int category, Shop shop, String file){
/*		System.out.println("size " + shop.category.size()); */
		for(int i=0; i < shop.category.size(); i++){
/*			System.out.println("bye"); */
			System.out.println("size " + shop.category.size());
			if(shop.category.get(i).getCategoryID() == category) {
				
				Product product = new Product(name,price, shop.category.get(i), shop);
				product.setPicture(file);
				System.out.println("just set file to: " + file);
				for(int j=0; j < shop.distributionCenters.size(); j++){
					Inventory inven = new Inventory(product, shop.distributionCenters.get(j), 0);
					shop.distributionCenters.get(j).addInventory(inven);
					product.update(shop.distributionCenters.get(j));
				}
				return product.getID();
				
			}
		}
		return -1;
		
	}
	
	/**
	 * Updates a product objects price, quantity and city fields.
	 * @param product -> The product to be updated.
	 * @param desc -> Updated Description of the product.
	 * @param picture -> Updated Image of the product.
	 * @param price -> Updated product sales price.
	 * @param quantity -> Updated product quantity.
	 * @param city -> City where distribution centers product is being updated.
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 */
	protected void updateProduct(Product product, double price, int quantity, City city, Shop shop){
		product.setPrice(price);
		updateQuantity(product, quantity, city, shop);
	}
	
	/**
	 * Updates a total quantity of a product object across distributionCenters.
	 * @param product -> The product to be updated.
	 * @param quantity -> Updated product quantity.
	 * @param city -> City where distribution centers product is being updated.
	 * @param shop -> the shop being operated on. There is at most one shop object at a time.
	 * @return -> true if updated quantity is non-negative and false otherwise.
	 * @see Shop#distributionCenters to get distribution centers
	 * @see Inventory#getProduct() to set quantity of product
	 */
	protected boolean updateQuantity(Product product,int quantity, City city, Shop shop){
		if(quantity < 0){
			return false;
		}
		for(int i=0; i < shop.distributionCenters.size(); i++){
			if(shop.distributionCenters.get(i).getCity().equals(city)){
				for(int j=0; j < shop.distributionCenters.get(i).inventory.size(); j++){
					if(shop.distributionCenters.get(i).inventory.get(j).getProduct().equals(product)){
						shop.distributionCenters.get(i).inventory.get(j).setQuantity(quantity);
					}
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "Admin [userID=" + userID + ", privileges=" + privileges + ", password=" + password + "]";
	}
	
    /**
     * Gets the sessionID.
     * @return -> The sessionID of the admin.
     */
    public int getSessionCode() {
        return sessionCode;
    }
	
}
