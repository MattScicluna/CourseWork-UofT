package finalproject;

import java.io.Serializable;

public class City implements Serializable{
	/**
	 * The city class is used as the nodes of a graphical representation of the map.
	 * This representation is used to compute shipping costs.
	 * Cities can be built by and updated by Admin class only.
	 * 
	 * <p>
	 * Cities can be the address of customers and the address of distribution centers
	 * 
	 * @author Matt
	 * @version 1.0
	 * @see Admin#addCity(String, Shop)
	 */
	private static final long serialVersionUID = -7377488982991588276L;
	private String cityName;
	
	public City(String cityName){
		this.cityName = cityName;
	}
	
	public String getCityName(){
		return cityName;
	}
	
	@Override
	public boolean equals(Object o){
		City n = ((City)o);
		return(cityName.equals(n.getCityName()));
	}
	
	@Override
	public String toString(){
		return this.cityName;
	}

}
