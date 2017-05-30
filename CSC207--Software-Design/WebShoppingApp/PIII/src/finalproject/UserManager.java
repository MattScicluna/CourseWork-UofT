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
 * Manages saving and loading of RegisteredUsers
 * @author Vincent H
 *
 */
public class UserManager {
	private static final Logger logger =
			Logger.getLogger(UserManager.class.getName());
	private static final Handler consoleHandler = new ConsoleHandler();
	
	/** A mapping of User IDs to Users. */
	protected Map<String,RegisteredUser> mapUser;
	
	/** Creates a new empty UserManager. */
	public UserManager() {
		mapUser = new HashMap<String, RegisteredUser>();
//		hh = new HashMap<Integer, String>();
		
	}
	
	/** Creates a new UserManager for the Registered users whose
	 * information is stored in filePath
	 * @param filePath
	 * @throws FileNotFoundException 
	 * @throws IOException if filePath is not valid
	 * @throws ClassNotFoundException 
	 */
	
	public UserManager(String filePath) throws IOException, ClassNotFoundException {
		mapUser = new HashMap<String, RegisteredUser>();
		
		
		logger.setLevel(Level.ALL);
		consoleHandler.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		
		File file = new File(filePath);
		if(file.exists()) {
			readFromFile(file.getPath());
		} else
			file.createNewFile();
		
	}

	 
	 /** Adds record to this UserManager.
	  * @param record a record to be added.
	  */
	 public void add(RegisteredUser record) {
		 mapUser.put(record.getUserID(), record);
		 logger.log(Level.FINE, "Added new RegisteredUser " + record.toString());
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
		 output.writeObject(mapUser);
		 
		 output.close();
	 }
	 
	 @SuppressWarnings("unchecked")
	 protected void readFromFile(String path) throws ClassNotFoundException {
		 try {
				 InputStream file = new FileInputStream(path);
				 InputStream buffer = new BufferedInputStream(file);
				 ObjectInput input = new ObjectInputStream(buffer);
				 
			 // deserialize the Map
			 mapUser = (HashMap<String,RegisteredUser>)input.readObject();
			 input.close();

		 }
		  catch(IOException ex) {
			 logger.log(Level.SEVERE, "Cannot read from input", ex);
		 }
	 }
//	 @SuppressWarnings("unchecked")
//	 private void readFromFile(String path) throws ClassNotFoundException {
//		 try (
//				 InputStream file = new FileInputStream(path);
//				 InputStream buffer = new BufferedInputStream(file);
//				 ObjectInput input = new ObjectInputStream(buffer);
//				 ) {
//			 // deserialize the Map
//			 mapUser = (HashMap<String,RegisteredUser>)input.readObject();
//		 } catch(IOException ex) {
//			 logger.log(Level.SEVERE, "Cannot read from input", ex);
//		 }
//	 }
	 
	 @Override
	 public String toString() {
		 String result = "";
		 for(RegisteredUser s : mapUser.values()) {
			 result += s.toString() + "\n";
		 }
		 return result;
	 }
}
