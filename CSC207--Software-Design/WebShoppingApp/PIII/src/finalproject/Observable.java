package finalproject;


/**
 * Interface implemented by shop
 * 
 * <p>
 * The addition of the distribution center notified the products
 * 
 * @author Conrad
 * @version 1.0
 */
public interface Observable {
	
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObserver();
}
