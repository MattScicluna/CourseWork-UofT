package finalproject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages saving and loading of products
 * @author Vincent
 *
 */
public class ProductManager {
	private static final Logger logger =
			Logger.getLogger(UserManager.class.getName());
	private static final Handler consoleHandler = new ConsoleHandler();
	
	/** A mapping of student ids to products. */
	protected Map<String, Product> mapUser;
	
	/** Creates a new empty ProductManager. */
	public ProductManager() {
		mapUser = new HashMap<String, Product>();
//		hh = new HashMap<Integer, String>();
		
	}
	
	/** Creates a new ProductManager for the products whose
	 * information is stored in filePath
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException if filePath is not valid
	 * @throws ClassNotFoundException 
	 */
	
	public ProductManager(String filePath) throws IOException, ClassNotFoundException {
		mapUser = new HashMap<String, Product>();
		
		
		logger.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		
		File file = new File(filePath);
		if(file.exists()) {
			readFromFile(file.getPath());
		} else
			file.createNewFile();
		
	}

	 
	 /** Adds record to this ProductManager.
	  * @param record a record to be added.
	  */
	 public void add(Product record) {
		 mapUser.put(Integer.toString(record.getID()), record); // productID is really an Integer, but here we convert to string
		 logger.log(Level.FINE, "Added new RegisteredUser " + record.toString());
	 }
	 
	 /** Writes the products to file at filePath.
	  * @param filePath the file to write records to
	  * @throws IOException
	  */
	 public void saveToFile(String filePath) throws IOException {
		 OutputStream file = new FileOutputStream(filePath);
		 OutputStream buffer = new BufferedOutputStream(file);
		 ObjectOutput output = new ObjectOutputStream(buffer);
		 
		 // serialize the map
		 output.writeObject(mapUser);
		 output.close();
	 }
	 
	 @SuppressWarnings("unchecked")
	 protected void readFromFile(String path) throws ClassNotFoundException {
		 try (
				 InputStream file = new FileInputStream(path);
				 InputStream buffer = new BufferedInputStream(file);
				 ObjectInput input = new ObjectInputStream(buffer);
				 ) {
			 // deserialize the Map
			 mapUser = (Map<String,Product>)input.readObject();
		 } catch(IOException ex) {
			 logger.log(Level.SEVERE, "Cannot read from input", ex);
		 }
	 }
	 
	 @Override
	 public String toString() {
		 String result = "";
		 for(Product s : mapUser.values()) {
			 result += s.toString() + "\n";
		 }
		 return result;
	 }
}
