package finalproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Invoice implements Serializable{
	/**
	 * Invoice is produced once a customers shopping cart in checked out and all the products are
	 * successfully purchased.
	 * 
	 * <p>
	 * Invoices contain the products ordered along with their respective prices and quantities. Also included
	 * is the date, order number and a unique ID specifier. 
	 * Invoices are maintained in a list owned by the customer, for their reference.
	 * 
	 * @author Conrad
	 * @version 1.0
	 */
	private static final long serialVersionUID = 4024602335347459671L;
	private static int orderNumber = 1;
	private int ID;
	private Customer customer;
	private String Date; // not used
	private double price;
	private ArrayList<ShopItem> productList;
	
		public Invoice(Customer customer) {
			productList = new ArrayList<ShopItem>();
			this.customer = customer;
			this.ID = orderNumber;
			orderNumber++;
			price = 0;
		}
		
		/**
		 * Adds an item to the invoice.
		 * @param item -> item added to the invoice.
		 */
		public void addToInvoice(ShopItem item){
			productList.add(item);
			price += item.getProduct().getPrice() * item.getQuantity();
		}
		public int getID() {
			return ID;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public ArrayList<ShopItem> getProductList(){
			return productList;
		}
		
		@Override
		public String toString(){
			String result = "<html>Invoice #: " + orderNumber + "<br>" + "Customer: " + customer.customerName + "<br>";
			for(int i=0; i < productList.size(); i++){
				result += "Product: " + productList.get(i).getProduct().getName() + ", Quantity: " + productList.get(i).getQuantity()
						+ ", Price (per item): " + productList.get(i).getProduct().getPrice() + "<br>"; 
			}
			result += "Total Price (including shipping): " + this.price + "</html>";
			return result;
		}
		
}
