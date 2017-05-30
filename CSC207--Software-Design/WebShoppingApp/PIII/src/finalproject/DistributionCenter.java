package finalproject;

import java.io.Serializable;
import java.util.ArrayList;

public class DistributionCenter implements Serializable{
	/**
	 * Distribution centers exist in some cities. This is where inventories of products are maintained.
	 * 
	 * @author Conrad
	 * @version 1.0
	 */
	private static final long serialVersionUID = -1708149329019169610L;
	private City city;
	protected ArrayList<Inventory> inventory;
	
	public DistributionCenter(City city){
		this.city = city;
		inventory = new ArrayList<Inventory>();
	}
	
	public City getCity(){
		return city;
	}
	
	public void addInventory(Inventory inven){
		inventory.add(inven);
	}
	
	public ArrayList<Inventory> getInventory(){
		return inventory;
	}
	
	public String display(){
		return "City: " + city + "\n" + inventory.toString();
	}
	
	@Override
	public boolean equals(Object o){
		DistributionCenter n = (DistributionCenter)o;
		return (n.getCity().equals(city));
	}
	
	/**
	 * Purchases an item from the distribution center and decreases the quantity of said item.
	 * @param prodID -> The product ID of the item you want to purchase.
	 * @param quantity -> the quantity of the item you want to purchase.
	 */
	public void purchaseItem(int prodID, int quantity){
		for(int i=0; i < inventory.size(); i++){
			if(inventory.get(i).getProduct().getID() == prodID){
				if(quantity <= inventory.get(i).getQuantity()){
					inventory.get(i).purchase(quantity);
				}
			}
			
		}
	}
}
