package finalproject;

import java.io.Serializable;
import java.util.TreeSet;

public class Category implements Serializable{
	/**
	 * The category is a set of products united by a common theme. It is used by the customer for their convenience.
	 * It is added by the Admin. 
	 * <p>
	 * 
	 * The category maintains the products in three lists, one ordered alphabetically by name
	 * and the other two ordered numerically by available stock and price.
	 * 
	 * @author Conrad
	 * @version 1.0
	 * @see Admin#addCategory(String, Shop)
	 */
	private static final long serialVersionUID = 5040959467290273083L;
	static int categoryIdGenerator = 0; 
	int categoryID;
	
	private String categoryName;
	TreeSet<Product> products_alph;
	TreeSet<Product> products_avail;
	TreeSet<Product> products_price;
	
	public Category(String categoryName) {
		categoryIdGenerator++;
		this.categoryID = categoryIdGenerator;
		
		this.categoryName = categoryName;
		this.products_alph = new TreeSet<Product>(Product.NameComparator);
		
		this.products_price = new TreeSet<Product>(Product.PriceComparator);
		
		this.products_avail = new TreeSet<Product>(Product.QuantityComparator);
	}
	
	public void addProduct(Product product){
		products_avail.add(product);
		products_alph.add(product);
		products_price.add(product);
		
	}
	
	public void updateAvail(Product product){
		if(products_avail.contains(product)){
			products_avail.remove(product);
			products_avail.add(product);
		}
	}

	
	public void updateProduct(Product p){
		if(products_price.contains(p)){
			products_price.remove(p);
			products_price.add(p);
		}
	}
	
	public String display_alph(){
		return products_alph.toString();
	}
	
	public String display_price(){
		return products_price.toString();
	}
	
	public String display_avail(){
		return products_avail.toString();
	}
	
	@Override
	public String toString(){
		return categoryName;
	}

	public int getCategoryID() {
		return this.categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}

