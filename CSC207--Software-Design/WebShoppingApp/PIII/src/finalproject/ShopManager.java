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
 * Manages saving and loading of shops
 * @author Vincent
 *
 */
public class ShopManager {
	private static final Logger logger =
			Logger.getLogger(UserManager.class.getName());
	private static final Handler consoleHandler = new ConsoleHandler();
	
	/** A mapping of shopUser IDs to shop. */
	protected HashMap<String, Shop> shopUser;
	protected Shop shop;
	
	/** Creates a new empty ShopManager. */
	public ShopManager() {
		shop = new Shop();
		shopUser = new HashMap<>();
		shopUser.put("OurShop", shop);
//		hh = new HashMap<Integer, String>();
		
	}
	
	/** Creates a new ShopManager for the students whose
	 * information is stored in filePath
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException if filePath is not valid
	 * @throws ClassNotFoundException 
	 */
	
	public ShopManager(String filePath) throws IOException, ClassNotFoundException {
		shopUser = new HashMap<String,Shop>();
		shop = new Shop();
		shopUser.put("OurShop", shop);
		
		logger.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		
		File file = new File(filePath);
//		InputStream reader = new FileInputStream(file);
//		InputStream buffer = new BufferedInputStream(reader);
//		ObjectInput input = new ObjectInputStream(buffer);
		
		if(file.exists()) {
			readFromFile(file.getPath());
		} else {
			file.createNewFile();
			shopUser.put("OurShop", shop);
			saveToFile(filePath);
//			readFromFile(file.getPath());
		}
	}

	 
	 /** Adds record to this ShopManager.
	  * @param record a record to be added.
	  */
	 public void add(Shop record) {
		 shopUser.put("OurShop", record);
		 logger.log(Level.FINE, "Added new Shop " + record.toString());
	 }
	 
	 /** Writes the Users to file at filePath.
	  * @param filePath the file to write records to
	  * @throws IOException
	  */
	 public void saveToFile(String filePath) throws IOException {
		 OutputStream file = new FileOutputStream(filePath);
		 OutputStream buffer = new BufferedOutputStream(file);
		 ObjectOutput output = new ObjectOutputStream(buffer);
		 
		 // serialize the map
		 
		 output.writeObject(shopUser);
		 output.close();
		 buffer.close();
	 }
	 
	 @SuppressWarnings("unchecked")
	 protected void readFromFile(String path) throws ClassNotFoundException {
		 try {
				 InputStream file = new FileInputStream(path);
				 if(file.available()>0){
					 InputStream buffer = new BufferedInputStream(file);
					 ObjectInput input = new ObjectInputStream(buffer);
				 
				 
			 // deserialize the Map
			 shopUser = (HashMap<String,Shop>)input.readObject();
				 }
		 } catch(IOException ex) {
			 logger.log(Level.SEVERE, "Cannot read from input", ex);
		 }
	 }
}
