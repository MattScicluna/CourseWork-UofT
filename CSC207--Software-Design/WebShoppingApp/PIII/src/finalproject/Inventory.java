package finalproject;

import java.io.Serializable;
import java.util.List;

public class Inventory implements Comparable<Inventory>,Serializable {
	/**
	 * Inventory of product belonging to a distribution center. Used to manage the available quantity.
	 * 
	 * <p>
	 * Distribution centers maintain inventories of products of a certain quantity.
	 * 
	 * @author Vincent
	 * @version 1.0
	 */
	private static final long serialVersionUID = 8011454136317166966L;
	private DistributionCenter distributionCenter;
	private Product product;
	private int quantity;
	
	public Inventory(Product product, DistributionCenter distributionCenter, int quantity){
		this.product = product;
		this.distributionCenter = distributionCenter;
		this.quantity = quantity;
	}
	
	public DistributionCenter getDistributionCenter(){
		return distributionCenter;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	/**
	 * Sets the quantity of the inventory, the available quantity adjusts accordingly to whether the new quantity
	 * is higher or lower.
	 * @param q -> The new quantity you want to set.
	 */
	public void setQuantity(int q){
		int avail = product.getAvailQuantity();
		if(q > quantity){
			product.setAvailQuantity(avail + q - quantity);
		} else {
			product.setAvailQuantity(avail - (quantity - q));
		}
		this.quantity = q;
		product.setTotalQuantity();
		product.getCategory().updateAvail(product);
	}
	
	public void purchase(int q){
		if(quantity >= q){
			quantity -= q;
		}
	}

	public Product getProduct(){
		return product;
	}

	/**
	 * Compares inventories by their available quantity of product.
	 * @param o -> inventory to be compared to
	 * @return -> positive integer if quantity of product in this inventory is higher then the other inventory,
	 * 			  zero if they are the same, and negative integer otherwise.
	 */
	@Override
	public int compareTo(Inventory o) {
		return this.getQuantity()-o.getQuantity();
	}
	
	@Override
	public String toString(){
		String result = "Product: " + product.getName() + " -- Quantity: " + quantity;
		return result;
	}
}
