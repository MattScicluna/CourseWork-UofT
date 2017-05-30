package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopItem implements Serializable{

	/**
	 * tuple of products along with the quantities purchased and a flag.
	 * These are held in the cart.
	 * The flag exists to ensure that no changes were done to the product that would 
	 * prevent it from being purchased. The flag is not implemented in the current version of the application.
	 *  
	 * @author Matt
	 * @version 1.0
	 */
	private static final long serialVersionUID = -7564774375972534867L;
	private Product product;
	private int Qty;
	private double price; // price it was purchased at
	
	ShopItem(Product product, int Qty, double price){
		this.product = product;
		this.Qty = Qty;
		this.price = price;
	}
	
	public Product getProduct(){
		return product;
	}
	
	public int getQuantity(){
		return Qty;
	}
	
	public void setQuantity(int q){
		Qty = q;
	}
	
	
	@Override
	public String toString(){
		return product.getName() + " " + product.getID() + " " + Qty + " " + price;
	}
}
