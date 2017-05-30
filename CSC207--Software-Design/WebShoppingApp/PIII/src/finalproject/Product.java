package finalproject;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Product implements Comparable<Product>,Serializable,Observer{

	/**
	 * The products at the store. They can be added and updated by the admin and purchased by the customer.
	 * The product has an ID, name, price, category, list of inventories it is maintained in and available and total quantities.
	 * Image and description for products have not been implemented yet.
	 * 
	 * @author Vincent
	 * @version 1.0
	 */
	private static final long serialVersionUID = 3272757987649541976L;
	private static int prodCount;
	private int ID;
	private String name;
	//	private String description;
	private String picture;
	protected double price;
	private Category category;
	private ArrayList<Inventory> inventoryList;
	protected Shop shop;
	private int availableQuantity;
	private int totalQuantity;

	public Product(String name, double price, Category category, Shop shop){ 
		prodCount++;
		ID = prodCount;
		this.name = name;
		/*		description = desc;
		this.picture = picture; */
		this.price = price;
		this.category = category;
		inventoryList = new ArrayList<Inventory>();
		this.shop = shop;
		shop.registerObserver(this);
		category.addProduct(this);
		availableQuantity = 0;
		totalQuantity = 0;
	}

	public int getID() {
		return ID;
	}

	public String getName(){
		return name;
	}
	
	public String getPicture() {
				return picture;
	}
	
	public void setPicture(String picture) {
				this.picture = picture;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
		category.updateProduct(this);	
	}

	public Category getCategory() {
		return category;
	}

	public ArrayList<Inventory> getInventory(){
		return inventoryList;
	}

	public void setTotalQuantity(){
		totalQuantity = getTotalQuantity();
	}

	/**
	 * Sum of the quantities across all the inventories for this product
	 * */
	public int getTotalQuantity(){
		int total = 0;
		for(int i=0; i < inventoryList.size(); i++){
			total += inventoryList.get(i).getQuantity();
		}
		return total;
	}


	public int getAvailQuantity(){
		return availableQuantity;
	}

	public void setAvailQuantity(int q){
		if (q <= 0){ //<=
			availableQuantity = 0;
		} else {
			availableQuantity = q;
		}
	}

	@Override
	public String toString(){
		return category.toString() + ", " + ID + ", " + name + ", " + price + "\n" + "Available Quantity: " + getAvailQuantity()
		+ "\n" + "Total Quantity: " + getTotalQuantity();
	}

	@Override
	public int compareTo(Product o) {
		return this.getID() - o.getID();
	}

	public static ComparatorProducts<Product> NameComparator = new ComparatorProducts<Product>(){
		/**
		 * Sorts all the products by name in alphabetical order
		 */
		private static final long serialVersionUID = -3497757110519047673L;

		@Override
		public int compare(Product p1, Product p2){
			return p1.getName().compareTo(p2.getName());
		}
	};

	public static ComparatorProducts<Product> PriceComparator = new ComparatorProducts<Product>(){
		/**
		 * Sorts all the products by price in increasing numeric order
		 */
		private static final long serialVersionUID = -7810526702410198289L;

		@Override
		public int compare(Product p1, Product p2){
			int result = 0;
			if(p1.getPrice()>p2.getPrice())
				result = 1;
			else result = -1;
			return result;
		}
	};

	public static ComparatorProducts<Product> QuantityComparator = new ComparatorProducts<Product>(){
		/**
		 * Sorts all the products by quantity in increasing numeric order
		 */
		private static final long serialVersionUID = 4070865799258335661L;

		@Override
		public int compare(Product p1, Product p2){
			int result = 0;
			if(p1.getAvailQuantity()>p2.getAvailQuantity())
				result = 1;
			else result = -1;
			return result;
		}
	};

	@Override
	/**
	 * Updates all of the distribution centers to have zero quantity of new product in inventory 
	 * whenever a new product is made by the admin.
	 */
	public void update(DistributionCenter dist) {
		for(int i=0; i < dist.getInventory().size(); i++){
			if(dist.getInventory().get(i).getProduct().equals(this)){
				Inventory newInv = dist.getInventory().get(i);
				inventoryList.add(newInv);
			}
		}
	}

	public String display(){
		String result = "Product: " + name + "\n" + "Price: $" + price
				+ "\n" + "Available Quantity: " + availableQuantity + "\n" + "Total Quantity: " + totalQuantity + "\n"
				+ "Quantities: " + "\n";
		for(int i=0; i < inventoryList.size(); i++){
			result += inventoryList.get(i).getDistributionCenter().getCity() + ":"
					+ inventoryList.get(i).getQuantity() + "\n";
		}
		return result;
	
	}
	
	/**
	 * Get the amount of inventory of this product from a distribution center.
	 * @param d -> the distribution center inquired.
	 * @return -> amount of inventory of this product from d.
	 */
	public int getAmount(DistributionCenter d){
		for(int i=0; i < inventoryList.size(); i++){
			if(inventoryList.get(i).getDistributionCenter().equals(d)) 
				return inventoryList.get(i).getQuantity();
		} return 0;
	}
}
