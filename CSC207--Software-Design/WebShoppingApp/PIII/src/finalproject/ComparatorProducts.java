package finalproject;

import java.io.Serializable;
import java.util.Comparator;

public abstract class ComparatorProducts<T> implements Comparator<T>, Serializable
{
 /**
  * The ComparatorProducts abstract class is used since the category class maintains the products in three lists, one ordered alphabetically by name
  * and the other two ordered numerically by available stock and price.
  * 
  * @author Conrad
  * @version 1.0
  * */
	
	private static final long serialVersionUID = -6365387261825447005L;

public abstract int compare(T t1, T t2);
}