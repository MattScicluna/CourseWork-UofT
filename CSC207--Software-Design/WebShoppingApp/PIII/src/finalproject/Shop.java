package finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Shop implements Observable, Serializable {


	/**
	 * The shop holds a list of products, categories, distribution centers and users. It is responsible for
	 * directing the workflow in this application.
	 *  
	 * @author Vincent
	 * @version 1.0
	 */
	private static final long serialVersionUID = -5761353580942776308L;

	Graph<City> cityGraph;
	final double SHIPPINGFACTOR;
	protected List<Product> observers;
	List<Category> category;
	List<DistributionCenter> distributionCenters;
	List<RegisteredUser> users; 

	public Shop() {
		SHIPPINGFACTOR = 1;
		this.category = new ArrayList<Category>();
		observers = new ArrayList<Product>();
		distributionCenters = new ArrayList<DistributionCenter>();
		users = new ArrayList<RegisteredUser>();
		this.cityGraph = new Graph<City>();
		
	}

	/**
	 * Search all the products available at the store using parameters given by user.
	 * browse is called by limitPrice always
	 * @param PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown -> user specifications for search
	 * @return -> list of products sorted by user specifications
	 * 
	 */
	protected List<Product> browse(String PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown) { // or CAT name
		
		List<Product> productList = new ArrayList<>();
		String key = PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown;
		Iterator<Product> iterator = null;

		if (key.equals("PRICEUp") || key.equals("PRICEDown")) {
			for(int i = 0; i < this.category.size(); i++) { //cat
				if (key.equals("PRICEUp")) {
					iterator = this.category.get(i).products_price.iterator();
					}
				else if (key.equals("PRICEDown")) {
					iterator = this.category.get(i).products_price.descendingIterator();
				}
				while (iterator.hasNext()){
					productList.add(iterator.next());
				}
			}
		}
		else if (key.equals("ALPHAUp") || key.equals("ALPHADown")) {
			for(int i = 0; i < this.category.size(); i++) { //cat
				if (key.equals("ALPHAUp")) {
					iterator = this.category.get(i).products_alph.iterator();
					}
				else if (key.equals("ALPHADown")) {
					iterator = this.category.get(i).products_alph.descendingIterator();
				}
				while (iterator.hasNext()){
					productList.add(iterator.next());
				}
			}
		}
		else if (key.equals("AVAILUp") || key.equals("AVAILDown")) {
			for(int i = 0; i < this.category.size(); i++) { //cat
				if (key.equals("AVAILUp")) {
					iterator = this.category.get(i).products_avail.iterator();
					}
				else if (key.equals("AVAILDown")) {
					iterator = this.category.get(i).products_avail.descendingIterator();
				}
				while (iterator.hasNext()){
					productList.add(iterator.next());
				}
			}
		}
		else { // if sorted by CategoryName, then also sorted by price in ascending order
			for(int i = 0; i < this.category.size(); i++) {
				if (key.equals(this.category.get(i).toString())) {
					iterator = this.category.get(i).products_alph.iterator();
					while (iterator.hasNext()) {
						productList.add(iterator.next());
					}
					break;
				}
			}
		}
		return productList;	
	}
	
	/**
	 * Search all the products available at the store using parameters given by user.
	 * browse is called by limitPrice always
	 * @param x -> lower price bound of products to be considered
	 * @param y -> upper price bound of products to be considered
	 * @param PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown -> user specifications for search
	 * @return -> list of products sorted by user specifications
	 * 
	 */
	protected List<Product> limitPrice(double x, double y, String PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown) { // or CAT name
		
		List<Product> objList =  browse(PRICEUp_PRICEDown_ALPHAUp_ALPHADown_AVAILUp_AVAILDown); // or CAT name
		List<Product> productList = new ArrayList<Product>();
		if (x == 0 && y == 0) {
			productList = objList;
		}
		else {
			for(int i = 0; i < objList.size(); i++) {
				Product obj = objList.get(i);
				if (obj.price >=x && obj.price <=y) {
					productList.add(obj);
				}
			}
		}
		return productList;
	}
	@Override
	public void registerObserver(Observer o) {
		observers.add((Product) o);
	}
	@Override
	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0) observers.remove(i);
	}
	
	/**
	 * notify is only called when adding a new distribution center, so it should only pertain to the last added item.
	 */
	@Override
	public void notifyObserver() {
		for (int i=0; i < observers.size(); i++){
			Observer obs = observers.get(i);
			obs.update(distributionCenters.get(distributionCenters.size() - 1));
		}
	}


	public List<Product> getObservers() {
		return observers;
	}
}
