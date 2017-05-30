package finalproject;

import java.io.Serializable;

public class VertexExistsException extends Exception implements Serializable{
	
	/**
	 * thrown whenever user attempts to add a vertex to the graph that already exists.
	 * 
	 * @author Gal
	 * @version 1.0
	 *
	 */
	private static final long serialVersionUID = -169812824189677829L;

	public VertexExistsException(){
		super("The vertex already exists!");
	}
	
	public VertexExistsException(String message){
		super(message);
	}
}