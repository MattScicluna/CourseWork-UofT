package finalproject;

import java.io.IOException;


public class UserManagerTest {


	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub

		demoShopManager();
	}

	public static void demoShopManager() 
	            throws IOException, ClassNotFoundException
	{
		String path = "UserData.bin";
		UserManager sm = new UserManager(path);
		System.out.println("Current Objects in file: " + "\n" + sm);
		sm.add(new Admin( "useridadmin","pass1", true));
		sm.add(new Shopper( "useridstudent","pass2", false));
		System.out.println("Newly added/Overwritten Objects in file:" + "\n" + sm);
		System.out.println("List of Object Keys: " + "\n" + sm.mapUser.keySet());
		sm.saveToFile(path);
		
		UserManager open = new UserManager(path);
		
		
		
	}
}
