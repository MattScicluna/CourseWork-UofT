package finalproject;

/**
 * 
 */

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
 * Manages saving and loading of Shops.
 * @author Vincent
 *
 */
public class ShopManager2 {
	private static final Logger logger =
			Logger.getLogger(ShopManager2.class.getName());
	private static final Handler consoleHandler = new ConsoleHandler();
	
	protected Shop shop;
	
	/** Creates a new empty ShopManager2. */
	public ShopManager2() {
		shop = new Shop();
//		hh = new HashMap<Integer, String>();
		
	}
	
	/** Creates a new ShopManager for the shop whose
	 * information is stored in filePath
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException if filePath is not valid
	 * @throws ClassNotFoundException 
	 */
	public ShopManager2(String filePath) throws IOException, ClassNotFoundException {
		shop = new Shop();
		
		logger.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		
		File file = new File(filePath);
		if(file.exists()) {
			readFromFile(file.getPath());
		} else
			file.createNewFile();
		
	}

	/** Populates the records map from the file at path filePath.
	 * @param filePath the path of data file.
	 * @throws FileNotFoundException if filePath is invalid
	 */
	 
	 /** Adds record to this ShopManager.
	  * @param record a record to be added.
	  */
	 public void add(Shop shop) {
		 this.shop = shop;
		 logger.log(Level.FINE, "Added new student " + shop.toString());
	 }
	 
	 /** Writes the students to file at filePath.
	  * @param filePath the file to write records to
	  * @throws IOException
	  */
	 public void saveToFile(String filePath) throws IOException {
		 OutputStream file = new FileOutputStream(filePath);
		 OutputStream buffer = new BufferedOutputStream(file);
		 ObjectOutput output = new ObjectOutputStream(buffer);
		 
		 // serialize the map
		 output.writeObject(shop);
		 
		 output.flush();
		 output.close(); 
	 }
	 
	 private void readFromFile(String path) throws ClassNotFoundException, IOException{
		 try {
				 InputStream file = new FileInputStream(path);
				 InputStream buffer = new BufferedInputStream(file);
				 ObjectInput input = new ObjectInputStream(buffer);
		  
			 // deserialize the Shop
				 
			 this.shop = (Shop) input.readObject();
			 input.close();

		 }
		  catch(IOException ex) {
			 logger.log(Level.SEVERE, "Cannot read from input", ex);
		 }
	 }

//	 @Override
//	 public String toString() {
//		 String result = "";
//		 for(Student s : students.values()) {
//			 result += s.toString() + "\n";
//		 }
//		 return result;
//	 }
}
